package com.example.go4lunch.ui.restaurant.list;

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

import com.example.go4lunch.databinding.RestaurantListFragmentBinding;
import com.example.go4lunch.ui.restaurant.detail.RestaurantDetailsActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantListFragment extends Fragment {

    private RestaurantListFragmentBinding binding;
    private RestaurantListViewModel viewModel;

    @NonNull
    public static RestaurantListFragment newInstance() {
        return new RestaurantListFragment();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RestaurantListFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RestaurantListViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.getRoot();

        RestaurantListAdapter adapter = new RestaurantListAdapter(restaurantId -> startActivity(RestaurantDetailsActivity.navigate(requireContext(), restaurantId))
        );

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getRestaurants()
            .observe(getViewLifecycleOwner(), adapter::submitList
            );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
