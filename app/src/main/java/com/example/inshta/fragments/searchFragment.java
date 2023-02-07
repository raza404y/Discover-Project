package com.example.inshta.fragments;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.inshta.Adapters.userAdapter;
import com.example.inshta.Models.Users;
import com.example.inshta.R;
import com.example.inshta.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class searchFragment extends Fragment {


    public searchFragment() {
        // Required empty public constructor
    }

    FragmentSearchBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    userAdapter adapter;
    ArrayList<Users> userList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userList = new ArrayList<>();
        adapter = new userAdapter(userList, getContext());
        binding.followersRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.followersRecyclerView.setLayoutManager(layoutManager);
        binding.progressBar.setVisibility(View.VISIBLE);

        database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                if (getActivity() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Users users = dataSnapshot.getValue(Users.class);
                        users.setUserId(dataSnapshot.getKey());
                        users.getFollowerCount();
                        // if current user id not equals to firebase userId then it will hide our id
                        if (!dataSnapshot.getKey().equals(auth.getUid())) {
                            userList.add(users);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                binding.progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       // ((AppCompatActivity) getActivity()).setSupportActionBar(binding.searchToolbar123);
        binding.searchBar.clearFocus();
        binding.searchBar.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               filterList(newText);
                return true;
            }
        });

        return binding.getRoot();
    }

    private void filterList(String newText) {
        ArrayList<Users> fileterdlist = new ArrayList<>();
        for (Users item : userList){
            if (item.getName().toLowerCase().contains(newText.toLowerCase())){
                fileterdlist.add(item);
            }
        }
        if (fileterdlist.isEmpty()){
            Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setFilterdList(fileterdlist);
        }
    }
}