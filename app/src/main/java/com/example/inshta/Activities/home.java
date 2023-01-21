package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.inshta.R;
import com.example.inshta.databinding.ActivityHomeBinding;
import com.example.inshta.fragments.bellFragment;
import com.example.inshta.fragments.homeFragment;
import com.example.inshta.fragments.profileFragment;
import com.example.inshta.fragments.searchFragment;
import com.example.inshta.fragments.uploadFragment;

import io.ak1.OnBubbleClickListener;

public class home extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setSupportActionBar(binding.toolbar);
        home.this.setTitle("My Profile");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new homeFragment());
        fragmentTransaction.commit();
        binding.toolbar.setVisibility(View.GONE);

        binding.bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case R.id.home:
                        transaction.replace(R.id.container, new homeFragment());
                        binding.toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.bell:
                        transaction.replace(R.id.container, new bellFragment());
                        binding.toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.upload:
                        transaction.replace(R.id.container, new uploadFragment());
                        binding.toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.search:
                        transaction.replace(R.id.container, new searchFragment());
                        binding.toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.profile:
                        transaction.replace(R.id.container, new profileFragment());
                        binding.toolbar.setVisibility(View.VISIBLE);
                        break;
                }
              
 transaction.commit();
            }
             
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logoutAction){
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}