package com.example.go4lunch.ui.map;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsEntity;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsWrapper;
import com.example.go4lunch.domain.gps.IsGpsEnabledUseCase;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;
import com.example.go4lunch.domain.nearbysearchresaturants.GetNearbySearchRestaurantsWrapperUseCase;
import com.example.go4lunch.ui.map.marker.RestaurantMarkerViewStateItem;
import com.example.go4lunch.ui.utils.Event;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends ViewModel {

    @NonNull
    private final GetCurrentLocationUseCase getCurrentLocationUseCase;

    private final MutableLiveData<Event<Integer>> noRestaurantMatchEvent = new MutableLiveData<>();

    private final MutableLiveData<Event<Integer>> noRestaurantFoundEvent = new MutableLiveData<>();

    @NonNull
    private final MediatorLiveData<List<RestaurantMarkerViewStateItem>> mapViewStateMediatorLiveData = new MediatorLiveData<>();

    @Inject
    public MapViewModel(
        @NonNull IsGpsEnabledUseCase isGpsEnabledUseCase,
        @NonNull GetCurrentLocationUseCase getCurrentLocationUseCase,
        @NonNull GetNearbySearchRestaurantsWrapperUseCase getNearbySearchRestaurantsWrapperUseCase
    ) {
        this.getCurrentLocationUseCase = getCurrentLocationUseCase;

        LiveData<NearbySearchRestaurantsWrapper> nearbySearchRestaurantsWrapperLiveData = getNearbySearchRestaurantsWrapperUseCase.invoke();
        Log.d("controle", "MapViewModel: "+nearbySearchRestaurantsWrapperLiveData.getValue());
        LiveData<LocationEntityWrapper> locationEntityWrapperLiveData = getCurrentLocationUseCase.invoke();
        LiveData<Boolean> isGpsEnabledLiveData = isGpsEnabledUseCase.invoke();

        mapViewStateMediatorLiveData.addSource(isGpsEnabledLiveData, isGpsEnabled -> {
            combine(
                isGpsEnabled,
                locationEntityWrapperLiveData.getValue(),
                nearbySearchRestaurantsWrapperLiveData.getValue()
            );
        });

        mapViewStateMediatorLiveData.addSource(locationEntityWrapperLiveData, locationEntityWrapper -> {
            combine(
                isGpsEnabledLiveData.getValue(),
                locationEntityWrapper,
                nearbySearchRestaurantsWrapperLiveData.getValue()
            );
        });

        mapViewStateMediatorLiveData.addSource(nearbySearchRestaurantsWrapperLiveData, nearbySearchRestaurantsWrapper -> {
            combine(
                isGpsEnabledLiveData.getValue(),
                locationEntityWrapperLiveData.getValue(),
                nearbySearchRestaurantsWrapper
            );
        });
    }

    public LiveData<Event<Integer>> getNoRestaurantFoundEvent() {
        return noRestaurantFoundEvent;
    }

    public LiveData<Event<Integer>> getNoRestaurantMatchEvent() {
        return noRestaurantMatchEvent;
    }

    public LiveData<List<RestaurantMarkerViewStateItem>> getMapViewState() {
        return mapViewStateMediatorLiveData;
    }

    public LiveData<LocationEntityWrapper> getLocationState() {
        return getCurrentLocationUseCase.invoke();
    }

    private void combine(
        @Nullable Boolean isGpsEnabled,
        @Nullable LocationEntityWrapper locationEntityWrapper,
        @Nullable NearbySearchRestaurantsWrapper nearbySearchRestaurantsWrapper
    ) {
        Log.d("Control", "combine: "+ isGpsEnabled+" "+locationEntityWrapper+" "+nearbySearchRestaurantsWrapper);
        if (isGpsEnabled == null ||
            nearbySearchRestaurantsWrapper == null ||
            nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.Loading ||
            locationEntityWrapper == null
        ) {
            Log.d("Control", "return void");
            return;
        }

        List<RestaurantMarkerViewStateItem> restaurantMarkerViewStateItems = new ArrayList<>();
        if (locationEntityWrapper instanceof LocationEntityWrapper.GpsProviderEnabled) {
            Log.d("controle", "combine: don't return void");
            if (nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.Success) {
                Log.d("controle", "combine: wrapper "+((NearbySearchRestaurantsWrapper.Success) nearbySearchRestaurantsWrapper).getNearbySearchRestaurantsEntityList());
                for (NearbySearchRestaurantsEntity nearbySearchRestaurantsEntity :
                    ((NearbySearchRestaurantsWrapper.Success) nearbySearchRestaurantsWrapper).getNearbySearchRestaurantsEntityList()) {
                    Log.d("controle", "combine: nearbysearchentity "+nearbySearchRestaurantsEntity.getLocationEntity());
                        restaurantMarkerViewStateItems.add(
                            new RestaurantMarkerViewStateItem(
                                nearbySearchRestaurantsEntity.getPlaceId(),
                                nearbySearchRestaurantsEntity.getRestaurantName(),
                                new LatLng(
                                    nearbySearchRestaurantsEntity.getLocationEntity().getLatitude(),
                                    nearbySearchRestaurantsEntity.getLocationEntity().getLongitude()
                                )
                            )
                        );
                }
                Log.d("Control", "add restaurant "+restaurantMarkerViewStateItems);
            } else if (nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.NoResults) {
                noRestaurantFoundEvent.setValue(new Event<>(R.string.no_restaurant_found));
            }
            if (restaurantMarkerViewStateItems.isEmpty() &&
                nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.Success) {
                noRestaurantMatchEvent.setValue(new Event<>(R.string.no_restaurant_nearby));
            }
            mapViewStateMediatorLiveData.setValue(restaurantMarkerViewStateItems);
        }
    }

}
