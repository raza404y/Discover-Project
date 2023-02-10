package com.example.inshta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inshta.Adapters.profileFollowerAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.bioModel;
import com.example.inshta.Models.editProfileModel;
import com.example.inshta.Models.profileFollowersModel;
import com.example.inshta.databinding.ActivityProfileView2Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileView2 extends AppCompatActivity {

    ActivityProfileView2Binding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileView2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        setSupportActionBar(binding.profileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.profileToolbar.setNavigationOnClickListener(view -> {

            onBackPressed();
            overridePendingTransition(0,0);

        });

        Intent intent = getIntent();
        String postby = intent.getStringExtra("postedBy");

      //  Toast.makeText(this, "id "+postby, Toast.LENGTH_SHORT).show();

       database.getReference()
                .child("Users")
                .child(postby).addValueEventListener(new ValueEventListener() {
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
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        ///// Getting public profile details from database from example country profession etc.......

        database.getReference()
                .child("Users")
                .child(postby)
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


        // Setting user bio

        database.getReference()
                .child("Users")
                .child(postby)
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


        // getting user followers

        ArrayList<profileFollowersModel> listFollowers = new ArrayList<>();

        profileFollowerAdapter followerAdapter = new profileFollowerAdapter(listFollowers, getApplicationContext());
        binding.followersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.followersRecyclerView.setAdapter(followerAdapter);


        database.getReference().child("Users")
                .child(postby)
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
}