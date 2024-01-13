package com.example.go4lunch.domain.nearbysearchresaturants;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.nearbysearchrestaurants.NearbySearchRestaurantsRepository;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsWrapper;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;

import javax.inject.Inject;

public class GetNearbySearchRestaurantsWrapperUseCase {

    private static final int RADIUS = 1_000;
    private static final String TYPE = "restaurant";

    @NonNull
    private final NearbySearchRestaurantsRepository nearbySearchRestaurantsRepository;

    @NonNull
    private final GetCurrentLocationUseCase getCurrentLocationUseCase;

    @Inject
    public GetNearbySearchRestaurantsWrapperUseCase(
        @NonNull NearbySearchRestaurantsRepository nearbySearchRestaurantsRepository,
        @NonNull GetCurrentLocationUseCase getCurrentLocationUseCase
    ) {
        this.nearbySearchRestaurantsRepository = nearbySearchRestaurantsRepository;
        this.getCurrentLocationUseCase = getCurrentLocationUseCase;
    }

    public LiveData<NearbySearchRestaurantsWrapper> invoke() {
        LiveData<LocationEntityWrapper> locationStateLiveData = getCurrentLocationUseCase.invoke();
        return Transformations.switchMap(locationStateLiveData, locationEntityWrapper -> {
                if (locationEntityWrapper instanceof LocationEntityWrapper.GpsProviderEnabled) {
                    LocationEntity location = ((LocationEntityWrapper.GpsProviderEnabled) locationEntityWrapper).locationEntity;
                    return nearbySearchRestaurantsRepository.getNearbyRestaurants(
                        location.getLatitude(),
                        location.getLongitude(),
                        TYPE,
                        RADIUS
                    );
                } else if (locationEntityWrapper instanceof LocationEntityWrapper.GpsProviderDisabled) {
                    return new MutableLiveData<>(
                        new NearbySearchRestaurantsWrapper.RequestError(
                            new Exception("GpsProviderDisabled")
                        )
                    );
                } else return null;
            }
        );
    }
}
