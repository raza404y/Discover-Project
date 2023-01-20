package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inshta.Adapters.profileFollowerAdapter;
import com.example.inshta.Models.profileFollowersModel;
import com.example.inshta.R;

import java.util.ArrayList;


public class profileFragment extends Fragment {


    public profileFragment() {
        // Required empty public constructor
    }

    RecyclerView followersRV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        followersRV = view.findViewById(R.id.followersRecyclerView);

        ArrayList<profileFollowersModel> listFollowers = new ArrayList<>();
        listFollowers.add(new profileFollowersModel(R.drawable.img_5, "Mark Zuckerberg"));
        listFollowers.add(new profileFollowersModel(R.drawable.img_6, "Elon Musk"));
        listFollowers.add(new profileFollowersModel(R.drawable.img_7, "Steve Jobs"));
        listFollowers.add(new profileFollowersModel(R.drawable.img_5, "Mark Zuckerberg"));
        listFollowers.add(new profileFollowersModel(R.drawable.img_6, "Elon Musk"));
        listFollowers.add(new profileFollowersModel(R.drawable.img_7, "Steve Jobs"));

        profileFollowerAdapter followerAdapter = new profileFollowerAdapter(listFollowers,getContext());
        followersRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        followersRV.setAdapter(followerAdapter);

        return view;
    }
}