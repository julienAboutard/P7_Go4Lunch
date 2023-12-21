package com.example.go4lunch.data;

import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public interface AuthRepository {

    @Nullable
    FirebaseUser getCurrentUser();


    void logOut();
}
