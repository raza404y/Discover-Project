package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inshta.Adapters.chatUsersAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.R;
import com.example.inshta.databinding.FragmentAllChatUsersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class all_chat_users extends Fragment {


    public all_chat_users() {
        // Required empty public constructor
    }

    FragmentAllChatUsersBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Users> chatUsersList;
    chatUsersAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllChatUsersBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        /// getting all user from database and showing in all user fragment and setting on Recycler View

        chatUsersList = new ArrayList<>();
        adapter = new chatUsersAdapter(chatUsersList,getContext());
        binding.recyclerViewAllChatUsers.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerViewAllChatUsers.setLayoutManager(layoutManager);

        database.getReference()
                .child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            chatUsersList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Users users = dataSnapshot.getValue(Users.class);
                                users.setUserId(dataSnapshot.getKey());
                                if (!dataSnapshot.getKey().equals(auth.getUid())){
                                    chatUsersList.add(users);
                                }
                            }


                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.searchBar.clearFocus();
        binding.searchBar.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        return binding.getRoot();
    }
    private void filterList(String newText) {
        ArrayList<Users> fileterdlist = new ArrayList<>();
        for (Users item : chatUsersList){
            if (item.getName().toLowerCase().contains(newText.toLowerCase())){
                fileterdlist.add(item);
            }
        }
        if (fileterdlist.isEmpty()){
            Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setFilterdList(fileterdlist);
        }
    }
}