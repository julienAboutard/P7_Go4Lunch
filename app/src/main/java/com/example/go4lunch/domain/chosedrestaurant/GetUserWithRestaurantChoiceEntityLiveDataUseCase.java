package com.example.go4lunch.domain.chosedrestaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;

import javax.inject.Inject;

public class GetUserWithRestaurantChoiceEntityLiveDataUseCase {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final AuthRepository authRepository;

    @Inject
    public GetUserWithRestaurantChoiceEntityLiveDataUseCase(
        @NonNull UserRepository userRepository,
        @NonNull AuthRepository authRepository
    ) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }

    public LiveData<UserWithRestaurantChoiceEntity> invoke() {
        return Transformations.switchMap(authRepository.getLoggedUserLiveData(), user ->
            userRepository.getUserWithRestaurantChoiceEntity(user.getId())
        );
    }
}
