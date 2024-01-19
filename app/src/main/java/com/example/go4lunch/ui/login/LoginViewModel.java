package com.example.go4lunch.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.domain.user.AddLoggedUserEntityUseCase;
import com.example.go4lunch.ui.utils.Event;
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
    private final MutableLiveData<Event<Integer>> displayToastEvent = new MutableLiveData<>();

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

    public LiveData<Event<Integer>> getDisplayToastEvent() {
        return displayToastEvent;
    }

    public Task<AuthResult> onLoginButton() {
        if (loginMail == null || loginPassword == null) {
            displayToastEvent.setValue(new Event<>(R.string.login_error_message));
            return null;
        } else {
            return authRepository.logIn(loginMail, loginPassword);
        }
    }

    public void onLoginComplete() {
        addLoggedUserEntityUseCase.invoke();
    }
}
