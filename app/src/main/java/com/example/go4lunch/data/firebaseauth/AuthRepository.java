package com.example.go4lunch.data.firebaseauth;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.annotation.Nullable;

public interface AuthRepository {

    @Nullable
    LoggedUserEntity getCurrentUser();

    LiveData<Boolean> isUserLoggedLiveData();

    @Nullable
    String getCurrentLoggedUserId();

    LiveData<LoggedUserEntity> getLoggedUserLiveData();

    Task<AuthResult> logIn(String mail, String password);

    Task<AuthResult> signUp(String mail, String password, String name);

    void logOut();
}
