package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inshta.Adapters.postAdapter;
import com.example.inshta.Models.postModel;
import com.example.inshta.R;
import com.example.inshta.Adapters.storyAdapter;
import com.example.inshta.Models.storyModel;

import java.util.ArrayList;


public class homeFragment extends Fragment {



    public homeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView storyRv = view.findViewById(R.id.storyRecyclerView);
        RecyclerView postRV = view.findViewById(R.id.postRecyclerView);


        ArrayList<storyModel> list = new ArrayList<>();
                list.add(new storyModel(R.drawable.img,R.drawable.img_6));
                list.add(new storyModel(R.drawable.img_1,R.drawable.profile_image));
                list.add(new storyModel(R.drawable.img_2,R.drawable.img_7));
                list.add(new storyModel(R.drawable.img_3,R.drawable.img_5));
                list.add(new storyModel(R.drawable.img_4,R.drawable.profile_image));


        storyAdapter adapter = new storyAdapter(list,getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRv.setLayoutManager(layoutManager);
        storyRv.setNestedScrollingEnabled(false);
         storyRv.setAdapter(adapter);




         // ########## Post Adapter ############### //

        ArrayList<postModel> postList = new ArrayList<>();
        postList.add(new postModel(R.drawable.img_5,R.drawable.img,"Bill Gates","Web developer","277","129","4"));
        postList.add(new postModel(R.drawable.img_6,R.drawable.img_1,"Elon Musk","Traveller","347","249","11"));
        postList.add(new postModel(R.drawable.img_7,R.drawable.img_2,"Steve Jobs","Software developer","122","12","2"));
        postList.add(new postModel(R.drawable.img_5,R.drawable.img,"Mark Zukerberg","Creator","542","199","21"));

        postAdapter postadapter = new postAdapter(postList,getContext());
        postRV.setLayoutManager(new LinearLayoutManager(getContext()));
        postRV.setAdapter(postadapter);
        postRV.setNestedScrollingEnabled(true);



        return view;
    }
}