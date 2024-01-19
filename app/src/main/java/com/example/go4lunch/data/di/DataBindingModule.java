package com.example.go4lunch.data.di;

import com.example.go4lunch.data.detailsretaurant.DetailsRestaurantRepository;
import com.example.go4lunch.data.detailsretaurant.DetailsRestaurantRepositoryGooglePlaces;
import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.data.firebaseauth.FirebaseAuthRepository;
import com.example.go4lunch.data.gps.location.GpsLocationRepository;
import com.example.go4lunch.data.gps.location.GpsLocationRepositoryBroadcastReceiver;
import com.example.go4lunch.data.gps.permission.GpsPermissionRepository;
import com.example.go4lunch.data.gps.permission.GpsPermissionRepositoryImpl;
import com.example.go4lunch.data.nearbysearchrestaurants.NearbySearchRestaurantsRepository;
import com.example.go4lunch.data.nearbysearchrestaurants.NearbySearchRestaurantsRepositoryGooglePlaces;
import com.example.go4lunch.data.user.FavoriteRestaurantRepository;
import com.example.go4lunch.data.user.FirestoreFavoriteRestaurantRepository;
import com.example.go4lunch.data.user.FirestoreUserRepository;
import com.example.go4lunch.data.user.UserRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class DataBindingModule {

    @Binds
    @Singleton
    public abstract AuthRepository bindAuthRepository(FirebaseAuthRepository impl);

    @Binds
    @Singleton
    public abstract GpsLocationRepository bindGpsLocationRepository(
        GpsLocationRepositoryBroadcastReceiver gpsLocationRepositoryBroadcastReceiver
    );

    @Binds
    @Singleton
    public abstract GpsPermissionRepository bindsGpsPermissionRepository(GpsPermissionRepositoryImpl gpsPermissionRepositoryImpl);

    @Binds
    @Singleton
    public abstract NearbySearchRestaurantsRepository bindsNearbySearchRestaurantsRepository(
        NearbySearchRestaurantsRepositoryGooglePlaces nearbySearchRestaurantsRepositoryGooglePlaces
    );

    @Binds
    @Singleton
    public abstract UserRepository bindsUserRepository(FirestoreUserRepository firestoreUserRepository);

    @Binds
    @Singleton
    public abstract DetailsRestaurantRepository bindsDetailsRestaurantRepository(DetailsRestaurantRepositoryGooglePlaces detailsRestaurantRepositoryGooglePlaces);

    @Binds
    @Singleton
    public abstract FavoriteRestaurantRepository bindsFavoriteRestaurantRepository(
        FirestoreFavoriteRestaurantRepository firestoreFavoriteRestaurantRepository
    );
}
