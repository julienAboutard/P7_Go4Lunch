package com.example.go4lunch.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public interface AuthRepository {

    @Nullable
    FirebaseUser getCurrentUser();

    Task<AuthResult> login(String mail, String password);

    void logOut();
}
