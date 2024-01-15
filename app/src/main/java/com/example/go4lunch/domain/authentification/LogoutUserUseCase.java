package com.example.go4lunch.domain.authentification;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.firebaseauth.AuthRepository;

import javax.inject.Inject;

public class LogoutUserUseCase {

    @NonNull
    private final AuthRepository authRepository;

    @Inject
    public LogoutUserUseCase(@NonNull AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void invoke() {
        authRepository.logOut();
    }
}
