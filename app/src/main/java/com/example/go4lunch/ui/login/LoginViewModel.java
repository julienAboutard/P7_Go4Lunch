package com.example.go4lunch.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.domain.user.AddLoggedUserEntityUseCase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    @NonNull
    private final AuthRepository authRepository;
    private String loginMail;
    private String loginPassword;

    @NonNull
    private final AddLoggedUserEntityUseCase addLoggedUserEntityUseCase;

    @Inject
    public LoginViewModel(
        @NonNull AuthRepository authRepository,
        @NonNull AddLoggedUserEntityUseCase addLoggedUserEntityUseCase
    ) {
        this.authRepository = authRepository;
        this.addLoggedUserEntityUseCase = addLoggedUserEntityUseCase;
    }

    public void onMailChanged(String mail) {
        loginMail = mail;
    }

    public void onPasswordChanged(String password) {
        loginPassword = password;
    }

    public String getLoginMail() {
        return loginMail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public Task<AuthResult> onLoginButton() {
        return authRepository.logIn(loginMail, loginPassword);
    }

    public void onLoginComplete() {
        addLoggedUserEntityUseCase.invoke();
    }
}
