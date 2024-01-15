package com.example.go4lunch.ui.workmatelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.databinding.WormateListBinding;
import com.google.android.gms.maps.SupportMapFragment;

public class WorkmateListFragment extends SupportMapFragment {

    private WormateListBinding binding;

    @NonNull
    public static WorkmateListFragment newInstance() {
        return new WorkmateListFragment();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WormateListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
