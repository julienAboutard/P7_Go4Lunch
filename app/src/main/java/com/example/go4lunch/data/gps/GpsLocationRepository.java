package com.example.go4lunch.data.gps;

import com.example.go4lunch.data.gps.entity.LocationEntity;

public class GpsLocationRepository{

    private LocationEntity locationEntity;

    public GpsLocationRepository(LocationEntity locationEntity) {
        this.locationEntity = locationEntity;
    }

}
