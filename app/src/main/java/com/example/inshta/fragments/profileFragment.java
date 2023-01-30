package com.example.inshta.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inshta.Adapters.postAdapter;
import com.example.inshta.Adapters.profileFollowerAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.postModel;
import com.example.inshta.Models.profileFollowersModel;
import com.example.inshta.R;
import com.example.inshta.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class profileFragment extends Fragment {


    public profileFragment() {
        // Required empty public constructor
    }

    FragmentProfileBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ArrayList<profileFollowersModel> listFollowers = new ArrayList<>();

        profileFollowerAdapter followerAdapter = new profileFollowerAdapter(listFollowers, getContext());
        binding.followersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.followersRecyclerView.setAdapter(followerAdapter);


        database.getReference().child("Users")
                .child(auth.getUid())
                .child("followers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listFollowers.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            profileFollowersModel model = dataSnapshot.getValue(profileFollowersModel.class);
                            listFollowers.add(model);
                        }
                        followerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        binding.uploadCoverImageView.setOnClickListener(view -> openGallery());
        binding.uploadCoverImage.setOnClickListener(view -> openGallery());

        binding.profileImageUpload.setOnClickListener(view -> openGalleryForProfile());
        binding.profilePicture.setOnClickListener(view -> openGalleryForProfile());


        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);
                    Glide.with(getActivity())
                            .load(users.getCoverPhoto())
                            .placeholder(R.drawable.cover_placeholder)
                            .into(binding.uploadCoverImageView);
                    Glide.with(getActivity())
                                    .load(users.getProfile())
                                            .placeholder(R.drawable.profile_placeholder)
                                                    .into(binding.profilePicture);
                    binding.profileUsername.setText(users.getName());
                    binding.profileFollowerCountTV.setText(users.getFollowerCount()+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayList<postModel> mypostList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.myPostsRecyclerView.setLayoutManager(layoutManager);
        postAdapter adapter = new postAdapter(mypostList,getContext());
        binding.myPostsRecyclerView.setAdapter(adapter);


        database.getReference()
                .child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            postModel model = snapshot1.getValue(postModel.class);
                            model.getPostedBy();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return binding.getRoot();
    }


    ActivityResultLauncher<Intent> pickCoverImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

            Uri url = result.getData().getData();

            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(url);
                Bitmap bitmapUrl = BitmapFactory.decodeStream(inputStream);
                binding.uploadCoverImageView.setImageBitmap(bitmapUrl);

                StorageReference reference = storage.getReference().child("cover_photo").child(auth.getUid());
                reference.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Cover uploaded", Toast.LENGTH_SHORT).show();

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                DatabaseReference reference1 = database.getReference().child("Users").child(auth.getUid()).child("coverPhoto");
                                reference1.setValue(uri.toString());

                            }
                        });

                    }
                });


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    });

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickCoverImage.launch(intent);
    }

    private void openGalleryForProfile() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickProfileImage.launch(intent);
    }
    ActivityResultLauncher<Intent> pickProfileImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {

        if (result.getResultCode()==RESULT_OK && result.getData()!=null){
            Uri url2 = result.getData().getData();

            try {
                InputStream stream = getContext().getContentResolver().openInputStream(url2);
                Bitmap bitmap2 = BitmapFactory.decodeStream(stream);
                binding.profilePicture.setImageBitmap(bitmap2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            StorageReference reference11 = storage.getReference().child("profile").child(auth.getUid());
                reference11.putFile(url2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Profile image Uploaded", Toast.LENGTH_SHORT).show();
                        reference11.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri2) {

                                DatabaseReference reference12 = database.getReference().child("Users").child(auth.getUid()).child("profile");
                                reference12.setValue(uri2.toString());

                            }
                        });

                    }
                });

        }
    });
}