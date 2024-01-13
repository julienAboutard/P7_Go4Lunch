package com.example.go4lunch.domain.location;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.gps.location.GpsLocationRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StartLocationRequestUseCase {

    @NonNull
    private final GpsLocationRepository gpsLocationRepository;

    @Inject
    public StartLocationRequestUseCase(@NonNull GpsLocationRepository gpsLocationRepository) {
        this.gpsLocationRepository = gpsLocationRepository;
    }

    public void invoke() {
        gpsLocationRepository.startLocationRequest();
    }
}
