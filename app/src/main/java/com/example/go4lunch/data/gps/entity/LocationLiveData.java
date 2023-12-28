package com.example.go4lunch.data.gps.entity;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationLiveData extends LiveData<LocationEntity> {

    private FusedLocationProviderClient fusedLocationProviderClient;

    public LocationLiveData(Context context) {
        
    }
    @Override
    protected void onActive() {
        super.onActive();

    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }
}
