package com.example.inshta.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inshta.Adapters.storyAdapter;
import com.example.inshta.Models.story;
import com.example.inshta.Models.userStories;
import com.example.inshta.R;
import com.example.inshta.databinding.FragmentUploadBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;


public class uploadFragment extends Fragment {


    public uploadFragment() {
        // Required empty public constructor
    }

    FragmentUploadBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Uri storyUri;
    FirebaseStorage storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false);

        binding.pickStoryFromGallery.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickStory.launch(intent);

        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        ArrayList<story> list = new ArrayList<>();
        storyAdapter adapter = new storyAdapter(list,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.storyRecyclerView.setLayoutManager(layoutManager);
        binding.storyRecyclerView.setAdapter(adapter);

         enableProgressbar2();
         database.getReference()
                 .child("stories").addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                         if (snapshot.exists()){
                             for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                 story storyy = new story();
                                 storyy.setStoryBy(snapshot1.getKey());
                                 storyy.setStoryAt(snapshot1.child("postedBy").getValue(long.class));
                                ArrayList<userStories> stories = new ArrayList<>();

                                for (DataSnapshot snapshot2 : snapshot1.child("userStories").getChildren()){
                                    userStories userStoriess = snapshot2.getValue(userStories.class);
                                    stories.add(userStoriess);
                                }
                                storyy.setStories(stories);
                                list.add(storyy);
                             }
                             adapter.notifyDataSetChanged();
                             disableProgressBar2();
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });




        return binding.getRoot();
    }

    ActivityResultLauncher<Intent> pickStory = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {

        if (result.getResultCode()==RESULT_OK && result.getData() != null){

            storyUri = result.getData().getData();
          //  binding.pickStoryFromGallery.setImageURI(storyUri);


            StorageReference reference = storage.getReference()
                    .child("stories")
                    .child(auth.getUid())
                    .child(new Date().getTime()+"");

            enableProgressbar();
            reference.putFile(storyUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            story mstory = new story();
                            mstory.setStoryAt(new Date().getTime());

                            database.getReference()
                                    .child("stories")
                                    .child(auth.getUid())
                                    .child("postedBy")
                                    .setValue(mstory.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            userStories stories = new userStories(uri.toString(),mstory.getStoryAt());

                                            database.getReference()
                                                    .child("stories")
                                                    .child(auth.getUid())
                                                    .child("userStories")
                                                    .push()
                                                    .setValue(stories);
                                            disableProgressBar();
                                             //   binding.pickStoryFromGallery.setImageResource(R.drawable.create_story);
                                        }
                                    });
                        }
                    });

                }
            });
        }

    });

    private void enableProgressbar(){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.pickStoryFromGallery.setVisibility(View.INVISIBLE);
    }
    private void disableProgressBar(){
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.pickStoryFromGallery.setVisibility(View.VISIBLE);
    }

    private void enableProgressbar2(){
        binding.progressBar2.setVisibility(View.VISIBLE);
        binding.storyRecyclerView.setVisibility(View.INVISIBLE);
    }
    private void disableProgressBar2(){
        binding.progressBar2.setVisibility(View.INVISIBLE);
        binding.storyRecyclerView.setVisibility(View.VISIBLE);
    }
}