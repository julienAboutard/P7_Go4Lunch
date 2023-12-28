package com.example.go4lunch.data.gps.entity;

import androidx.annotation.NonNull;

import java.util.Objects;

public class LocationEntity {

    @NonNull
    private final Double latitude;
    @NonNull
    private final Double longitude;

    public LocationEntity(@NonNull Double latitude, @NonNull Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public Double getLatitude() {
        return latitude;
    }

    @NonNull
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationEntity locationEntity = (LocationEntity) o;
        return Objects.equals(latitude, locationEntity.latitude) && Objects.equals(longitude, locationEntity.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "LocationEntity{" +
            "latitude=" + latitude +
            ", longitude=" + longitude +
            '}';
    }
}
