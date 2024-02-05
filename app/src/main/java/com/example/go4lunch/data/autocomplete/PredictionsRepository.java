package com.example.go4lunch.data.autocomplete;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.autocomplete.entity.PredictionEntityWrapper;

public interface PredictionsRepository {

    LiveData<PredictionEntityWrapper> getPredictionsLiveData(
        @NonNull String query,
        @NonNull Double latitude,
        @NonNull Double longitude,
        int radius,
        @NonNull String types
    );

    void savePredictionPlaceId(@NonNull String placeId);

    LiveData<String> getPredictionPlaceIdLiveData();

    void resetPredictionPlaceIdQuery();
}
