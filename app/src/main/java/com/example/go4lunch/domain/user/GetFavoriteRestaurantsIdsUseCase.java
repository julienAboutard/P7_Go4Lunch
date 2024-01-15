package com.example.go4lunch.domain.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.user.FavoriteRestaurantRepository;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;

import java.util.Set;

import javax.inject.Inject;

public class GetFavoriteRestaurantsIdsUseCase {

    @NonNull
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;

    @NonNull
    private final GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    @Inject
    public GetFavoriteRestaurantsIdsUseCase(
        @NonNull FavoriteRestaurantRepository favoriteRestaurantRepository,
        @NonNull GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase
    ) {
        this.favoriteRestaurantRepository = favoriteRestaurantRepository;
        this.getCurrentLoggedUserIdUseCase = getCurrentLoggedUserIdUseCase;
    }

    public LiveData<Set<String>> invoke() {
        return favoriteRestaurantRepository.getUserFavoriteRestaurantIdsLiveData(getCurrentLoggedUserIdUseCase.invoke());
    }
}
