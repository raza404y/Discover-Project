package com.example.inshta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Models.messageModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ItemContainerReceiverBackgroundBinding;
import com.example.inshta.databinding.ItemContainerSentMessageBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter{

    ArrayList<messageModel> list ;
    Context context;

    public static final int SENDER_VIEWTYPE = 1;
    public static final int RECEIVER_VIEWTYPE = 2;

    public messageAdapter(ArrayList<messageModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType==SENDER_VIEWTYPE){
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_sent_message,parent,false);
        return new SenderViewHolder(view);
    }else {
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_receiver_background,parent,false);
        return new ReceiverViewHoler(view);
    }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEWTYPE;
        }else {
            return RECEIVER_VIEWTYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        messageModel model = list.get(position);
        if (holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).SenderMsg.setText(model.getMessage());
            String textTime = TimeAgo.using(model.getTime());
            ((SenderViewHolder)holder).stime.setText(textTime.toString());

        }
        else {
            ((ReceiverViewHoler)holder).ReceiverMsg.setText(model.getMessage());
            String textTime = TimeAgo.using(model.getTime());
            ((ReceiverViewHoler)holder).rtime.setText(textTime.toString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReceiverViewHoler extends RecyclerView.ViewHolder {

       TextView ReceiverMsg , rtime;

        public ReceiverViewHoler(@NonNull View itemView) {
            super(itemView);

            ReceiverMsg = itemView.findViewById(R.id.RecieverMessage);
        rtime = itemView.findViewById(R.id.rtextDateTime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView SenderMsg, stime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            SenderMsg = itemView.findViewById(R.id.SenderMessage);
            stime = itemView.findViewById(R.id.stextDateTime);
        }
    }
}
