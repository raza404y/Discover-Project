package com.example.inshta.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inshta.Models.bioModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivityProfileBioBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class profileBio extends AppCompatActivity {

    ActivityProfileBioBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setSupportActionBar(binding.toolabr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.toolabr.setNavigationOnClickListener(view -> {

            onBackPressed();
            overridePendingTransition(0,0);

        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.updateBio.setOnClickListener(view -> {


            String bio = binding.bioEt.getText().toString().trim();

            if (bio.isEmpty()){
                Toast.makeText(this, "Write something", Toast.LENGTH_SHORT).show();
            }else {

                bioModel biomdel = new bioModel();
                biomdel.setBio(bio);
                database.getReference()
                        .child("Users")
                        .child(auth.getUid())
                        .child("bio")
                        .setValue(biomdel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(profileBio.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), home.class));
                            }
                        });
                binding.bioEt.setText("");

            }
        });


    }
}