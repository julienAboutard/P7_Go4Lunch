package com.example.go4lunch.data.firebaseauth;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    boolean isUserLogged = firebaseAuth.getCurrentUser() != null;
                    isUserLoggedMutableLiveData.setValue(isUserLogged);

                    if (firebaseAuth.getCurrentUser() != null &&
                        firebaseAuth.getCurrentUser().getPhotoUrl() != null &&
                        firebaseAuth.getCurrentUser().getDisplayName() != null) {
                        loggedUserMutableLiveData.setValue(new LoggedUserEntity(
                                firebaseAuth.getCurrentUser().getUid(),
                                firebaseAuth.getCurrentUser().getDisplayName(),
                                firebaseAuth.getCurrentUser().getEmail(),
                                firebaseAuth.getCurrentUser().getPhotoUrl().toString()
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
    public Task<AuthResult> signUp(String mail, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(mail, password);
    }

    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }

}
