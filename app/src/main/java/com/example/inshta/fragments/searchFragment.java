package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inshta.Adapters.userAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.R;
import com.example.inshta.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class searchFragment extends Fragment {


    public searchFragment() {
        // Required empty public constructor
    }

    FragmentSearchBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ArrayList<Users> userList = new ArrayList<>();

        userAdapter adapter = new userAdapter(userList,getContext());
        binding.followersRecyclerView.setAdapter(adapter);
        binding.followersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    userList.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



       return binding.getRoot();
    }
}