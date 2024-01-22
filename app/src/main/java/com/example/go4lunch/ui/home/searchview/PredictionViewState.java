package com.example.go4lunch.ui.home.searchview;

import androidx.annotation.NonNull;

import java.util.Objects;

public class PredictionViewState {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    public PredictionViewState(@NonNull String placeId, @NonNull String restaurantName) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredictionViewState that = (PredictionViewState) o;
        return Objects.equals(placeId, that.placeId) && Objects.equals(restaurantName, that.restaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, restaurantName);
    }

    @Override
    public String toString() {
        return "PredictionViewState{" +
            "placeId='" + placeId + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            '}';
    }
}
