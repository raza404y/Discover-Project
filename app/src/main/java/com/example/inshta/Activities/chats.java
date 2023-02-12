package com.example.inshta.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.inshta.Models.Users;
import com.example.inshta.R;
import com.example.inshta.databinding.ActivityChatsBinding;
import com.example.inshta.fragments.all_chat_users;
import com.example.inshta.fragments.recent_chats;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class chats extends AppCompatActivity {

    ActivityChatsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    private int[] tabIcons = {
            R.drawable.ic_messenger,
            R.drawable.ic_people2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        // setting user profile image in chat Activity
        database.getReference()
                .child("Users")
                .child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Users users = snapshot.getValue(Users.class);
                            Glide.with(chats.this)
                                    .load(users.getProfile())
                                    .placeholder(R.drawable.profile_placeholder)
                                    .into(binding.chatProfilepic);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),home.class));
            overridePendingTransition(0,0);
        });

        ///// Setting tabLayout

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tablayout.setupWithViewPager(binding.viewPager);
        setupTabIcons();


        binding.tablayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager){

            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tab_color);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                super.onTabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.black);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
    }


    ///################ outside of class methods //#################3



    // setting tab icons
    private void setupTabIcons() {
        binding.tablayout.getTabAt(0).setIcon(tabIcons[0]);
        binding.tablayout.getTabAt(1).setIcon(tabIcons[1]);
    }

             /// ViewPager Adapter Class /////////

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new recent_chats();
                case 1:
                    return new all_chat_users();
                default:
                    return new recent_chats();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}