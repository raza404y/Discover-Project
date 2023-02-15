package com.example.inshta.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inshta.R;
import com.example.inshta.databinding.ActivityPersonalInformationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class personalInformation extends AppCompatActivity {

    ActivityPersonalInformationBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Inshta);
        binding = ActivityPersonalInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        binding.newNameET.requestFocus();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.updateToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.updateToolbar.setNavigationOnClickListener(view -> {

            onBackPressed();
            overridePendingTransition(0,0);

        });


            binding.saveNameBtn.setOnClickListener(view -> {

                String name = binding.newNameET.getText().toString().trim();
                if (name.isEmpty()){
                    showToast("enter new Name");
                }else if (name.length()<6){
                    showToast("Name is too short");
                }else {
                    database.getReference()
                            .child("Users")
                            .child(auth.getUid())
                            .child("name")
                            .setValue(name);
                    binding.newNameET.setText("");
                    showToast("Updated Successfully");
                }

            });


            binding.saveEmailBtn.setOnClickListener(view -> {

                String email = binding.newEmailET.getText().toString().trim();

                if (email.isEmpty()){
                    showToast("Enter new Email");
                }else if (!email.matches(emailPattern)){
                    showToast("Invalid email");
                }else {
                    database.getReference()
                            .child("Users")
                            .child(auth.getUid())
                            .child("email")
                            .setValue(email);
                    binding.newEmailET.setText("");
                    showToast("Updated Successfully");
                }

            });

            binding.savePasswordBtn.setOnClickListener(view -> {

                String password = binding.newPasswordEt.getText().toString().trim();

                if (password.isEmpty()){
                     showToast("Enter new passowrd");
                }else if (password.length()<6){
                    showToast("Password at least 6 characters");
                }else {
                    database.getReference()
                            .child("Users")
                            .child(auth.getUid())
                            .child("password")
                            .setValue(password);
                    binding.newPasswordEt.setText("");
                    showToast("Updated Successfully");
                }

            });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}