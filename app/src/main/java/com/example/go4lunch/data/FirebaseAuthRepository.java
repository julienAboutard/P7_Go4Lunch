package com.example.go4lunch.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseAuthRepository implements AuthRepository {

    private final FirebaseAuth firebaseAuth;

    @Inject
    public FirebaseAuthRepository(@Nonnull FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }
}
