package com.example.inshta.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.editProfileModel;
import com.example.inshta.Models.postModel;
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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class uploadFragment extends Fragment {


    public uploadFragment() {
        // Required empty public constructor
    }

    FragmentUploadBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri urlPostImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getActivity() != null){
                    Users users = snapshot.getValue(Users.class);
                    Glide.with(getContext())
                            .load(users.getProfile())
                            .placeholder(R.drawable.profile_placeholder)
                            .into(binding.postProfile);
                    binding.postUsername.setText(users.getName());
                //    binding.postProfesstion.setText(users.getProfession());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                        .child(auth.getUid())
                                .child("profileInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            editProfileModel profileModel = snapshot.getValue(editProfileModel.class);
                            binding.postProfesstion.setText(profileModel.getProfession());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                /// TextWatcher for Enable and Disable POST button
        binding.postDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = binding.postDescription.getText().toString().trim();

                if (!description.isEmpty()) {
                    binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.post_button_active));
                    binding.btnPost.setEnabled(true);
                    binding.btnPost.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_active_btn));
                    binding.btnPost.setEnabled(false);
                    binding.btnPost.setTextColor(Color.parseColor("#8DAAA5A5"));
                }
               // 8DAAA5A5
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /// Picking Post image from Gallery

        binding.postPickImage.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPostImage.launch(intent);

        });


        /// Uploading Post into database

        binding.btnPost.setOnClickListener(view -> {
            enableProgressBar();
            StorageReference reference = storage.getReference()
                    .child("posts").child(auth.getUid())
                    .child(new Date().getTime()+"");
                if (urlPostImage==null){
                    Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                    disableProgressBar();
                }else {

            reference.putFile(urlPostImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            postModel model = new postModel();
                            model.setPostImage(uri.toString());
                            model.setPostedBy(auth.getUid());
                            model.setPostAt(new Date().getTime());
                            model.setPostDescription(binding.postDescription.getText().toString().trim());

                            database.getReference().child("posts").push()
                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                            binding.postDescription.setText("");
                                            binding.postImageView.setVisibility(View.INVISIBLE);

                                            disableProgressBar();
                                        }
                                    });

                        }
                    });

                }
            });
                }

        });

        return binding.getRoot();
    }

    ActivityResultLauncher<Intent> pickPostImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {

        if (result.getResultCode()==RESULT_OK && result.getData()!=null){

             urlPostImage = result.getData().getData();

           try {

               InputStream inputStream = getContext().getContentResolver().openInputStream(urlPostImage);
               Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
               binding.postImageView.setImageBitmap(bitmap);
               binding.postImageView.setVisibility(View.VISIBLE);

           }catch (Exception e){
               e.printStackTrace();
           }
        }


    });

    private void enableProgressBar(){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnPost.setVisibility(View.INVISIBLE);
    }
    private void disableProgressBar(){
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.btnPost.setVisibility(View.VISIBLE);
    }
}