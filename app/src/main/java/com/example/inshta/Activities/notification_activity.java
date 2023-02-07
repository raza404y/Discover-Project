package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.inshta.Adapters.notificationAdapter;
import com.example.inshta.Models.NotificationModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivityNotificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class notification_activity extends AppCompatActivity {

    ActivityNotificationBinding binding;
    RecyclerView notificationRV;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setSupportActionBar(binding.toolbarNotification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.toolbarNotification.setNavigationOnClickListener(view -> {

            onBackPressed();
            overridePendingTransition(0,0);

        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        notificationRV = findViewById(R.id.notificationRecyclerView);


        ArrayList<NotificationModel> listNotify = new ArrayList<>();

        notificationAdapter adapter = new notificationAdapter(listNotify, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
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
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}