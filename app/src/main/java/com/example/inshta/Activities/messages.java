package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inshta.Adapters.messageAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.messageModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivityCommentsBinding;
import com.example.inshta.databinding.ActivityMessagesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class messages extends AppCompatActivity {

    ActivityMessagesBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String receiverId , senderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setSupportActionBar(binding.messageToolbar);
        getSupportActionBar().setTitle("");


        Intent intent = getIntent();
         receiverId = intent.getStringExtra("friendId");
        senderId = FirebaseAuth.getInstance().getUid();


        database.getReference()
                .child("Users")
                .child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                Users users = snapshot.getValue(Users.class);
                                Glide.with(getApplicationContext())
                                        .load(users.getProfile())
                                        .placeholder(R.drawable.profile_placeholder)
                                        .into(binding.msgProfilePic);
                                binding.msgUsername.setText(users.getName());
                            }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.msgProfilePic.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), chats.class));
            overridePendingTransition(0, 0);
        });
        binding.msgbackBtn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), chats.class));
            overridePendingTransition(0, 0);
        });


        // Storing messages into database
        binding.layoutSend.setOnClickListener(view -> {
            String msg = binding.typeMessage.getText().toString().trim();
            if (msg.isEmpty()){
                return;
            }
          final   messageModel model = new messageModel(senderId,msg);
            model.setTime(new Date().getTime());
            model.setReceiverid(receiverId);
            binding.typeMessage.setText("");


            database.getReference()
                    .child("chats")
                    .push()
                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            DatabaseReference reference = database.getReference()
                                    .child("chatList")
                                    .child(senderId)
                                    .child(receiverId);

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()){
                                        reference.child("id").setValue(receiverId);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    }
                    });


        });


        // Displaying messages
        ArrayList<messageModel> list = new ArrayList<>();
        messageAdapter adapter = new messageAdapter(list,this);
        binding.chatrecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.chatrecyclerView.setLayoutManager(layoutManager);

        database.getReference()
                .child("chats").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                list.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    messageModel modell = dataSnapshot.getValue(messageModel.class);
                                    if (modell.getuId().equals(senderId)&&modell.getReceiverid().equals(receiverId)||modell.getReceiverid().equals(senderId)&&modell.getuId().equals(receiverId)){

                                        list.add(modell);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteChat) {
            Toast.makeText(this, "Feature will added soon..", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}