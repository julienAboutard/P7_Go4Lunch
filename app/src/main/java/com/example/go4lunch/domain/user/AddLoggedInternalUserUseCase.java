package com.example.go4lunch.domain.user;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;

import javax.inject.Inject;

public class AddLoggedInternalUserUseCase {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    @Inject
    public AddLoggedInternalUserUseCase(@NonNull UserRepository userRepository, @NonNull GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase) {
        this.userRepository = userRepository;
        this.getCurrentLoggedUserIdUseCase = getCurrentLoggedUserIdUseCase;
    }

    public void invoke(@NonNull String mail, @NonNull String name) {
        userRepository.upsertLoggedUserEntity(
            new LoggedUserEntity(
                getCurrentLoggedUserIdUseCase.invoke(),
                name,
                mail,
                null
            )
        );
    }
}
