package com.example.go4lunch.ui.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.gps.GpsLocationRepository;
import com.example.go4lunch.data.gps.entity.LocationStateEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends ViewModel {

    @NonNull
    private final GpsLocationRepository gpsLocationRepository;

    @Inject
    public MapViewModel(
        @NonNull GpsLocationRepository gpsLocationRepository
        ) {
            this.gpsLocationRepository = gpsLocationRepository;
    }

    public LiveData<LocationStateEntity> getLocationState() {
        return gpsLocationRepository.getLocationStateLiveData();
    }
}
