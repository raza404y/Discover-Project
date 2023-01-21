package com.example.inshta.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inshta.Models.NotificationModel;
import com.example.inshta.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.viewHolder>{

    ArrayList<NotificationModel> listNotification ;
    Context context;

    public notificationAdapter(ArrayList<NotificationModel> listNotification, Context context) {
        this.listNotification = listNotification;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationModel model = listNotification.get(position);
        holder.notificationProfilePic.setImageResource(model.getNotificationProfilePic());
        holder.notificationTxt.setText(Html.fromHtml(model.getNotificationText()));
        holder.time.setText(model.getTime());
    }

    @Override
    public int getItemCount() {
        return listNotification.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView notificationProfilePic;
        TextView notificationTxt , time;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            notificationProfilePic = itemView.findViewById(R.id.notificationProfilePic);
            notificationTxt = itemView.findViewById(R.id.notificatioText);
            time = itemView.findViewById(R.id.notificationTime);

        }
    }

}
