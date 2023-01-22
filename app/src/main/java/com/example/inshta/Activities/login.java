package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inshta.R;
import com.example.inshta.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        auth = FirebaseAuth.getInstance();


        binding.loginBtn.setOnClickListener(view -> {

            loginUser();

        });



        binding.createNewAccount.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);


        });
    }

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
                        disableProgressBar();
                        Intent intent = new Intent(getApplicationContext(),home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();

        if (auth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}