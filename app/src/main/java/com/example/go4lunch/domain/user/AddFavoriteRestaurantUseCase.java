package com.example.go4lunch.domain.user;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.user.FavoriteRestaurantRepository;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;

import javax.inject.Inject;

public class AddFavoriteRestaurantUseCase {

    @NonNull
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;

    @NonNull
    private final GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    @Inject
    public AddFavoriteRestaurantUseCase(
        @NonNull FavoriteRestaurantRepository favoriteRestaurantRepository,
        @NonNull GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase
    ) {
        this.favoriteRestaurantRepository = favoriteRestaurantRepository;
        this.getCurrentLoggedUserIdUseCase = getCurrentLoggedUserIdUseCase;
    }

    public void invoke(@NonNull String restaurantId) {
        favoriteRestaurantRepository.addFavoriteRestaurant(
            getCurrentLoggedUserIdUseCase.invoke(),
            restaurantId
        );
    }
}
