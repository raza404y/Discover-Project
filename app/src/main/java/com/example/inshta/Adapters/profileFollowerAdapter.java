package com.example.inshta.Adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Activities.view_follower_profile;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.profileFollowersModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ProfileFollowersRvLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;




public class profileFollowerAdapter extends RecyclerView.Adapter<profileFollowerAdapter.viewHolder>{

    ArrayList<profileFollowersModel> listFollowers;
    Context context;

    public profileFollowerAdapter(ArrayList<profileFollowersModel> listFollowers, Context context) {
        this.listFollowers = listFollowers;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_followers_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        profileFollowersModel model = listFollowers.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getFollowedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Users users = snapshot.getValue(Users.class);
                            Glide.with(context)
                                    .load(users.getProfile())
                                    .placeholder(R.drawable.profile_placeholder)
                                    .into(holder.binding.followerPicture);
                            holder.binding.followerName.setText(users.getName());


                            if (users.getFollowerCount()<10){
                                holder.binding.blueTick.setVisibility(View.INVISIBLE);
                                holder.binding.greenTick.setVisibility(View.INVISIBLE);
                            }else if ((users.getFollowerCount()>=10 && users.getFollowerCount()<50)){
                                holder.binding.greenTick.setVisibility(View.VISIBLE);
                            }else {
                                holder.binding.blueTick.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                        @Override
                        public void onCancelled (@NonNull DatabaseError error){

                        }

                });

        holder.binding.followerPicture.setOnClickListener(view -> {

            Intent intent = new Intent(context, view_follower_profile.class);
            intent.putExtra("followerId",model.getFollowedBy());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return listFollowers.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ProfileFollowersRvLayoutBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ProfileFollowersRvLayoutBinding.bind(itemView);


        }
    }

}
