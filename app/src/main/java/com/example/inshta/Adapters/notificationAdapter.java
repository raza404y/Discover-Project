package com.example.inshta.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Activities.commentsActivity;
import com.example.inshta.Models.NotificationModel;
import com.example.inshta.Models.Users;
import com.example.inshta.R;
import com.example.inshta.databinding.NotificationRvLayoutBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.viewHolder> {

    ArrayList<NotificationModel> listNotification;
    Context context;

    public notificationAdapter(ArrayList<NotificationModel> listNotification, Context context) {
        this.listNotification = listNotification;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_rv_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationModel notification = listNotification.get(position);

        String notificationTime = TimeAgo.using(notification.getNotificationAt());

        String type = notification.getType();
        if (type == null) {
            Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(notification.getNotificationBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Users users = snapshot.getValue(Users.class);
                                Glide.with(context)
                                        .load(users.getProfile())
                                        .placeholder(R.drawable.profile_placeholder)
                                        .into(holder.binding.notificationProfilePic);
                                holder.binding.notificationTime.setText(notificationTime);

                                if (users.getFollowerCount()<10){
                                    holder.binding.blueTick.setVisibility(View.INVISIBLE);
                                    holder.binding.greenTick.setVisibility(View.INVISIBLE);
                                }else if ((users.getFollowerCount()>=10 && users.getFollowerCount()<50)){
                                    holder.binding.greenTick.setVisibility(View.VISIBLE);
                                }else {
                                    holder.binding.blueTick.setVisibility(View.VISIBLE);
                                }


                                if (type.equals("like")) {
                                    holder.binding.notificatioText.setText(Html.fromHtml("<b>" + users.getName() + "</b> " + "liked your post."));
                                } else if (type.equals("comment")) {
                                    holder.binding.notificatioText.setText(Html.fromHtml("<b>" + users.getName() + "</b> " + "commented on your post."));
                                } else {
                                    holder.binding.notificatioText.setText(Html.fromHtml("<b>" + users.getName() + "</b> " + "start following you."));

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            holder.binding.parentNotification.setOnClickListener(view -> {
                if (!type.equals("follow")) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("notification")
                            .child(notification.getPostedBy())
                            .child(notification.getNotificationId())
                                    .child("checkOpen").setValue(true);
                    holder.binding.parentNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent = new Intent(context, commentsActivity.class);
                    intent.putExtra("postId", notification.getPostId());
                    intent.putExtra("postedBy", notification.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        boolean checkOpen = notification.isCheckOpen();
        if (checkOpen == true){
            holder.binding.parentNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return listNotification.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        NotificationRvLayoutBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = NotificationRvLayoutBinding.bind(itemView);

        }
    }

}
