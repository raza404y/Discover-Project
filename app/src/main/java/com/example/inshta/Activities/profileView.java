package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.inshta.Adapters.profileFollowerAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.bioModel;
import com.example.inshta.Models.editProfileModel;
import com.example.inshta.Models.profileFollowersModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivityProfileViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileView extends AppCompatActivity {

    ActivityProfileViewBinding binding;
    String useridd;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Inshta);
        binding = ActivityProfileViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // setting toolbar
        setSupportActionBar(binding.profileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.profileToolbar.setNavigationOnClickListener(view -> {

            onBackPressed();
            overridePendingTransition(0,0);

        });

        // getting data from previous activity through getIntent();
        Intent intent = getIntent();
        useridd = intent.getStringExtra("userid");


        // setting the data on user profile
        database.getReference()
                .child("Users")
                .child(useridd).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            Users users = snapshot.getValue(Users.class);
                            Glide.with(getApplicationContext())
                                    .load(users.getProfile())
                                    .placeholder(R.drawable.profile_placeholder)
                                    .into(binding.profileViewUserprofile);
                            Glide.with(getApplicationContext())
                                    .load(users.getCoverPhoto())
                                    .placeholder(R.drawable.cover_placeholder)
                                    .into(binding.uploadCoverImageView);
                            binding.profileUsername.setText(users.getName());

                            binding.profileFollowerCountTV.setText(users.getFollowerCount()+"");
                            if (users.getFollowerCount()>10){
                                binding.blueTick.setVisibility(View.VISIBLE);
                            }else {
                                binding.blueTick.setVisibility(View.INVISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        ///// Getting public profile details from database from example country profession etc.......
        database.getReference()
                .child("Users")
                .child(useridd)
                .child("profileInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){

                                editProfileModel profileModel = snapshot.getValue(editProfileModel.class);
                                binding.countryTv.setText(profileModel.getCountry());
                                binding.relationTv.setText(profileModel.getRelation());
                                binding.genderTv.setText(profileModel.getGender());
                                binding.professionTv.setText(profileModel.getProfession());
                                binding.birthdayTv.setText(profileModel.getBirthday());
                            }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // getting user profile bio

        database.getReference()
                .child("Users")
                .child(useridd)
                .child("bio").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            bioModel model = snapshot.getValue(bioModel.class);
                            binding.bioTv.setText(model.getBio());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        ArrayList<profileFollowersModel> listFollowers = new ArrayList<>();

        profileFollowerAdapter followerAdapter = new profileFollowerAdapter(listFollowers, getApplicationContext());
        binding.followersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.followersRecyclerView.setAdapter(followerAdapter);


        database.getReference().child("Users")
                .child(useridd)
                .child("followers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listFollowers.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                profileFollowersModel model = dataSnapshot.getValue(profileFollowersModel.class);
                                listFollowers.add(model);
                            }
                            followerAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}