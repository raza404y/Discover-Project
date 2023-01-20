package com.example.inshta.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inshta.Models.profileFollowersModel;
import com.example.inshta.R;
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
        profileFollowersModel followersModel = listFollowers.get(position);
        holder.followerPicture.setImageResource(followersModel.getFollowerPicture());
        holder.followerName.setText(followersModel.getFollowerName());
    }

    @Override
    public int getItemCount() {
        return listFollowers.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView followerPicture;
        TextView followerName;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            followerPicture = itemView.findViewById(R.id.followerPicture);
            followerName = itemView.findViewById(R.id.followerName);
        }
    }
}
