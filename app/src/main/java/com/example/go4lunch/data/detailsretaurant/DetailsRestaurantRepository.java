package com.example.go4lunch.data.detailsretaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantWrapper;

public interface DetailsRestaurantRepository {

    LiveData<DetailsRestaurantWrapper> getRestaurantDetails(
        @NonNull String placeId
    );
}
