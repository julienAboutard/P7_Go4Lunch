package com.example.go4lunch.data.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.entity.ChosenRestaurantEntity;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;

import java.util.List;

public interface UserRepository {

    void upsertLoggedUserEntity(@Nullable LoggedUserEntity userEntity);

    LiveData<List<LoggedUserEntity>> getLoggedUserEntitiesLiveData();


    void upsertUserRestaurantChoice(
        @Nullable LoggedUserEntity loggedUserEntity,
        @NonNull ChosenRestaurantEntity chosenRestaurantEntity
    );

    LiveData<List<UserWithRestaurantChoiceEntity>> getUsersWithRestaurantChoiceEntities();

    LiveData<UserWithRestaurantChoiceEntity> getUserWithRestaurantChoiceEntity(@NonNull String userId);

    void deleteUserRestaurantChoice(@Nullable LoggedUserEntity loggedUserEntity);

    List<UserWithRestaurantChoiceEntity> getUsersWithRestaurantChoiceEntitiesAsync();
}
