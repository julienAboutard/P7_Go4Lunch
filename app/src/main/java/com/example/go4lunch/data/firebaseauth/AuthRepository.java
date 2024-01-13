package com.example.go4lunch.data.firebaseauth;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public interface AuthRepository {

    @Nullable
    FirebaseUser getCurrentUser();

    LiveData<Boolean> isUserLoggedLiveData();

    Task<AuthResult> logIn(String mail, String password);

    Task<AuthResult> signUp(String mail, String password);

    void logOut();
}
