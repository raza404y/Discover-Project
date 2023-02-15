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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileEdit extends AppCompatActivity {
    ActivityProfileEditBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String notAdded = "Not Added";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Inshta);
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

        database.getReference()
                .child("Users")
                .child(auth.getUid())
                .child("profileInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){

                                editProfileModel profileModel = snapshot.getValue(editProfileModel.class);
                                binding.countryEt.setText(profileModel.getCountry());
                                binding.relationEt.setText(profileModel.getRelation());
                                binding.genderEt.setText(profileModel.getGender());
                                binding.professioneEt.setText(profileModel.getProfession());
                                binding.birthdayEt.setText(profileModel.getBirthday());
//                            }else {
//                                binding.professioneEt.setHint(notAdded);
//                                binding.countryEt.setHint(notAdded);
//                                binding.relationEt.setHint(notAdded);
//                                binding.birthdayEt.setHint(notAdded);
//                                binding.genderEt.setHint(notAdded);
                            }
                        }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.updateButton.setOnClickListener(view -> {


            try {
                String country = binding.countryEt.getText().toString().trim();
                String profession = binding.professioneEt.getText().toString().trim();
                String relation = binding.relationEt.getText().toString().trim();
                String gender = binding.genderEt.getText().toString().trim();
                String birthday = binding.birthdayEt.getText().toString().trim();


                editProfileModel model = new editProfileModel(country, profession, relation, birthday, gender);
                enableProgressBar();
                database.getReference()
                        .child("Users")
                        .child(auth.getUid())
                        .child("profileInfo")
                        .setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showToast("Profile Updated Successfully");
                                    startActivity(new Intent(getApplicationContext(), home.class));
                                    binding.countryEt.setText("");
                                    binding.professioneEt.setText("");
                                    binding.relationEt.setText("");
                                    binding.genderEt.setText("");
                                    disableProgressBar();
                                } else {
                                    showToast("something is wrong");
                                    disableProgressBar();
                                }
                            }
                        });

            }catch (Exception e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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