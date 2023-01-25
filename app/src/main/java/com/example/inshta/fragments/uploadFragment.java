package com.example.inshta.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inshta.R;
import com.example.inshta.databinding.FragmentUploadBinding;

import java.text.SimpleDateFormat;
import java.util.Date;


public class uploadFragment extends Fragment {


    public uploadFragment() {
        // Required empty public constructor
    }

    FragmentUploadBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false);


        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        String formatedTime = format.format(date);
        binding.timeDisplayTV.setText(formatedTime);

        return binding.getRoot();
    }
}