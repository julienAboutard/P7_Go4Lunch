package com.example.go4lunch.ui.map;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.gps.entity.LocationStateEntity;
import com.example.go4lunch.data.gps.location.GpsLocationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends ViewModel {

    @NonNull
    private final GpsLocationRepository gpsLocationRepository;

    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    private final MediatorLiveData<String> gpsMessageLiveData = new MediatorLiveData<>();

    @Inject
    public MapViewModel(
        @NonNull GpsLocationRepository gpsLocationRepository
        ) {
            this.gpsLocationRepository = gpsLocationRepository;
    }


    public LiveData<LocationStateEntity> getLocationState() {
        return gpsLocationRepository.getLocationStateLiveData();
    }

    public LiveData<String> getGpsMessageLiveData() {
        return gpsMessageLiveData;
    }

}
