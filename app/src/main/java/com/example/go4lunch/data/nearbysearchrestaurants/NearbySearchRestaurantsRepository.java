package com.example.go4lunch.data.nearbysearchrestaurants;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsWrapper;

public interface NearbySearchRestaurantsRepository {

    LiveData<NearbySearchRestaurantsWrapper> getNearbyRestaurants(
        @NonNull Double latitude,
        @NonNull Double longitude,
        @NonNull String type,
        int radius
    );
}
