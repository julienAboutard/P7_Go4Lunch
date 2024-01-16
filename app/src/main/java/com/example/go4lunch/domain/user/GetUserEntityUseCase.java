package com.example.go4lunch.domain.user;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.entity.UserEntity;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.domain.authentification.IsUserLoggedInLiveDataUseCase;
import com.example.go4lunch.domain.chosedrestaurant.GetUserWithRestaurantChoiceEntityLiveDataUseCase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class GetUserEntityUseCase {

    @NonNull
    private final MediatorLiveData<UserEntity> userEntityMediatorLiveData;

    @Inject
    public GetUserEntityUseCase(
        @NonNull GetFavoriteRestaurantsIdsUseCase getFavoriteRestaurantsIdsUseCase,
        @NonNull GetUserWithRestaurantChoiceEntityLiveDataUseCase getUserWithRestaurantChoiceEntityLiveDataUseCase,
        @NonNull IsUserLoggedInLiveDataUseCase isUserLoggedInLiveDataUseCase,
        @NonNull AuthRepository authRepository
    ) {
        userEntityMediatorLiveData = new MediatorLiveData<>();

        LiveData<Set<String>> favoriteRestaurantsIdsLiveData = getFavoriteRestaurantsIdsUseCase.invoke();
        LiveData<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntityLiveData = getUserWithRestaurantChoiceEntityLiveDataUseCase.invoke();
        LiveData<Boolean> isUserLoggedInLiveData = isUserLoggedInLiveDataUseCase.invoke();
        LiveData<LoggedUserEntity> loggedUserEntityLiveData = authRepository.getLoggedUserLiveData();
        userEntityMediatorLiveData.addSource(isUserLoggedInLiveData, isUserLoggedIn -> {
                combine(
                    isUserLoggedIn,
                    favoriteRestaurantsIdsLiveData.getValue(),
                    userWithRestaurantChoiceEntityLiveData.getValue(),
                    loggedUserEntityLiveData.getValue()
                );
            }
        );

        userEntityMediatorLiveData.addSource(favoriteRestaurantsIdsLiveData, favoriteRestaurantIds -> {
                combine(
                    isUserLoggedInLiveData.getValue(),
                    favoriteRestaurantIds,
                    userWithRestaurantChoiceEntityLiveData.getValue(),
                    loggedUserEntityLiveData.getValue()
                );
            }
        );

        userEntityMediatorLiveData.addSource(userWithRestaurantChoiceEntityLiveData, userWithRestaurantChoice -> {
                combine(
                    isUserLoggedInLiveData.getValue(),
                    favoriteRestaurantsIdsLiveData.getValue(),
                    userWithRestaurantChoice,
                    loggedUserEntityLiveData.getValue()
                );
            }
        );

        userEntityMediatorLiveData.addSource(loggedUserEntityLiveData, currentUser -> {
                combine(
                    isUserLoggedInLiveData.getValue(),
                    favoriteRestaurantsIdsLiveData.getValue(),
                    userWithRestaurantChoiceEntityLiveData.getValue(),
                    currentUser
                );
            }
        );
    }

    public LiveData<UserEntity> invoke() {
        return userEntityMediatorLiveData;
    }

    private void combine(
        @Nullable Boolean isUserLoggedIn,
        @Nullable Set<String> favoriteRestaurantsIds,
        @Nullable UserWithRestaurantChoiceEntity userWithRestaurantChoice,
        @Nullable LoggedUserEntity currentUser
    ) {
        if (isUserLoggedIn == null || !isUserLoggedIn || currentUser == null) {
            return;
        }

        Set<String> updatedFavoriteRestaurantsIds = new HashSet<>(favoriteRestaurantsIds != null ? favoriteRestaurantsIds : Collections.emptySet());

        userEntityMediatorLiveData.setValue(
            new UserEntity(
                currentUser,
                updatedFavoriteRestaurantsIds,
                userWithRestaurantChoice != null ? userWithRestaurantChoice.getAttendingRestaurantId() : null
            )
        );
    }
}
