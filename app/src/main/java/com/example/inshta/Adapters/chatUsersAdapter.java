package com.example.inshta.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Activities.messages;
import com.example.inshta.Models.Users;
import com.example.inshta.R;
import com.example.inshta.databinding.ChatUsersRvLayoutBinding;

import java.util.ArrayList;

public class chatUsersAdapter extends RecyclerView.Adapter<chatUsersAdapter.viewHolder>{

    ArrayList<Users> chatUserList;
    Context context;

    public chatUsersAdapter(ArrayList<Users> chatUserList, Context context) {
        this.chatUserList = chatUserList;
        this.context = context;
    }
    public void setFilterdList(ArrayList<Users> filterdList){
        this.chatUserList = filterdList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_users_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users = chatUserList.get(position);
        Glide.with(context)
                .load(users.getProfile())
                .placeholder(R.drawable.profile_placeholder)
                .into(holder.binding.chatProfilePic);
        holder.binding.chatUsername.setText(users.getName());
       // holder.binding.chatProfession.setText(users.getProfession2());
        holder.binding.chatProfession.setText("Message");

        if (users.getFollowerCount()<10){
            holder.binding.blueTick.setVisibility(View.INVISIBLE);
            holder.binding.greenTick.setVisibility(View.INVISIBLE);
        }else if ((users.getFollowerCount()>=10 && users.getFollowerCount()<50)){
            holder.binding.greenTick.setVisibility(View.VISIBLE);
        }else {
            holder.binding.blueTick.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context.getApplicationContext(), messages.class);
                intent.putExtra("friendId",users.getUserId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ChatUsersRvLayoutBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatUsersRvLayoutBinding.bind(itemView);
        }
    }
}
