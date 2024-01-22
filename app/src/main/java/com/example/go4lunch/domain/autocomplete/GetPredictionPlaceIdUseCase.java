package com.example.go4lunch.domain.autocomplete;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.autocomplete.PredictionsRepository;

import javax.inject.Inject;

public class GetPredictionPlaceIdUseCase {

    @NonNull
    private final PredictionsRepository repository;

    @Inject
    public GetPredictionPlaceIdUseCase(@NonNull PredictionsRepository repository) {
        this.repository = repository;
    }

    public LiveData<String> invoke() {
        return repository.getPredictionPlaceIdLiveData();
    }
}
