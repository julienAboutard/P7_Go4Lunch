package com.example.go4lunch.domain.autocomplete;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.autocomplete.PredictionsRepository;

import javax.inject.Inject;

public class ResetPredictionPlaceIdUseCase {

    @NonNull
    private final PredictionsRepository predictionsRepository;

    @Inject
    public ResetPredictionPlaceIdUseCase(@NonNull PredictionsRepository predictionsRepository) {
        this.predictionsRepository = predictionsRepository;
    }

    public void invoke() {
        predictionsRepository.resetPredictionPlaceIdQuery();
    }
}
