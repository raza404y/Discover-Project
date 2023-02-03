package com.example.inshta.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.inshta.R;
import com.example.inshta.databinding.ActivitySettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class settings extends AppCompatActivity {

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        setSupportActionBar(binding.settingTOolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.settingTOolbar.setNavigationOnClickListener(view -> {

            onBackPressed();
            overridePendingTransition(0,0);

        });



        binding.personalInformationTv.setOnClickListener(view -> {

            startActivity(new Intent(getApplicationContext(),personalInformation.class));
            overridePendingTransition(0,0);

        });

        binding.editProfileTv.setOnClickListener(view -> {

            startActivity(new Intent(getApplicationContext(),profileEdit.class));
            overridePendingTransition(0,0);

        });

        binding.logoutTv.setOnClickListener(view -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),login.class));
            overridePendingTransition(0,0);
        });
    }
}