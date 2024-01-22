package com.example.go4lunch.data.autocomplete;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.autocomplete.entity.PredictionEntity;
import com.example.go4lunch.data.autocomplete.entity.PredictionEntityWrapper;

import java.util.List;

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
