package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.inshta.Adapters.postAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.postModel;
import com.example.inshta.R;
import com.example.inshta.Adapters.storyAdapter;
import com.example.inshta.Models.storyModel;
import com.example.inshta.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class homeFragment extends Fragment {



    public homeFragment() {
    }

    FragmentHomeBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding  = FragmentHomeBinding.inflate(inflater, container, false);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (getActivity() != null) {
                    Users users = snapshot.getValue(Users.class);
                    Glide.with(getContext())
                            .load(users.getProfile())
                            .placeholder(R.drawable.profile_placeholder)
                            .into(binding.homeProfileImage);
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ArrayList<storyModel> list = new ArrayList<>();
                list.add(new storyModel(R.drawable.img,R.drawable.img_6));
                list.add(new storyModel(R.drawable.img_1,R.drawable.profile_image));
                list.add(new storyModel(R.drawable.img_2,R.drawable.img_7));
                list.add(new storyModel(R.drawable.img_3,R.drawable.img_5));
                list.add(new storyModel(R.drawable.img_4,R.drawable.profile_image));


        storyAdapter adapter = new storyAdapter(list,getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.storyRecyclerView.setLayoutManager(layoutManager);
        binding.storyRecyclerView.setNestedScrollingEnabled(false);
         binding.storyRecyclerView.setAdapter(adapter);




         // ########## Post Adapter ############### //

        ArrayList<postModel> postList = new ArrayList<>();
        postList.add(new postModel(R.drawable.img_5,R.drawable.img,"Bill Gates","Web developer","277","129","4"));
        postList.add(new postModel(R.drawable.img_6,R.drawable.img_1,"Elon Musk","Traveller","347","249","11"));
        postList.add(new postModel(R.drawable.img_7,R.drawable.img_2,"Steve Jobs","Software developer","122","12","2"));
        postList.add(new postModel(R.drawable.img_5,R.drawable.img,"Mark Zukerberg","Creator","542","199","21"));

        postAdapter postadapter = new postAdapter(postList,getContext());
        binding.postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.postRecyclerView.setAdapter(postadapter);
        binding.postRecyclerView.setNestedScrollingEnabled(true);



        return binding.getRoot();
    }
}