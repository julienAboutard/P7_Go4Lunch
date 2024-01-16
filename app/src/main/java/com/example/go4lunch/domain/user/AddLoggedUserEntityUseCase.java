package com.example.go4lunch.domain.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;

import javax.inject.Inject;

public class AddLoggedUserEntityUseCase {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    @Inject
    public AddLoggedUserEntityUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase
    ) {
        this.userRepository = userRepository;
        this.getCurrentLoggedUserUseCase = getCurrentLoggedUserUseCase;
    }

    public void invoke() {
        LoggedUserEntity loggedUserEntity = getCurrentLoggedUserUseCase.invoke();
        if (loggedUserEntity != null) {
            userRepository.upsertLoggedUserEntity(
                new LoggedUserEntity(
                    loggedUserEntity.getId(),
                    loggedUserEntity.getName(),
                    loggedUserEntity.getEmail(),
                    loggedUserEntity.getPictureUrl()
                )
            );
        } else {
            Log.e("UpsertLoggedUser", "Error while getting current user");
        }
    }
}
