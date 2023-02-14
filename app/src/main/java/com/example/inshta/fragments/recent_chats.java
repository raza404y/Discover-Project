package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inshta.Adapters.chatUsersAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.chatListModel;
import com.example.inshta.R;
import com.example.inshta.databinding.FragmentRecentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class recent_chats extends Fragment {


    public recent_chats() {
        // Required empty public constructor
    }


    FragmentRecentChatsBinding binding;

    ArrayList<chatListModel> userlist;
    ArrayList<Users> mUsers;
    chatUsersAdapter adapter;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecentChatsBinding.inflate(inflater, container, false);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        userlist = new ArrayList<>();
        mUsers = new ArrayList<>();

//        if (mUsers.size()<0){
//            binding.imageView3.setVisibility(View.VISIBLE);
//            binding.recentChatTV.setVisibility(View.VISIBLE);
//        }else {
//            binding.imageView3.setVisibility(View.GONE);
//            binding.recentChatTV.setVisibility(View.GONE);
//        }



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chatList")
                .child(FirebaseAuth.getInstance().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userlist.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        chatListModel chatlist = dataSnapshot.getValue(chatListModel.class);
                        userlist.add(chatlist);
                    }
                    chatListings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }

    private void chatListings() {


            database.getReference()
                    .child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){
                                mUsers.clear();
                                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                    Users users = snapshot1.getValue(Users.class);
                                        users.setUserId(snapshot1.getKey());

                                    for (chatListModel chatList : userlist){

                                        Log.d("userId", "onDataChange: "+users.getName());
                                        if (users.getUserId().equals(chatList.getId())){
                                            mUsers.add(users);
                                        }else {
                                            binding.imageView3.setVisibility(View.INVISIBLE);
                                            binding.recentChatTV.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                }

                                chatUsersAdapter adapter = new chatUsersAdapter(mUsers,getContext());
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setReverseLayout(true);
                                layoutManager.setStackFromEnd(true);
                                binding.recyclerViewRecentChats.setLayoutManager(layoutManager);
                                binding.recyclerViewRecentChats.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
}