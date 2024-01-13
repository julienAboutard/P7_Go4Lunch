package com.example.go4lunch.domain.location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.gps.location.GpsLocationRepository;

import javax.inject.Inject;

public class GetCurrentLocationUseCase {

    @NonNull
    private final GpsLocationRepository gpsLocationRepository;

    @Inject
    public GetCurrentLocationUseCase(@NonNull GpsLocationRepository gpsLocationRepository) {
        this.gpsLocationRepository = gpsLocationRepository;
    }

    public LiveData<LocationEntityWrapper> invoke() {
        return gpsLocationRepository.getLocationStateLiveData();
    }
}
