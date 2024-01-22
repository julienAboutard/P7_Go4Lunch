package com.example.go4lunch.domain.autocomplete;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.autocomplete.PredictionsRepository;

import javax.inject.Inject;

public class ResetPredictionPlaceIdUseCase {

    @NonNull
    private final PredictionsRepository repository;

    @Inject
    public ResetPredictionPlaceIdUseCase(@NonNull PredictionsRepository repository) {
        this.repository = repository;
    }

    public void invoke() {
        repository.resetPredictionPlaceIdQuery();
    }
}
