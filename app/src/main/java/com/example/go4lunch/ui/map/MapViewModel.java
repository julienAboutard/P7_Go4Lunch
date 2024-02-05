package com.example.go4lunch.ui.map;

import androidx.annotation.ColorRes;
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
import com.example.go4lunch.domain.autocomplete.GetPredictionPlaceIdUseCase;
import com.example.go4lunch.domain.gps.IsGpsEnabledUseCase;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;
import com.example.go4lunch.domain.nearbysearchrestaurants.GetNearbySearchRestaurantsWrapperUseCase;
import com.example.go4lunch.domain.workmate.GetAttendantsGoingToSameRestaurantAsUserUseCase;
import com.example.go4lunch.ui.map.marker.RestaurantMarkerViewStateItem;
import com.example.go4lunch.ui.utils.Event;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        @NonNull GetNearbySearchRestaurantsWrapperUseCase getNearbySearchRestaurantsWrapperUseCase,
        @NonNull GetAttendantsGoingToSameRestaurantAsUserUseCase getAttendantsGoingToSameRestaurantAsUserUseCase,
        @NonNull GetPredictionPlaceIdUseCase getPredictionPlaceIdUseCase
    ) {
        this.getCurrentLocationUseCase = getCurrentLocationUseCase;

        LiveData<NearbySearchRestaurantsWrapper> nearbySearchRestaurantsWrapperLiveData = getNearbySearchRestaurantsWrapperUseCase.invoke();
        LiveData<LocationEntityWrapper> locationEntityWrapperLiveData = getCurrentLocationUseCase.invoke();
        LiveData<Boolean> isGpsEnabledLiveData = isGpsEnabledUseCase.invoke();
        LiveData<Map<String, Integer>> restaurantIdWithAttendantsMapLiveData = getAttendantsGoingToSameRestaurantAsUserUseCase.invoke();
        LiveData<String> placeIdLiveData = getPredictionPlaceIdUseCase.invoke();

        mapViewStateMediatorLiveData.addSource(isGpsEnabledLiveData, isGpsEnabled -> combine(
            isGpsEnabled,
            locationEntityWrapperLiveData.getValue(),
            nearbySearchRestaurantsWrapperLiveData.getValue(),
            restaurantIdWithAttendantsMapLiveData.getValue(),
            placeIdLiveData.getValue()
        )
        );

        mapViewStateMediatorLiveData.addSource(locationEntityWrapperLiveData, locationEntityWrapper -> combine(
            isGpsEnabledLiveData.getValue(),
            locationEntityWrapper,
            nearbySearchRestaurantsWrapperLiveData.getValue(),
            restaurantIdWithAttendantsMapLiveData.getValue(),
            placeIdLiveData.getValue()
        )
        );

        mapViewStateMediatorLiveData.addSource(nearbySearchRestaurantsWrapperLiveData, nearbySearchRestaurantsWrapper -> combine(
            isGpsEnabledLiveData.getValue(),
            locationEntityWrapperLiveData.getValue(),
            nearbySearchRestaurantsWrapper,
            restaurantIdWithAttendantsMapLiveData.getValue(),
            placeIdLiveData.getValue()
        )
        );

        mapViewStateMediatorLiveData.addSource(restaurantIdWithAttendantsMapLiveData, restaurantIdToAttendantsCount -> combine(
            isGpsEnabledLiveData.getValue(),
            locationEntityWrapperLiveData.getValue(),
            nearbySearchRestaurantsWrapperLiveData.getValue(),
            restaurantIdToAttendantsCount,
            placeIdLiveData.getValue()
        )
        );

        mapViewStateMediatorLiveData.addSource(placeIdLiveData, placeId -> combine(
            isGpsEnabledLiveData.getValue(),
            locationEntityWrapperLiveData.getValue(),
            nearbySearchRestaurantsWrapperLiveData.getValue(),
            restaurantIdWithAttendantsMapLiveData.getValue(),
            placeId
        )
        );
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
        @Nullable NearbySearchRestaurantsWrapper nearbySearchRestaurantsWrapper,
        @Nullable Map<String, Integer> restaurantIdToAttendantsCount,
        @Nullable String placeId
    ) {
        if (isGpsEnabled == null ||
            nearbySearchRestaurantsWrapper == null ||
            nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.Loading ||
            locationEntityWrapper == null
        ) {
            return;
        }

        List<RestaurantMarkerViewStateItem> restaurantMarkerViewStateItems = new ArrayList<>();
        if (locationEntityWrapper instanceof LocationEntityWrapper.GpsProviderEnabled) {
            if (nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.Success) {
                for (NearbySearchRestaurantsEntity nearbySearchRestaurantsEntity :
                    ((NearbySearchRestaurantsWrapper.Success) nearbySearchRestaurantsWrapper).getNearbySearchRestaurantsEntityList()) {
                    if (placeId == null) {
                        restaurantMarkerViewStateItems.add(
                            new RestaurantMarkerViewStateItem(
                                nearbySearchRestaurantsEntity.getPlaceId(),
                                nearbySearchRestaurantsEntity.getRestaurantName(),
                                new LatLng(
                                    nearbySearchRestaurantsEntity.getLocationEntity().getLatitude(),
                                    nearbySearchRestaurantsEntity.getLocationEntity().getLongitude()
                                ),
                                isRestaurantAttended(restaurantIdToAttendantsCount, nearbySearchRestaurantsEntity.getPlaceId())
                            )
                        );
                    } else if (nearbySearchRestaurantsEntity.getPlaceId().equals(placeId)) {
                        restaurantMarkerViewStateItems.add(
                            new RestaurantMarkerViewStateItem(
                                nearbySearchRestaurantsEntity.getPlaceId(),
                                nearbySearchRestaurantsEntity.getRestaurantName(),
                                new LatLng(
                                    nearbySearchRestaurantsEntity.getLocationEntity().getLatitude(),
                                    nearbySearchRestaurantsEntity.getLocationEntity().getLongitude()
                                ),
                                isRestaurantAttended(restaurantIdToAttendantsCount, nearbySearchRestaurantsEntity.getPlaceId())
                            )
                        );
                        break;
                    }
                }
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

    @ColorRes
    private int isRestaurantAttended(
        @Nullable Map<String, Integer> restaurantIdToAttendantsCount,
        @NonNull String placeId
    ) {
        if (restaurantIdToAttendantsCount != null && restaurantIdToAttendantsCount.containsKey(placeId)) {
            return R.color.ok_green_pale;
        } else {
            return R.color.color_secondary;
        }
    }
}
