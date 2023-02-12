package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inshta.Models.editProfileModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivityProfileEditBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class profileEdit extends AppCompatActivity {
    ActivityProfileEditBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.toolbar.setNavigationOnClickListener(view -> {

            onBackPressed();
            overridePendingTransition(0,0);

        });

        binding.updateButton.setOnClickListener(view -> {

            String country = binding.countryEt.getText().toString().trim();
            String profession = binding.professioneEt.getText().toString().trim();
            String relation = binding.relationEt.getText().toString().trim();
            String gender = binding.genderEt.getText().toString().trim();
            String birthday = binding.birthdayEt.getText().toString().trim();

            if (country.isEmpty()){
                showToast("Enter country");
            }else if (profession.isEmpty()){
                showToast("Enter profession");
            }else if (relation.isEmpty()){
                showToast("Enter your relation");
            }else if (gender.isEmpty()){
                showToast("Enter your gender Male/Female");
            }else if (birthday.isEmpty()) {
                showToast("write your birthday date");
            }else {
                editProfileModel model = new editProfileModel(country,profession,relation,birthday,gender);
                enableProgressBar();
                database.getReference()
                        .child("Users")
                        .child(auth.getUid())
                        .child("profileInfo")
                        .setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    showToast("Profile Updated Successfully");
                                    startActivity(new Intent(getApplicationContext(),home.class));
                                    binding.countryEt.setText("");
                                    binding.professioneEt.setText("");
                                    binding.relationEt.setText("");
                                    binding.genderEt.setText("");
                                    disableProgressBar();
                                }
                                else {
                                    showToast("something is wrong");
                                    disableProgressBar();
                                }
                            }
                        });


            }

        });

    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void enableProgressBar(){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.updateButton.setVisibility(View.INVISIBLE);
    }
    private void disableProgressBar(){
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.updateButton.setVisibility(View.VISIBLE);
    }
}