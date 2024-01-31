package com.example.go4lunch.domain.autocomplete;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.go4lunch.data.autocomplete.PredictionsRepository;
import com.example.go4lunch.data.autocomplete.entity.PredictionEntityWrapper;
import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;

import javax.inject.Inject;

public class GetPredictionsWrapperUseCase {

    private static final int RADIUS = 1_000;
    private static final String TYPES = "restaurant";

    @NonNull
    private final PredictionsRepository predictionsRepository;

    @NonNull
    private final GetCurrentLocationUseCase getCurrentLocationUseCase;

    @Inject
    public GetPredictionsWrapperUseCase(
        @NonNull PredictionsRepository predictionsRepository,
        @NonNull GetCurrentLocationUseCase getCurrentLocationUseCase
    ) {
        this.predictionsRepository = predictionsRepository;
        this.getCurrentLocationUseCase = getCurrentLocationUseCase;
    }

    public LiveData<PredictionEntityWrapper> invoke(@NonNull String query) {
        LiveData<LocationEntityWrapper> locationEntityWrapperLiveData = getCurrentLocationUseCase.invoke();
        return Transformations.switchMap(locationEntityWrapperLiveData, locationEntityWrapper -> {
                if (locationEntityWrapper instanceof LocationEntityWrapper.GpsProviderEnabled) {
                    LocationEntity location = ((LocationEntityWrapper.GpsProviderEnabled) locationEntityWrapper).locationEntity;
                    return predictionsRepository.getPredictionsLiveData(
                        query,
                        location.getLatitude(),
                        location.getLongitude(),
                        RADIUS,
                        TYPES
                    );
                } else if (locationEntityWrapper instanceof LocationEntityWrapper.GpsProviderDisabled) {
                    return new MutableLiveData<>(
                        new PredictionEntityWrapper.RequestError(
                            new Exception("GpsProviderDisabled")
                        )
                    );
                } else {
                    return null;
                }
            }
        );
    }
}
