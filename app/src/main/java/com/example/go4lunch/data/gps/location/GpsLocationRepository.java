package com.example.go4lunch.data.gps.location;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.gps.entity.LocationStateEntity;

public interface GpsLocationRepository {

    LiveData<LocationStateEntity> getLocationStateLiveData();

    void startLocationRequest();

    void stopLocationRequest();
}
