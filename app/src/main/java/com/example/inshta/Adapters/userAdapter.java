package com.example.inshta.Adapters;

import static androidx.core.content.ContextCompat.getDrawable;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Activities.profileView;
import com.example.inshta.Models.NotificationModel;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.editProfileModel;
import com.example.inshta.Models.profileFollowersModel;
import com.example.inshta.R;
import com.example.inshta.databinding.FollowersRvLayoutBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.viewHolder> {


    ArrayList<Users> usersList;
    Context context;

    public userAdapter(ArrayList<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;

    }

    public void setFilterdList(ArrayList<Users> filterdList){
        this.usersList = filterdList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.followers_rv_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Users users = usersList.get(position);
        Glide.with(context)
                .load(users.getProfile())
                .placeholder(R.drawable.profile_placeholder)
                .into(holder.binding.followerProfilePic);
        holder.binding.followerUsername.setText(users.getName());
        holder.binding.followerProfession.setText(users.getProfession2());

        if (users.getFollowerCount()<10){
            holder.binding.blueTick.setVisibility(View.INVISIBLE);
            holder.binding.greenTick.setVisibility(View.INVISIBLE);
        }else if ((users.getFollowerCount()>=10 && users.getFollowerCount()<50)){
            holder.binding.greenTick.setVisibility(View.VISIBLE);
        }else {
            holder.binding.blueTick.setVisibility(View.VISIBLE);
        }

                // uploading user data into database whenever he/she will click on follow button

        holder.binding.followBtn.setOnClickListener(view -> {

            profileFollowersModel follow = new profileFollowersModel();
            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
            follow.setFollowerdAt(new Date().getTime());
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(users.getUserId())
                    .child("followers")
                    .child(FirebaseAuth.getInstance().getUid())
                    .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(users.getUserId())
                                    .child("followerCount")
                                    .setValue(users.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            holder.binding.followBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                                            holder.binding.followBtn.setText("following");
                                            holder.binding.followBtn.setEnabled(false);
                                            Toast.makeText(context, "You followed " + users.getName(), Toast.LENGTH_SHORT).show();

                                            NotificationModel model = new NotificationModel();
                                            model.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            model.setNotificationAt(new Date().getTime());
                                            model.setType("follow");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(users.getUserId())
                                                    .push()
                                                    .setValue(model);
                                        }
                                    });


                        }
                    });

        });

//       getting all users (followers from database)
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(users.getUserId())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            holder.binding.followBtn.setBackgroundDrawable(getDrawable(context, R.drawable.follow_active_btn));
                            holder.binding.followBtn.setText("following");
                            holder.binding.followBtn.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        /// When some will click on user profile image a new activity will open and will show the user whole profile

        holder.binding.followerProfilePic.setOnClickListener(view -> {

           Intent intent = new Intent(context.getApplicationContext(),profileView.class);
           intent.putExtra("userid",users.getUserId());
           context.startActivity(intent);

        });

        holder.binding.followerUsername.setOnClickListener(view -> {

            Intent intent = new Intent(context.getApplicationContext(),profileView.class);
            intent.putExtra("userid",users.getUserId());
            context.startActivity(intent);

        });
        holder.binding.followerProfession.setOnClickListener(view -> {

            Intent intent = new Intent(context.getApplicationContext(),profileView.class);
            intent.putExtra("userid",users.getUserId());
            context.startActivity(intent);

        });

    }



    @Override
    public int getItemCount() {
        return usersList.size();
    }



    public class viewHolder extends RecyclerView.ViewHolder {
        FollowersRvLayoutBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FollowersRvLayoutBinding.bind(itemView);
        }

    }
}
