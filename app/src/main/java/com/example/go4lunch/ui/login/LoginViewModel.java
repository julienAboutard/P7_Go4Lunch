package com.example.go4lunch.ui.login;

import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.data.FirebaseAuthRepository;
import com.example.go4lunch.ui.utils.SingleLiveEvent;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final  FirebaseAuthRepository firebaseAuthRepository;
    private String loginMail;
    private String loginPassword;
    private final SingleLiveEvent<Integer> displayToastSingleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public LoginViewModel(
        FirebaseAuthRepository firebaseAuthRepository
    ) {
        this.firebaseAuthRepository = firebaseAuthRepository;
    }

    public void onMailChanged(String mail) {
        loginMail = mail;
    }

    public void onPasswordChanged(String password) {
        loginPassword = password;
    }

    public SingleLiveEvent<Integer> getDisplayToastSingleLiveEvent() {
        return displayToastSingleLiveEvent;
    }

    public Task<AuthResult> onLoginButton() {
        if (loginMail == null || loginPassword == null) {
            displayToastSingleLiveEvent.setValue(R.string.login_error_message);
            return null;
        } else {
            return firebaseAuthRepository.login(loginMail, loginPassword);
        }
    }
}
