package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inshta.Adapters.notificationAdapter;
import com.example.inshta.Models.NotificationModel;
import com.example.inshta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class bellFragment extends Fragment {


    public bellFragment() {
        // Required empty public constructor
    }

    RecyclerView notificationRV;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bell, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        notificationRV = view.findViewById(R.id.notificationRecyclerView);


        ArrayList<NotificationModel> listNotify = new ArrayList<>();

        notificationAdapter adapter = new notificationAdapter(listNotify, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        notificationRV.setAdapter(adapter);
        notificationRV.setLayoutManager(layoutManager);

        database.getReference()
                .child("notification")
                .child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listNotify.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                NotificationModel notification1 = snapshot1.getValue(NotificationModel.class);
                                notification1.setNotificationId(snapshot1.getKey());
                                listNotify.add(notification1);
                            }
                            adapter.notifyDataSetChanged();

                    }

                        @Override
                        public void onCancelled (@NonNull DatabaseError error){

                        }

                });

        return view;
    }
}