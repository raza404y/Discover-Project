package com.example.inshta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inshta.Models.Users;
import com.example.inshta.databinding.ActivityProfileView2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileView2 extends AppCompatActivity {

    ActivityProfileView2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileView2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String postid = intent.getStringExtra("postid");

        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(postid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            Users users = snapshot.getValue(Users.class);

//
//                            Glide.with(getApplicationContext())
//                                    .load(users.getProfile())
//                                    .placeholder(R.drawable.profile_placeholder)
//                                    .into(binding.imageView3);
                            binding.textView7.setText(users.getName());


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}