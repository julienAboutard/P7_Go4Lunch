package com.example.go4lunch.ui.signup;

import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.data.FirebaseAuthRepository;
import com.example.go4lunch.ui.utils.SingleLiveEvent;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignupViewModel extends ViewModel {

    private final FirebaseAuthRepository firebaseAuthRepository;

    private String signupMail;

    private String signupPassword;

    private final SingleLiveEvent<Integer> displayToastSingleLiveEvent = new SingleLiveEvent<>();


    @Inject
    public SignupViewModel(FirebaseAuthRepository firebaseAuthRepository) {
        this.firebaseAuthRepository = firebaseAuthRepository;
    }

    public void onMailChanged(String mail) {
        signupMail = mail;
    }

    public void onPasswordChanged(String password) { signupPassword = password; }

    public SingleLiveEvent<Integer> getDisplayToastSingleLiveEvent() {
        return displayToastSingleLiveEvent;
    }

    public Task<AuthResult> onSignupButton() {
        if (signupMail == null || signupPassword == null) {
            displayToastSingleLiveEvent.setValue(R.string.login_error_message);
            return null;
        } else {
            return firebaseAuthRepository.signUp(signupMail, signupPassword);
        }
    }
}
