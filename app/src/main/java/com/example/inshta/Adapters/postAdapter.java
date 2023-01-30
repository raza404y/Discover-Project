package com.example.inshta.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Activities.commentsActivity;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.postModel;
import com.example.inshta.R;
import com.example.inshta.databinding.PostRvLayoutBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
                        Users users = snapshot.getValue(Users.class);
                        Glide.with(context)
                                .load(users.getProfile())
                                .placeholder(R.drawable.profile_placeholder)
                                .into(holder.binding.postProfileImage);
                        holder.binding.postUserName.setText(users.getName());
                        holder.binding.postProfession.setText(textTimePost+"");
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

                                                            }
                                                        });

                                            }
                                        });

                            });
                        }
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
