package com.example.go4lunch.data.firebaseauth;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseAuthRepository implements AuthRepository {

    private final FirebaseAuth firebaseAuth;

    private final MutableLiveData<Boolean> isUserLoggedMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<LoggedUserEntity> loggedUserMutableLiveData = new MutableLiveData<>();

    @Inject
    public FirebaseAuthRepository(@Nonnull FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;

        firebaseAuth.addAuthStateListener(
            firebaseAuth1 -> {
                boolean isUserLogged = firebaseAuth1.getCurrentUser() != null;
                isUserLoggedMutableLiveData.setValue(isUserLogged);

                if (firebaseAuth1.getCurrentUser() != null &&
                    firebaseAuth1.getCurrentUser().getDisplayName() != null) {
                    if (firebaseAuth1.getCurrentUser().getPhotoUrl() != null) {
                        loggedUserMutableLiveData.setValue(new LoggedUserEntity(
                                firebaseAuth1.getCurrentUser().getUid(),
                                firebaseAuth1.getCurrentUser().getDisplayName(),
                                firebaseAuth1.getCurrentUser().getEmail(),
                                firebaseAuth1.getCurrentUser().getPhotoUrl().toString()
                            )
                        );
                    } else {
                        loggedUserMutableLiveData.setValue(new LoggedUserEntity(
                                firebaseAuth1.getCurrentUser().getUid(),
                                firebaseAuth1.getCurrentUser().getDisplayName(),
                                firebaseAuth1.getCurrentUser().getEmail(),
                                null
                            )
                        );
                    }
                }
            }
        );
    }

    @Override
    public LoggedUserEntity getCurrentUser() {
        if (firebaseAuth.getCurrentUser() != null) {
            String id = firebaseAuth.getCurrentUser().getUid();
            String email = firebaseAuth.getCurrentUser().getEmail();
            String name = firebaseAuth.getCurrentUser().getDisplayName();
            String pictureUrl;

            if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
                pictureUrl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
            } else {
                pictureUrl = Uri.parse("android.resource://com.example.go4lunch/" + R.drawable.outline_person_24).toString();
            }
            if (name != null) {
                return new LoggedUserEntity(
                    id,
                    name,
                    email,
                    pictureUrl
                );
            }
        } else {
            Log.e("AuthRepository", "Error while getting current user");
        }
        return null;
    }

    @Nullable
    @Override
    public String getCurrentLoggedUserId() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().getUid();
        }
        return null;
    }

    @Override
    public LiveData<Boolean> isUserLoggedLiveData() {
        return isUserLoggedMutableLiveData;
    }

    @Override
    public LiveData<LoggedUserEntity> getLoggedUserLiveData() {
        return loggedUserMutableLiveData;
    }

    @Override
    public Task<AuthResult> logIn(String mail, String password) {
        return firebaseAuth.signInWithEmailAndPassword(mail, password);
    }

    @Override
    public Task<AuthResult> signUp(String mail, String password, String name) {
        return firebaseAuth.createUserWithEmailAndPassword(mail, password)
            .addOnSuccessListener(authResult -> {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
                if (authResult.getUser() != null) {
                    authResult.getUser().updateProfile(userProfileChangeRequest);
                }
            });
    }

    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }
}
