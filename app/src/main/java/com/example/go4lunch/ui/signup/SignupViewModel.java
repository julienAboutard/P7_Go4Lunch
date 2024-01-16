package com.example.go4lunch.ui.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.domain.authentification.SignupUserUseCase;
import com.example.go4lunch.domain.user.AddLoggedUserEntityUseCase;
import com.example.go4lunch.ui.utils.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignupViewModel extends ViewModel {

    @NonNull
    private final SignupUserUseCase signupUserUseCase;

    @NonNull
    private final AddLoggedUserEntityUseCase addLoggedUserEntityUseCase;

    private String signupMail;

    private String signupPassword;

    private String signupName;

    private final MutableLiveData<Event<Integer>> displayToastSingleLiveEvent = new MutableLiveData<>();

    @Inject
    public SignupViewModel(
        @NonNull SignupUserUseCase signupUserUseCase,
        @NonNull AddLoggedUserEntityUseCase addLoggedUserEntityUseCase
    ) {
        this.signupUserUseCase = signupUserUseCase;
        this.addLoggedUserEntityUseCase = addLoggedUserEntityUseCase;
    }

    public void onMailChanged(String mail) {
        signupMail = mail;
    }

    public void onPasswordChanged(String password) { signupPassword = password; }

    public void onNameChanged (String name) {
        signupName = name;
    }

    public LiveData<Event<Integer>> getDisplayToastSingleLiveEvent() {
        return displayToastSingleLiveEvent;
    }

    public Task<AuthResult> onSignupButton() {
        if (signupMail == null || signupPassword == null || signupName == null) {
            displayToastSingleLiveEvent.setValue(new Event<>(R.string.signup_error_message));
            return null;
        } else {
            return signupUserUseCase.invoke(signupMail, signupPassword, signupName);
        }
    }
    public void onLoginComplete() {
        addLoggedUserEntityUseCase.invoke();
    }

}
