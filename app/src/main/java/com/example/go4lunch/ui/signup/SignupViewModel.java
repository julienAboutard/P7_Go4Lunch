package com.example.go4lunch.ui.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.domain.authentification.SignupUserUseCase;
import com.example.go4lunch.domain.user.AddLoggedUserEntityUseCase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignupViewModel extends ViewModel {

    @NonNull
    private final SignupUserUseCase signupUserUseCase;

    @NonNull
    private  AddLoggedUserEntityUseCase addLoggedUserEntityUseCase;

    @NonNull
    private FirebaseAuth firebaseAuth;

    private String signupMail;

    private String signupPassword;

    private String signupName;

    @Inject
    public SignupViewModel(
        @NonNull SignupUserUseCase signupUserUseCase,
        @NonNull AddLoggedUserEntityUseCase addLoggedUserEntityUseCase,
        @NonNull FirebaseAuth firebaseAuth
    ) {
        this.signupUserUseCase = signupUserUseCase;
        this.addLoggedUserEntityUseCase = addLoggedUserEntityUseCase;
        this.firebaseAuth = firebaseAuth;
    }

    public void onMailChanged(String mail) {
        signupMail = mail;
    }

    public void onPasswordChanged(String password) {
        signupPassword = password;
    }

    public void onNameChanged(String name) {
        signupName = name;
    }

    public String getSignupMail() {
        return signupMail;
    }

    public String getSignupPassword() {
        return signupPassword;
    }

    public String getSignupName() {
        return signupName;
    }

    public Task<AuthResult> onSignupButton() {
        return signupUserUseCase.invoke(signupMail, signupPassword);
    }

    public FirebaseUser getUser() {
       return firebaseAuth.getCurrentUser();
    }

    public void onLoginComplete() {
        addLoggedUserEntityUseCase.invoke();
    }

}
