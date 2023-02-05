package com.example.inshta.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Activities.commentsActivity;
import com.example.inshta.Models.NotificationModel;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.postModel;
import com.example.inshta.R;
import com.example.inshta.databinding.PostRvLayoutBinding;
import com.example.inshta.profileView2;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class postAdapter extends RecyclerView.Adapter<postAdapter.viewHolder> {

    ArrayList<postModel> postList;
    Context context;

    public postAdapter(ArrayList<postModel> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_rv_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        postModel model = postList.get(position);
        String textTimePost = TimeAgo.using(model.getPostAt());
        Glide.with(context)
                .load(model.getPostImage())
                .placeholder(R.drawable.cover_placeholder)
                .into(holder.binding.postImage);
        holder.binding.postDescriptionHome.setText(model.getPostDescription());
        holder.binding.likesTV.setText(model.getPostLike() + "");
        holder.binding.commentsTV.setText(model.getCommentCount()+"");
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()) {
                           Users users = snapshot.getValue(Users.class);
                           Glide.with(context)
                                   .load(users.getProfile())
                                   .placeholder(R.drawable.profile_placeholder)
                                   .into(holder.binding.postProfileImage);
                           holder.binding.postUserName.setText(users.getName());
                           holder.binding.postProfession.setText(textTimePost + "");

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
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            holder.binding.likesTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filled_heart, 0, 0, 0);

                        } else {
                            holder.binding.likesTV.setOnClickListener(view -> {

                                FirebaseDatabase.getInstance().getReference()
                                        .child("posts")
                                        .child(model.getPostId())
                                        .child("likes")
                                        .child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("posts")
                                                        .child(model.getPostId())
                                                        .child("postLike")
                                                        .setValue(model.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                holder.binding.likesTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filled_heart, 0, 0, 0);

                                                                NotificationModel notification = new NotificationModel();
                                                                notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                notification.setNotificationAt(new Date().getTime());
                                                                notification.setPostId(model.getPostId());
                                                                notification.setPostedBy(model.getPostedBy());
                                                                notification.setType("like");

                                                                FirebaseDatabase.getInstance().getReference()
                                                                        .child("notification")
                                                                        .child(model.getPostedBy())
                                                                        .push()
                                                                        .setValue(notification);
                                                            }
                                                        });

                                            }
                                        });

                            });
                        }

                        holder.binding.postProfileImage.setOnClickListener(view -> {

                            Intent intent = new Intent(context.getApplicationContext(), profileView2.class);
                            intent.putExtra("postid",model.getPostId());
                            context.startActivity(intent);

                        });

                        holder.binding.commentsTV.setOnClickListener(view -> {

                            Intent intent = new Intent(context.getApplicationContext(), commentsActivity.class);
                            intent.putExtra("postId",model.getPostId());
                            intent.putExtra("postedBy",model.getPostedBy());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.binding.shareTV.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,holder.binding.postDescriptionHome.getText().toString());
            Intent.createChooser(intent,"sharing");
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        PostRvLayoutBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = PostRvLayoutBinding.bind(itemView);


        }
    }
}
