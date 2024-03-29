package com.example.go4lunch.ui.home.searchview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.databinding.PredictionItemBinding;


public class AutocompleteListAdapter extends ListAdapter<PredictionViewState, RecyclerView.ViewHolder> {

    @NonNull
    private final OnPredictionClickedListener listener;

    public AutocompleteListAdapter(@NonNull OnPredictionClickedListener listener) {
        super(new ListPredictionItemCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent,
        int viewType
    ) {
        return new PredictionViewHolder(
            PredictionItemBinding.inflate(
                LayoutInflater.from(
                    parent.getContext()),
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(
        @NonNull RecyclerView.ViewHolder holder,
        int position
    ) {
        ((PredictionViewHolder) holder).bind(getItem(position), listener);
    }

    public static class PredictionViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final PredictionItemBinding binding;

        public PredictionViewHolder(@NonNull PredictionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(
            @NonNull PredictionViewState predictionViewState,
            @NonNull OnPredictionClickedListener listener
        ) {
            binding.predictionItemRestaurantName.setText(predictionViewState.getRestaurantName());
            binding.predictionItemRestaurantName.setOnClickListener(v ->
                listener.onPredictionClicked(
                    predictionViewState.getPlaceId(),
                    predictionViewState.getRestaurantName()
                )
            );
        }
    }

    private static class ListPredictionItemCallback extends DiffUtil.ItemCallback<PredictionViewState> {

        @Override
        public boolean areItemsTheSame(
            @NonNull PredictionViewState oldItem,
            @NonNull PredictionViewState newItem
        ) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(
            @NonNull PredictionViewState oldItem,
            @NonNull PredictionViewState newItem
        ) {
            return oldItem.equals(newItem);
        }
    }
}
