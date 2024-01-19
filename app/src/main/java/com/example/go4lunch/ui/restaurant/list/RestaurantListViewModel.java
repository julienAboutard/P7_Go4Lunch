package com.example.go4lunch.ui.restaurant.list;

import static com.example.go4lunch.BuildConfig.API_KEY;
import static com.example.go4lunch.ui.restaurant.list.ErrorDrawable.NO_GPS_FOUND;
import static com.example.go4lunch.ui.restaurant.list.ErrorDrawable.NO_RESULT_FOUND;
import static com.example.go4lunch.ui.restaurant.list.ErrorDrawable.REQUEST_FAILURE;
import static com.example.go4lunch.ui.restaurant.list.RestaurantOpeningState.IS_CLOSED;
import static com.example.go4lunch.ui.restaurant.list.RestaurantOpeningState.IS_NOT_DEFINED;
import static com.example.go4lunch.ui.restaurant.list.RestaurantOpeningState.IS_OPEN;

import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsEntity;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsWrapper;
import com.example.go4lunch.domain.gps.IsGpsEnabledUseCase;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;
import com.example.go4lunch.domain.nearbysearchresaturants.GetNearbySearchRestaurantsWrapperUseCase;
import com.example.go4lunch.domain.permission.HasGpsPermissionUseCase;
import com.example.go4lunch.domain.workmate.GetAttendantsGoingToSameRestaurantAsUserUseCase;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantListViewModel extends ViewModel {

    @NonNull
    private final Resources resources;

    @NonNull
    private final MediatorLiveData<List<RestaurantListViewStateItem>> restaurantListMediatorLiveData = new MediatorLiveData<>();

    @Inject
    public RestaurantListViewModel(
        @NonNull GetNearbySearchRestaurantsWrapperUseCase getNearbySearchRestaurantsWrapperUseCase,
        @NonNull GetCurrentLocationUseCase getCurrentLocationUseCase,
        @NonNull HasGpsPermissionUseCase hasGpsPermissionUseCase,
        @NonNull IsGpsEnabledUseCase isGpsEnabledUseCase,
        @NonNull GetAttendantsGoingToSameRestaurantAsUserUseCase getAttendantsGoingToSameRestaurantAsUserUseCase,
        @NonNull Resources resources
    ) {
        this.resources = resources;

        LiveData<LocationEntityWrapper> locationLiveData = getCurrentLocationUseCase.invoke();
        LiveData<Boolean> isGpsEnabledMutableLiveData = isGpsEnabledUseCase.invoke();
        LiveData<Boolean> hasGpsPermissionLiveData = hasGpsPermissionUseCase.invoke();
        LiveData<NearbySearchRestaurantsWrapper> nearbySearchRestaurantsWrapperLiveData = getNearbySearchRestaurantsWrapperUseCase.invoke();
        LiveData<Map<String, Integer>> attendantsByRestaurantIdsLiveData = getAttendantsGoingToSameRestaurantAsUserUseCase.invoke();

        restaurantListMediatorLiveData.addSource(hasGpsPermissionLiveData, hasGpsPermission ->
            combine(
                hasGpsPermission,
                isGpsEnabledMutableLiveData.getValue(),
                locationLiveData.getValue(),
                nearbySearchRestaurantsWrapperLiveData.getValue(),
                attendantsByRestaurantIdsLiveData.getValue()
            )
        );

        restaurantListMediatorLiveData.addSource(locationLiveData, location ->
            combine(
                hasGpsPermissionLiveData.getValue(),
                isGpsEnabledMutableLiveData.getValue(),
                location,
                nearbySearchRestaurantsWrapperLiveData.getValue(),
                attendantsByRestaurantIdsLiveData.getValue()
            )
        );

        restaurantListMediatorLiveData.addSource(nearbySearchRestaurantsWrapperLiveData, nearbySearchWrapper ->
            combine(
                hasGpsPermissionLiveData.getValue(),
                isGpsEnabledMutableLiveData.getValue(),
                locationLiveData.getValue(),
                nearbySearchWrapper,
                attendantsByRestaurantIdsLiveData.getValue()
            )
        );

        restaurantListMediatorLiveData.addSource(isGpsEnabledMutableLiveData, isGpsEnabled ->
            combine(
                hasGpsPermissionLiveData.getValue(),
                isGpsEnabled,
                locationLiveData.getValue(),
                nearbySearchRestaurantsWrapperLiveData.getValue(),
                attendantsByRestaurantIdsLiveData.getValue()
            )
        );

        restaurantListMediatorLiveData.addSource(attendantsByRestaurantIdsLiveData, attendantsRestaurantsId ->
            combine(
                hasGpsPermissionLiveData.getValue(),
                isGpsEnabledMutableLiveData.getValue(),
                locationLiveData.getValue(),
                nearbySearchRestaurantsWrapperLiveData.getValue(),
                attendantsRestaurantsId
            )
        );
    }

    private void combine(
        @Nullable Boolean hasGpsPermission,
        @Nullable Boolean isGpsEnabled,
        @Nullable LocationEntityWrapper locationStateEntity,
        @Nullable NearbySearchRestaurantsWrapper nearbySearchRestaurantsWrapper,
        @Nullable Map<String, Integer> attendantsByRestaurantIds
    ) {
        if (nearbySearchRestaurantsWrapper == null || locationStateEntity == null || isGpsEnabled == null) {
            return;
        }
        List<RestaurantListViewStateItem> result = new ArrayList<>();
        if (hasGpsPermission != null && !hasGpsPermission) {
            result.add(
                new RestaurantListViewStateItem.RestaurantListErrorItem(
                    resources.getString(R.string.list_error_message_no_gps),
                    NO_GPS_FOUND
                )
            );
            restaurantListMediatorLiveData.setValue(result);
        }

        if (locationStateEntity instanceof LocationEntityWrapper.GpsProviderDisabled) {
            result.add(
                new RestaurantListViewStateItem.RestaurantListErrorItem(
                    resources.getString(R.string.list_error_message_no_gps),
                    NO_GPS_FOUND
                )
            );
            restaurantListMediatorLiveData.setValue(result);
            return;
        }

        if (!isGpsEnabled) {
            result.add(
                new RestaurantListViewStateItem.RestaurantListErrorItem(
                    resources.getString(R.string.list_error_message_no_gps),
                    NO_GPS_FOUND
                )
            );
            restaurantListMediatorLiveData.setValue(result);
            return;
        }

        if (nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.Loading) {
            result.add(
                new RestaurantListViewStateItem.Loading()
            );
        } else if (nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.NoResults) {
            result.add(
                new RestaurantListViewStateItem.RestaurantListErrorItem(
                    resources.getString(R.string.list_error_message_no_results),
                    NO_RESULT_FOUND
                )
            );
        } else if (nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.Success) {
            if (locationStateEntity instanceof LocationEntityWrapper.GpsProviderEnabled) {
                LocationEntity location = ((LocationEntityWrapper.GpsProviderEnabled) locationStateEntity).locationEntity;

                List<NearbySearchRestaurantsEntity> sortedRestaurantList = sortNearbyRestaurants(
                    ((NearbySearchRestaurantsWrapper.Success) nearbySearchRestaurantsWrapper).getNearbySearchRestaurantsEntityList(),
                    location
                );
                for (NearbySearchRestaurantsEntity nearbySearchEntity : sortedRestaurantList) {
                    result.add(
                        new RestaurantListViewStateItem.RestaurantListItem(
                            nearbySearchEntity.getPlaceId(),
                            nearbySearchEntity.getRestaurantName(),
                            nearbySearchEntity.getVicinity(),
                            nearbySearchEntity.getDistance() + resources.getString(R.string.distance_meter),
                            getAttendants(nearbySearchEntity.getPlaceId(), attendantsByRestaurantIds),
                            formatOpeningStatus(nearbySearchEntity.getOpen()),
                            parseRestaurantPictureUrl(nearbySearchEntity.getPhotoReferenceUrl()),
                            isRatingBarVisible(nearbySearchEntity.getRating()),
                            convertFiveToThreeRating(nearbySearchEntity.getRating())
                        )
                    );
                }
            }
        } else if (nearbySearchRestaurantsWrapper instanceof NearbySearchRestaurantsWrapper.RequestError) {
            ((NearbySearchRestaurantsWrapper.RequestError) nearbySearchRestaurantsWrapper).getThrowable().printStackTrace();
            result.add(
                new RestaurantListViewStateItem.RestaurantListErrorItem(
                    resources.getString(R.string.list_error_message_generic),
                    REQUEST_FAILURE
                )
            );
        }
        if (result.isEmpty()) {
            result.add(
                new RestaurantListViewStateItem.RestaurantListErrorItem(
                    resources.getString(R.string.list_no_restaurant_match),
                    NO_RESULT_FOUND
                )
            );
        }
        restaurantListMediatorLiveData.setValue(result);
    }

    private List<NearbySearchRestaurantsEntity> sortNearbyRestaurants(
        @NonNull List<NearbySearchRestaurantsEntity> nearbySearchEntityList,
        @NonNull LocationEntity userLocationEntity
    ) {
        LatLng userLatLng = new LatLng(userLocationEntity.getLatitude(), userLocationEntity.getLongitude());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return nearbySearchEntityList.stream()
                .sorted(Comparator.comparingDouble(place ->
                        SphericalUtil.computeDistanceBetween(
                            userLatLng,
                            new LatLng(place.getLocationEntity().getLatitude(), place.getLocationEntity().getLongitude()
                            )
                        )
                    )
                )
                .collect(Collectors.toList());
        } else {
            return nearbySearchEntityList;
        }
    }

    private String getAttendants(
        String placeId,
        Map<String, Integer> attendantsByRestaurantIds
    ) {
        if (attendantsByRestaurantIds != null &&
            attendantsByRestaurantIds.containsKey(placeId) &&
            attendantsByRestaurantIds.get(placeId) != null
        ) {
            return String.valueOf(attendantsByRestaurantIds.get(placeId));
        } else {
            return "0";
        }
    }

    @NonNull
    public LiveData<List<RestaurantListViewStateItem>> getRestaurants() {
        return restaurantListMediatorLiveData;
    }

    private RestaurantOpeningState formatOpeningStatus(@Nullable Boolean isOpen) {
        if (isOpen == null) {
            return IS_NOT_DEFINED;
        } else if (isOpen) {
            return IS_OPEN;
        } else {
            return IS_CLOSED;
        }
    }

    private boolean isRatingBarVisible(@Nullable Float rating) {
        return rating != null && rating > 0F;
    }

    @Nullable
    private String parseRestaurantPictureUrl(String photoReferenceUrl) {
        if (photoReferenceUrl != null && !photoReferenceUrl.isEmpty()) {
            return resources.getString(R.string.google_image_url, photoReferenceUrl, API_KEY);
        } else {
            return null;
        }
    }

    private float convertFiveToThreeRating(@Nullable Float fiveRating) {
        if (fiveRating == null) {
            return 0f;
        } else {
            float convertedRating = Math.round(fiveRating * 2) / 2f;
            return Math.min(3f, convertedRating / 5f * 3f);
        }
    }
}
