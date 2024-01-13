package com.example.go4lunch.data.gps.location;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;

public interface GpsLocationRepository {

    LiveData<LocationEntityWrapper> getLocationStateLiveData();

    void startLocationRequest();

    void stopLocationRequest();
}
