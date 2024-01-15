package com.example.go4lunch.domain.chosedrestaurant;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;

import javax.inject.Inject;

public class RemoveUserRestaurantChoiceUseCase {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    @Inject
    public RemoveUserRestaurantChoiceUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase
    ) {
        this.userRepository = userRepository;
        this.getCurrentLoggedUserUseCase = getCurrentLoggedUserUseCase;
    }

    public void invoke() {
        userRepository.deleteUserRestaurantChoice(
            getCurrentLoggedUserUseCase.invoke()
        );
    }
}
