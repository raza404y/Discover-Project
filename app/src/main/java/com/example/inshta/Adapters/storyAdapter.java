package com.example.inshta.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.userStories;
import com.example.inshta.R;
import com.example.inshta.Models.story;
import com.example.inshta.databinding.StroyRvLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;
import omari.hamza.storyview.utils.StoryViewHeaderInfo;

public class storyAdapter extends RecyclerView.Adapter<storyAdapter.viewHolder>{

    ArrayList<story> list;
    Context context;

    public storyAdapter(ArrayList<story> list, Context context) {
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
        story story = list.get(position);

        if (story.getStories().size() > 0) {
            userStories laststory = story.getStories().get(story.getStories().size() - 1);
            Glide.with(context)
                    .load(laststory.getImage())
                    .placeholder(R.drawable.cover_placeholder)
                    .into(holder.binding.storyImageView);
            holder.binding.circularStatusView.setPortionsCount(story.getStories().size());
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Users users = snapshot.getValue(Users.class);
                            Glide.with(context)
                                    .load(users.getProfile())
                                    .placeholder(R.drawable.profile_placeholder)
                                    .into(holder.binding.storyProfileImage);
                            holder.binding.storyName.setText(users.getName());


                            holder.binding.storyImageView.setOnClickListener(view -> {

                                ArrayList<MyStory> myStories = new ArrayList<>();

                                for (userStories stories : story.getStories()) {
                                    myStories.add(new MyStory(
                                            stories.getImage()
                                    ));
                                }

                                new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                        .setStoriesList(myStories) // Required
                                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                        .setTitleText(users.getName()) // Default is Hidden
                                        .setSubtitleText("") // Default is Hidden
                                        .setTitleLogoUrl(users.getProfile()) // Default is Hidden
                                        .setStoryClickListeners(new StoryClickListeners() {
                                            @Override
                                            public void onDescriptionClickListener(int position) {
                                                //your action
                                            }

                                            @Override
                                            public void onTitleIconClickListener(int position) {
                                                //your action
                                            }
                                        }) // Optional Listeners
                                        .build() // Must be called before calling show method
                                        .show();

                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        StroyRvLayoutBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = StroyRvLayoutBinding.bind(itemView);

        }
    }

}
