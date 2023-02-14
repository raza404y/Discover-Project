package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inshta.R;
import com.example.inshta.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    String value;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        auth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences1 = getSharedPreferences("user",MODE_PRIVATE);
        if (sharedPreferences1.contains("key")){
           Intent intent = new Intent(getApplicationContext(),home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        binding.loginBtn.setOnClickListener(view -> {
            loginUser();

        });



        binding.createNewAccount.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);


        });
            binding.resendVerificationlink.setOnClickListener(view -> {

                try {
                    Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(login.this, "Please wait we're sending an verification link to your Email", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }catch (Exception e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                });

        binding.forgetPasswordTv.setOnClickListener(view -> {
            String mail = binding.emailEt.getEditText().getText().toString().trim();
            if (mail.isEmpty()){
                Toast.makeText(this, "Enter email to reset password", Toast.LENGTH_SHORT).show();
            }else if (!mail.matches(emailPattern)){
                showToast("Enter a valid email");
            }else {

            auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(login.this, "Please wait we're sending an reset password link to your Email.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            }
        });

    }

    /// Methods /////////////////
    private void loginUser() {

        String email = binding.emailEt.getEditText().getText().toString().trim();
        String password = binding.passwordEt.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            showToast("Enter your email");
        }else if (password.isEmpty()){
            showToast("Enter your password");
        }else {

            enableProgressBar();
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()){
                            disableProgressBar();
                            Intent intent = new Intent(getApplicationContext(),home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            value = "done";
                            SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key",value);
                            editor.apply();

                        }else {
                            disableProgressBar();
                            Toast.makeText(login.this, "We sent an verification link to your email, Verify your email to login ", Toast.LENGTH_LONG).show();
                        }


                    }else {
                        disableProgressBar();
                        showToast("Email or password wrong");
                    }

                }
            });


        }
    }

    // ############# Methods ###############


    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void enableProgressBar(){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginBtn.setVisibility(View.INVISIBLE);
    }
    private void disableProgressBar(){
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.loginBtn.setVisibility(View.VISIBLE);
    }
}