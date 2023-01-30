package com.example.inshta.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.commentsModel;
import com.example.inshta.R;
import com.example.inshta.databinding.CommentsRvLayoutBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.viewHolder>{


    ArrayList<commentsModel> commentList;
    Context context;

    public commentAdapter(ArrayList<commentsModel> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        commentsModel model = commentList.get(position);

        String textTime = TimeAgo.using(model.getCommentedAt());

        // holder.binding.commentText.setText(model.getCommentText());
        holder.binding.commentTime.setText(textTime);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Glide.with(context)
                                .load(users.getProfile())
                                .placeholder(R.drawable.profile_placeholder)
                                .into(holder.binding.commentUserPic);
                        holder.binding.commentText.setText(Html.fromHtml( "<b>"+users.getName()+"</b>"+" " + model.getCommentText()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        CommentsRvLayoutBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentsRvLayoutBinding.bind(itemView);
        }
    }

}
