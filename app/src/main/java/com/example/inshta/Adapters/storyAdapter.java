package com.example.inshta.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inshta.R;
import com.example.inshta.Models.storyModel;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
public class storyAdapter extends RecyclerView.Adapter<storyAdapter.viewHolder>{

    ArrayList<storyModel> list;
    Context context;

    public storyAdapter(ArrayList<storyModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stroy_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        storyModel model = list.get(position);
        holder.stroyImage.setImageResource(model.getStory());
        holder.storyProfileImage.setImageResource(model.getStoryProfileImage());

        if (position==0) {
            holder.stroyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  ((Activity) context).startActivityForResult(galleryIntent, PICK_IMAGE);


                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RoundedImageView stroyImage;
        CircleImageView storyProfileImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

           stroyImage = itemView.findViewById(R.id.storyImageView);
           storyProfileImage = itemView.findViewById(R.id.storyProfileImage);
        }
    }

}
