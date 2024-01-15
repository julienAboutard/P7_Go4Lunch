package com.example.go4lunch.domain.chosedrestaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.ChosenRestaurantEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;

import javax.inject.Inject;

public class AddUserRestaurantChoiceUseCase {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    @Inject
    public AddUserRestaurantChoiceUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase
    ) {
        this.userRepository = userRepository;
        this.getCurrentLoggedUserUseCase = getCurrentLoggedUserUseCase;
    }

    public void invoke(
        @Nullable String restaurantId,
        @Nullable String restaurantName,
        @Nullable String vicinity,
        @Nullable String pictureReferenceUrl
    ) {
        if (restaurantId != null &&
            restaurantName != null &&
            vicinity != null
        ) {
            userRepository.upsertUserRestaurantChoice(
                getCurrentLoggedUserUseCase.invoke(),
                new ChosenRestaurantEntity(
                    null,
                    restaurantId,
                    restaurantName,
                    vicinity,
                    pictureReferenceUrl
                )
            );
        }
    }
}
