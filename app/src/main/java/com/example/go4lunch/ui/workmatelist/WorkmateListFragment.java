package com.example.go4lunch.ui.workmatelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.databinding.WorkmateListFragmentBinding;
import com.example.go4lunch.ui.restaurant.detail.RestaurantDetailsActivity;
import com.google.android.gms.maps.SupportMapFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkmateListFragment extends Fragment {

    private WorkmateListFragmentBinding binding;

    private WorkmatesViewModel viewModel;

    @NonNull
    public static WorkmateListFragment newInstance() {
        return new WorkmateListFragment();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WorkmateListFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.getRoot();

        WorkmateListAdapter adapter = new WorkmateListAdapter(new OnWorkmateClickedListener() {

            @Override
            public void onWorkmateClicked(@NonNull String restaurantId) {
                startActivity(RestaurantDetailsActivity.navigate(requireContext(), restaurantId));
            }
        }
        );

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getWorkmates().observe(getViewLifecycleOwner(), workmates -> {
                adapter.submitList(workmates);
            }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
