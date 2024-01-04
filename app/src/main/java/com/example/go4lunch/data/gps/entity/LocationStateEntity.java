package com.example.go4lunch.data.gps.entity;

import androidx.annotation.NonNull;

import java.util.Objects;

public abstract class LocationStateEntity {

    public static class GpsProviderEnabled extends LocationStateEntity {
        @NonNull
        public final LocationEntity locationEntity;

        public GpsProviderEnabled(@NonNull LocationEntity locationEntity) {
            this.locationEntity = locationEntity;
        }

        @NonNull
        public LocationEntity getLocationEntity() {
            return locationEntity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GpsProviderEnabled that = (GpsProviderEnabled) o;
            return Objects.equals(locationEntity, that.locationEntity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(locationEntity);
        }

        @Override
        public String toString() {
            return "GpsProviderEnabled{" +
                "locationEntity=" + locationEntity +
                '}';
        }
    }

    public static class GpsProviderDisabled extends LocationStateEntity {
    }
}
