package com.example.go4lunch.domain.authentification;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

public class LoginUserUSeCase {

    @NonNull
    private final AuthRepository authRepository;

    @Inject
    public LoginUserUSeCase(@NonNull AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Task<AuthResult> invoke(@NonNull String mail, @NonNull String pwd) {
        return authRepository.logIn(mail, pwd);
    }
}
