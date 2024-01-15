package com.example.go4lunch.domain.authentification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;

import javax.inject.Inject;

public class GetCurrentLoggedUserUseCase {

    @NonNull
    private final AuthRepository authRepository;

    @Inject
    public GetCurrentLoggedUserUseCase(@NonNull AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Nullable
    public LoggedUserEntity invoke() {
        return authRepository.getCurrentUser();
    }
}
