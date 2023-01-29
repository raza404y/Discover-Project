package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.commentsModel;
import com.example.inshta.Models.postModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivityCommentsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class commentsActivity extends AppCompatActivity {

    ActivityCommentsBinding binding;

    String postedby;
    String postid;

    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.commentsToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Comments");
        binding.commentsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(0,0);
            }
        });

        Intent intent = getIntent();
        postid = intent.getStringExtra("postId");
        postedby = intent.getStringExtra("postedBy");

        database.getReference()
                .child("posts")
                .child(postid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        postModel post = snapshot.getValue(postModel.class);
                        Glide.with(getApplicationContext())
                                .load(post.getPostImage())
                                .placeholder(R.drawable.cover_placeholder)
                                .into(binding.postImage2);
                        binding.postDescription2.setText(post.getPostDescription());
                        binding.likesTV2.setText(post.getPostLike()+"");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Users")
                .child(postedby).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Glide.with(getApplicationContext())
                                .load(users.getProfile())
                                .placeholder(R.drawable.profile_placeholder)
                                .into(binding.profileImage2);
                        binding.postUsername2.setText(users.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.sendCommentBtn.setOnClickListener(view -> {

            commentsModel model = new commentsModel();
            model.setCommentText(binding.writeCcomment.getText().toString().trim());
            model.setCommentedAt(new Date().getTime());
            model.setCommentedBy(auth.getUid());

        });

    }
}