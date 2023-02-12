package com.example.inshta.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.inshta.R;
import com.example.inshta.databinding.ActivitySplashScreenBinding;

public class splash_screen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    Animation top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       top = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logo_anime);

       binding.iconImage.setAnimation(top);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),login.class));
                overridePendingTransition(0,0);
            }
        },3000);

    }
}