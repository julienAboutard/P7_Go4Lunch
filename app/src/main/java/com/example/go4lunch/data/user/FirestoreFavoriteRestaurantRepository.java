package com.example.go4lunch.data.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreFavoriteRestaurantRepository implements FavoriteRestaurantRepository{

    private final static String USERS_COLLECTION = "users";
    private final static String COLLECTION_PATH_FAVORITE_RESTAURANTS = "favoriteRestaurantIds";

    @NonNull
    private final FirebaseFirestore firestore;

    @Inject
    public FirestoreFavoriteRestaurantRepository(@NonNull FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void addFavoriteRestaurant(
        @NonNull String userId,
        @NonNull String restaurantId
    ) {
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(COLLECTION_PATH_FAVORITE_RESTAURANTS)
            .document(restaurantId)
            .set(new HashMap<>());
    }

    @Override
    public void removeFavoriteRestaurant(
        @NonNull String userId,
        @NonNull String restaurantId
    ) {
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(COLLECTION_PATH_FAVORITE_RESTAURANTS)
            .document(restaurantId)
            .delete();
    }

    @Override
    @NonNull
    public LiveData<Set<String>> getUserFavoriteRestaurantIdsLiveData(@NonNull String userId) {
        return new FirestoreFavoriteRestaurantIdsLiveData(
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(COLLECTION_PATH_FAVORITE_RESTAURANTS)
        );
    }
}
