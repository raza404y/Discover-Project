package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inshta.Adapters.commentAdapter;
import com.example.inshta.Models.NotificationModel;
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

import java.util.ArrayList;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

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
                        if (snapshot.exists()) {
                            postModel post = snapshot.getValue(postModel.class);
                            Glide.with(getApplicationContext())
                                    .load(post.getPostImage())
                                    .placeholder(R.drawable.cover_placeholder)
                                    .into(binding.postImage2);
                            binding.postDescription2.setText(post.getPostDescription());
                            binding.likesTV2.setText(post.getPostLike() + "");
                            binding.commentsTV2.setText(post.getCommentCount() + "");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Users")
                .child(postedby).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Users users = snapshot.getValue(Users.class);
                            Glide.with(getApplicationContext())
                                    .load(users.getProfile())
                                    .placeholder(R.drawable.profile_placeholder)
                                    .into(binding.profileImage2);
                            binding.postUsername2.setText(users.getName());

                            if (users.getFollowerCount()<10){
                                binding.blueTick.setVisibility(View.INVISIBLE);
                                binding.greenTick.setVisibility(View.INVISIBLE);
                            }else if ((users.getFollowerCount()>=10 && users.getFollowerCount()<50)){
                                binding.greenTick.setVisibility(View.VISIBLE);
                            }else {
                                binding.blueTick.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.sendCommentBtn.setOnClickListener(view -> {
            String commentText = binding.writeCcomment.getText().toString().trim();
            commentsModel model = new commentsModel();
            if (commentText.isEmpty()){
                    return;
            }else {
                model.setCommentText(commentText);
            }
            model.setCommentedAt(new Date().getTime());
            model.setCommentedBy(auth.getUid());
            
            
            database.getReference()
                    .child("posts")
                    .child(postid)
                    .child("comments")
                    .push()
                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.getReference()
                                    .child("posts")
                                    .child(postid)
                                    .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int commentCount = 0;
                                            if (snapshot.exists()){
                                                commentCount = snapshot.getValue(Integer.class);
                                            }
                                            database.getReference()
                                                    .child("posts")
                                                    .child(postid)
                                                    .child("commentCount")
                                                    .setValue(commentCount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            binding.writeCcomment.setText("");
                                                            NotificationModel notificationModel = new NotificationModel();
                                                            notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                            notificationModel.setNotificationAt(new Date().getTime());
                                                            notificationModel.setPostId(postid);
                                                            notificationModel.setPostedBy(postedby);
                                                            notificationModel.setType("comment");

                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("notification")
                                                                    .child(postedby)
                                                                    .push()
                                                                    .setValue(notificationModel);
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    });

        });


        /// getting comments from database

        ArrayList<commentsModel> commentList = new ArrayList<>();

        commentAdapter adapter = new commentAdapter(commentList,getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.commentRV.setAdapter(adapter);
        binding.commentRV.setLayoutManager(layoutManager);
//        binding.commentRV.setNestedScrollingEnabled(false);
        database.getReference()
                .child("posts")
                .child(postid)
                .child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            commentsModel model = snapshot1.getValue(commentsModel.class);
                            commentList.add(model);
                        }
                        adapter.notifyDataSetChanged();
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