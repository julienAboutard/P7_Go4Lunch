package com.example.go4lunch.ui.home.searchview;

import androidx.annotation.NonNull;

public interface OnPredictionClickedListener {

    void onPredictionClicked(
        @NonNull String placeId,
        @NonNull String restaurantName
    );
}
