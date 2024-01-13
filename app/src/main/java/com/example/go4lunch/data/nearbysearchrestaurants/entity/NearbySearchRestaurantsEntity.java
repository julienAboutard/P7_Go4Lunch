package com.example.go4lunch.data.nearbysearchrestaurants.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.data.gps.entity.LocationEntity;

import java.util.Objects;

public class NearbySearchRestaurantsEntity {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String vicinity;

    @Nullable
    private final String photoReferenceUrl;

    @Nullable
    private final Float rating;

    @NonNull
    private final LocationEntity locationEntity;

    @NonNull
    private final Integer distance;

    @Nullable
    private final Boolean isOpen;

    public NearbySearchRestaurantsEntity(
        @NonNull String placeId,
        @NonNull String restaurantName,
        @NonNull String vicinity,
        @Nullable String photoReferenceUrl,
        @Nullable Float rating,
        @NonNull LocationEntity locationEntity,
        @NonNull Integer distance,
        @Nullable Boolean isOpen
    ) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.vicinity = vicinity;
        this.photoReferenceUrl = photoReferenceUrl;
        this.rating = rating;
        this.locationEntity = locationEntity;
        this.distance = distance;
        this.isOpen = isOpen;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    @NonNull
    public String getVicinity() {
        return vicinity;
    }

    @Nullable
    public String getPhotoReferenceUrl() {
        return photoReferenceUrl;
    }

    @Nullable
    public Float getRating() {
        return rating;
    }

    @NonNull
    public LocationEntity getLocationEntity() {
        return locationEntity;
    }

    @NonNull
    public Integer getDistance() {
        return distance;
    }

    @Nullable
    public Boolean getOpen() {
        return isOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NearbySearchRestaurantsEntity that = (NearbySearchRestaurantsEntity) o;
        return Objects.equals(placeId, that.placeId) && Objects.equals(restaurantName, that.restaurantName)
            && Objects.equals(vicinity, that.vicinity) && Objects.equals(photoReferenceUrl, that.photoReferenceUrl)
            && Objects.equals(rating, that.rating) && Objects.equals(locationEntity, that.locationEntity)
            && Objects.equals(distance, that.distance) && Objects.equals(isOpen, that.isOpen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, restaurantName, vicinity, photoReferenceUrl, rating, locationEntity, distance, isOpen);
    }

    @Override
    public String toString() {
        return "NearbySearchRestaurantsEntity{" +
            "placeId='" + placeId + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", vicinity='" + vicinity + '\'' +
            ", photoReferenceUrl='" + photoReferenceUrl + '\'' +
            ", rating=" + rating +
            ", locationEntity=" + locationEntity +
            ", distance=" + distance +
            ", isOpen=" + isOpen +
            '}';
    }
}
