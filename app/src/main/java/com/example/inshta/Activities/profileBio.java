package com.example.inshta.Activities;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileBio extends AppCompatActivity {

    ActivityProfileBioBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Inshta);
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




        database.getReference()
                .child("Users")
                .child(auth.getUid())
                .child("bio").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            bioModel model = snapshot.getValue(bioModel.class);
                            binding.bioEt.setText(model.getBio());
                        }else {
                            binding.bioEt.setText("This is user didn't write anything about himself.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        binding.updateBio.setOnClickListener(view -> {


            String bio = binding.bioEt.getText().toString().trim();

            if (bio.isEmpty()){
                binding.bioEt.setText("This is user didn't write anything about himself.");
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