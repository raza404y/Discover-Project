package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inshta.Adapters.notificationAdapter;
import com.example.inshta.Models.NotificationModel;
import com.example.inshta.R;

import java.util.ArrayList;


public class bellFragment extends Fragment {


    public bellFragment() {
        // Required empty public constructor
    }

    RecyclerView notificationRV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bell, container, false);


        notificationRV = view.findViewById(R.id.notificationRecyclerView);


        ArrayList<NotificationModel> listNotify = new ArrayList<>();

        listNotify.add(new NotificationModel(R.drawable.img_6,"<b>Elon Musk</b> mentioned you in a comment.","just Now"));
        listNotify.add(new NotificationModel(R.drawable.img_5,"<b>Mark Zuckerberg</b> liked your picture.","5 minutes ago"));
        listNotify.add(new NotificationModel(R.drawable.img_7,"<b>Steve Jobs</b> commented on your picture.","1 hours ago"));
        listNotify.add(new NotificationModel(R.drawable.img_6,"<b>Elon Musk</b> mentioned you in a comment.","3 hours ago"));
        listNotify.add(new NotificationModel(R.drawable.img_5,"<b>Mark Zuckerberg</b> liked your picture.","8 hours ago"));
        listNotify.add(new NotificationModel(R.drawable.img_7,"<b>Steve Jobs</b> mentioned you in a comment.","11 hours ago"));
        listNotify.add(new NotificationModel(R.drawable.img_6,"<b>Elon Musk</b> mentioned you in a comment.","1 day ago"));
        listNotify.add(new NotificationModel(R.drawable.img_5,"<b>Mark Zuckerberg</b> liked your picture.","5 days Ago"));
        listNotify.add(new NotificationModel(R.drawable.img_7,"<b>Steve Jobs</b> mentioned you in a comment.","7 days ago"));
        listNotify.add(new NotificationModel(R.drawable.img_6,"<b>Elon Musk</b> mentioned you in a comment.","1 mon ago"));
        listNotify.add(new NotificationModel(R.drawable.img_5,"<b>Mark Zuckerberg</b> liked your picture.","2 mons ago"));

        notificationAdapter  adapter = new notificationAdapter(listNotify,getContext());
        notificationRV.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRV.setAdapter(adapter);

        return view;
    }
}