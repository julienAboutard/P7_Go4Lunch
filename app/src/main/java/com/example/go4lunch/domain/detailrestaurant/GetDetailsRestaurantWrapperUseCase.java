package com.example.go4lunch.domain.detailrestaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.detailsretaurant.DetailsRestaurantRepository;
import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantWrapper;

import javax.inject.Inject;

public class GetDetailsRestaurantWrapperUseCase {

    @NonNull
    private final DetailsRestaurantRepository detailsRestaurantRepository;

    @Inject
    public GetDetailsRestaurantWrapperUseCase(@NonNull DetailsRestaurantRepository detailsRestaurantRepository) {
        this.detailsRestaurantRepository = detailsRestaurantRepository;
    }

    public LiveData<DetailsRestaurantWrapper> invoke(@NonNull String restaurantId) {
        return detailsRestaurantRepository.getRestaurantDetails(restaurantId);
    }
}
