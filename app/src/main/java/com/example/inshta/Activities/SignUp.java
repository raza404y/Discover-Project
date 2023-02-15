package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inshta.Models.Users;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Inshta);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        binding.signupBtn.setOnClickListener(view -> {

            signupUser();

        });


        binding.alreadyHaveAccount.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(),login.class);
            startActivity(intent);

        });
    }

    //############# Methods ###############

    private void signupUser() {

        String name = binding.nameEt.getEditText().getText().toString().trim();
        String profession = binding.prfoessionEt.getEditText().getText().toString().trim();
        String email = binding.emailEt.getEditText().getText().toString().trim();
        String password = binding.passwordEt.getEditText().getText().toString().trim();


        if (name.isEmpty()){
                showToast("Enter your Name");
        }else if(profession.isEmpty()) {
            showToast("Enter Profession");
        }else if (email.isEmpty()){
                showToast("Enter your Email");
        } else if (password.isEmpty()){
                showToast("Enter your Password");
        } else if (!email.matches(emailPattern)){
                showToast("Enter a valid Email");
        }else if (password.length()<6){
                showToast("Password must be 6 characters");
        }else if (name.length()<5){
                showToast("Name should be greater than 5 characters");
        }else if (profession.length()<5) {
                showToast("Profession at least greater than 5 characters");
        }else {

                enableProgressBar();
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){


                        Users users = new Users(name,profession,email,password);
                        String id = task.getResult().getUser().getUid();

                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                DatabaseReference reference = database.getReference().child("Users").child(id);
                                reference.setValue(users);

                                if (task.isSuccessful()){
                                    disableProgressBar();
                                    Intent intent = new Intent(SignUp.this,login.class);
                                    startActivity(intent);
                                    finish();
                                    showToast("Account created Successfully , Please verify your email");
                                }
                                else {
                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });



                    }else {
                                disableProgressBar();
                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            });


        }


    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void enableProgressBar(){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.signupBtn.setVisibility(View.INVISIBLE);
    }
    private void disableProgressBar(){
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.signupBtn.setVisibility(View.VISIBLE);
    }
}