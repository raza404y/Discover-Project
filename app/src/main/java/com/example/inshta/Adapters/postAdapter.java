package com.example.inshta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inshta.Models.postModel;
import com.example.inshta.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class postAdapter extends RecyclerView.Adapter<postAdapter.viewHolder>{

    ArrayList<postModel> postList;
    Context context;

    public postAdapter(ArrayList<postModel> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        postModel model = postList.get(position);
        holder.postProfileImage.setImageResource(model.getProfileImage());
        holder.postImage.setImageResource(model.getPostImage());
        holder.postUsername.setText(model.getPostUsername());
        holder.postProfession.setText(model.getPostProfession());
        holder.likes.setText(model.getLikes());
        holder.comments.setText(model.getComments());
        holder.shares.setText(model.getShares());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView postProfileImage ;
        RoundedImageView postImage;
        TextView postUsername , postProfession , likes , comments , shares;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            postProfileImage = itemView.findViewById(R.id.postProfileImage);
            postImage = itemView.findViewById(R.id.postImage);
            postUsername = itemView.findViewById(R.id.postUserName);
            postProfession = itemView.findViewById(R.id.postProfession);
            likes = itemView.findViewById(R.id.likesTV);
            comments = itemView.findViewById(R.id.commentsTV);
            shares = itemView.findViewById(R.id.shareTV);

        }
    }
}
