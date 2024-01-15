package com.example.go4lunch.domain.authentification;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.firebaseauth.AuthRepository;

import javax.inject.Inject;

public class GetCurrentLoggedUserIdUseCase {

    @NonNull
    private final AuthRepository authRepository;

    @Inject
    public GetCurrentLoggedUserIdUseCase(
        @NonNull AuthRepository authRepository
    ) {
        this.authRepository = authRepository;
    }

    @NonNull
    public String invoke() {
        String currentUserId = authRepository.getCurrentLoggedUserId();
        if (currentUserId != null) {
            return currentUserId;
        } else {
            throw new IllegalStateException("User is not logged in");
        }
    }
}
