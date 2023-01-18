package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inshta.R;
import com.example.inshta.storyAdapter;
import com.example.inshta.storyModel;

import java.util.ArrayList;


public class homeFragment extends Fragment {



    public homeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView storyRv = view.findViewById(R.id.storyRecyclerView);

        ArrayList<storyModel> list = new ArrayList<>();
                list.add(new storyModel(R.drawable.profile_image));
                list.add(new storyModel(R.drawable.profile_image));
                list.add(new storyModel(R.drawable.profile_image));
                list.add(new storyModel(R.drawable.profile_image));
                list.add(new storyModel(R.drawable.profile_image));
                list.add(new storyModel(R.drawable.profile_image));

        storyAdapter adapter = new storyAdapter(list,getContext());
        storyRv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRv.setLayoutManager(layoutManager);
        storyRv.setNestedScrollingEnabled(true);

        return view;
    }
}