package com.example.inshta.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.inshta.R;
import com.example.inshta.databinding.ActivityLoginBinding;

public class login extends AppCompatActivity {

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);




        binding.loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),home.class);
            startActivity(new Intent(intent));
        });



        binding.createNewAccount.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);


        });
    }
}