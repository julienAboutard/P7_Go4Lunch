package com.example.go4lunch.domain.authentification;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.firebaseauth.AuthRepository;

import javax.inject.Inject;

public class IsUserLoggedInLiveDataUseCase {

    @NonNull
    private final AuthRepository authRepository;

    @Inject
    public IsUserLoggedInLiveDataUseCase(@NonNull AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Boolean> invoke() {
        return authRepository.isUserLoggedLiveData();
    }
}
