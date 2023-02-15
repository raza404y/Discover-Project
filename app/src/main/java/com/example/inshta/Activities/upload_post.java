package com.example.inshta.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inshta.Models.Users;
import com.example.inshta.Models.editProfileModel;
import com.example.inshta.Models.postModel;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivityUploadPostBinding;
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
import java.util.Date;

public class upload_post extends AppCompatActivity {

    ActivityUploadPostBinding binding;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri urlPostImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Inshta);
        binding = ActivityUploadPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();


        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(upload_post.this,home.class));
            overridePendingTransition(0,0);
        });


        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Users users = snapshot.getValue(Users.class);
                    Glide.with(upload_post.this)
                            .load(users.getProfile())
                            .placeholder(R.drawable.profile_placeholder)
                            .into(binding.postProfile);
                    binding.postUsername.setText(users.getName());

                    if (users.getFollowerCount()<10){
                        binding.blueTick.setVisibility(View.INVISIBLE);
                        binding.greenTick.setVisibility(View.INVISIBLE);
                    }else if ((users.getFollowerCount()>=10 && users.getFollowerCount()<50)){
                        binding.greenTick.setVisibility(View.VISIBLE);
                    }else {
                        binding.blueTick.setVisibility(View.VISIBLE);
                    }
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
                    binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(upload_post.this, R.drawable.post_button_active));
                    binding.btnPost.setEnabled(true);
                    binding.btnPost.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(upload_post.this, R.drawable.follow_active_btn));
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
                Toast.makeText(upload_post.this, "Please select an image", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(upload_post.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                binding.postDescription.setText("");
                                                startActivity(new Intent(getApplicationContext(),home.class));
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


    }
            /// methods //////
    ActivityResultLauncher<Intent> pickPostImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode()==RESULT_OK && result.getData()!=null){

            urlPostImage = result.getData().getData();

            try {

                InputStream inputStream = getContentResolver().openInputStream(urlPostImage);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}