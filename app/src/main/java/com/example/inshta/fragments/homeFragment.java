package com.example.inshta.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.inshta.Activities.notification_activity;
import com.example.inshta.Activities.upload_post;
import com.example.inshta.Adapters.postAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.postModel;
import com.example.inshta.R;
import com.example.inshta.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class homeFragment extends Fragment {


    public homeFragment() {
    }

    FragmentHomeBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding  = FragmentHomeBinding.inflate(inflater, container, false);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        binding.notification.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), notification_activity.class));
            getActivity().overridePendingTransition(0,0);
        });

        binding.uploadPostTv.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), upload_post.class));
            getActivity().overridePendingTransition(0,0);
        });

        binding.uploadPostPicView.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), upload_post.class));
            getActivity().overridePendingTransition(0,0);
        });

        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (getActivity() != null) {
                    Users users = snapshot.getValue(Users.class);
                    Glide.with(getContext())
                            .load(users.getProfile())
                            .placeholder(R.drawable.profile_placeholder)
                            .into(binding.homeProfileImage);
                    Glide.with(getContext())
                            .load(users.getProfile())
                            .placeholder(R.drawable.profile_placeholder)
                            .into(binding.prfilePicture);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


         // ########## Post Adapter ############### //

        ArrayList<postModel> postList = new ArrayList<>();

        postAdapter postadapter = new postAdapter(postList,getContext());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setReverseLayout(true);
        layoutManager1.setStackFromEnd(true);
        binding.postRecyclerView.setAdapter(postadapter);
        binding.postRecyclerView.setLayoutManager(layoutManager1);

        binding.progressBar.setVisibility(View.VISIBLE);
        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    postModel model = dataSnapshot.getValue(postModel.class);
                    model.setPostId(dataSnapshot.getKey());
                    postList.add(model);
                }
                postadapter.notifyDataSetChanged();
              binding.progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }

}