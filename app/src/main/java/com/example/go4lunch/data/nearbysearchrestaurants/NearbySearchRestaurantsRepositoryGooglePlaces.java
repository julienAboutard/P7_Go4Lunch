package com.example.go4lunch.data.nearbysearchrestaurants;

import static com.example.go4lunch.BuildConfig.API_KEY;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.api.GooglePlacesApi;
import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsEntity;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsWrapper;
import com.example.go4lunch.data.nearbysearchrestaurants.response.NearbySearchResponse;
import com.example.go4lunch.data.nearbysearchrestaurants.response.ResultsItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class NearbySearchRestaurantsRepositoryGooglePlaces implements NearbySearchRestaurantsRepository {

    @NonNull
    private final GooglePlacesApi googlePlacesApi;

    private final HashMap<LocationEntity, List<NearbySearchRestaurantsEntity>> nearbySearchHashMap = new HashMap<>();

    @Inject
    public NearbySearchRestaurantsRepositoryGooglePlaces(@NonNull GooglePlacesApi googlePlacesApi) {
        this.googlePlacesApi = googlePlacesApi;
    }

    @Override
    public LiveData<NearbySearchRestaurantsWrapper> getNearbyRestaurants(
        @NonNull Double latitude,
        @NonNull Double longitude,
        @NonNull String type,
        int radius
    ) {
        MutableLiveData<NearbySearchRestaurantsWrapper> resultMutableLiveData = new MutableLiveData<>();
        LocationEntity userLocation = new LocationEntity(latitude, longitude);
        List<NearbySearchRestaurantsEntity> nearbyRestaurantsList = nearbySearchHashMap.get(userLocation);
        String location = latitude + "," + longitude;
        if (nearbyRestaurantsList == null) {
            resultMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.Loading());
            googlePlacesApi.getNearby(
                    location,
                    type,
                    radius,
                    API_KEY
                )
                .enqueue(
                    new Callback<NearbySearchResponse>() {
                        @Override
                        public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                            if (response.isSuccessful() &&
                                response.body() != null &&
                                response.body().getStatus() != null &&
                                response.body().getStatus().equals("OK")
                            ) {
                                List<NearbySearchRestaurantsEntity> nearbySearchRestaurantsEntityList = mapToNearbySearchEntityList(
                                    response.body(),
                                    location
                                );
                                if (nearbySearchRestaurantsEntityList != null) {
                                    nearbySearchHashMap.put(userLocation, nearbySearchRestaurantsEntityList);
                                    resultMutableLiveData.setValue(
                                        new NearbySearchRestaurantsWrapper.Success(
                                            nearbySearchRestaurantsEntityList
                                        )
                                    );
                                }
                            } else if (response.isSuccessful() &&
                                response.body() != null &&
                                response.body().getStatus() != null &&
                                response.body().getStatus().equals("ZERO_RESULTS")
                            ) {
                                List<NearbySearchRestaurantsEntity> emptyList = new ArrayList<>();
                                nearbySearchHashMap.put(userLocation, emptyList);
                                resultMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.NoResults());
                            }
                        }

                        @Override
                        public void onFailure(
                            @NonNull Call<NearbySearchResponse> call,
                            @NonNull Throwable t
                        ) {
                            resultMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.RequestError(t));
                            t.printStackTrace();
                        }
                    }
                );
        } else {
            resultMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.Success(nearbyRestaurantsList));
        }
        return resultMutableLiveData;
    }

    private List<NearbySearchRestaurantsEntity> mapToNearbySearchEntityList(
        @Nullable NearbySearchResponse response,
        @NonNull String userLocation
    ) {
        List<NearbySearchRestaurantsEntity> results = new ArrayList<>();

        if (response != null && response.getResults() != null) {
            for (ResultsItem result : response.getResults()) {
                String placeId = result.getPlaceId();
                String name = result.getName();
                String vicinity = result.getVicinity();

                String photoUrl;
                if (result.getPhotos() != null &&
                    !result.getPhotos().isEmpty() &&
                    result.getPhotos().get(0) != null) {
                    photoUrl = result.getPhotos().get(0).getPhotoReference();
                } else {
                    photoUrl = null;
                }

                Float rating;
                if (result.getRating() != null) {
                    rating = result.getRating();
                } else {
                    rating = null;
                }

                LocationEntity locationEntity;
                Integer distance;
                if (result.getGeometry() != null && result.getGeometry().getLocation() != null) {
                    Double latitude = result.getGeometry().getLocation().getLat();
                    Double longitude = result.getGeometry().getLocation().getLng();
                    locationEntity = new LocationEntity(latitude, longitude);
                    distance = getDistanceFromUserLocation(locationEntity, userLocation);
                } else {
                    locationEntity = null;
                    distance = null;
                }

                Boolean openingHours;
                if (result.getOpeningHours() != null && result.getOpeningHours().isOpenNow() != null) {
                    openingHours = result.getOpeningHours().isOpenNow();
                } else {
                    openingHours = null;
                }

                if (placeId != null &&
                    name != null &&
                    vicinity != null &&
                    locationEntity != null
                ) {
                    NearbySearchRestaurantsEntity searchResult = new NearbySearchRestaurantsEntity(
                        placeId,
                        name,
                        vicinity,
                        photoUrl,
                        rating,
                        locationEntity,
                        distance,
                        openingHours);
                    results.add(searchResult);
                } else {
                    return null;
                }
            }
        }
        return results;
    }

    private Integer getDistanceFromUserLocation(
        @NonNull LocationEntity restaurantLocationEntity,
        @NonNull String userLocationString
    ) {
        String[] coordinatesArr = userLocationString.split(",");
        double userLatitude = Double.parseDouble(coordinatesArr[0]);
        double userLongitude = Double.parseDouble(coordinatesArr[1]);

        Location userLocation = new Location("userLocation");
        userLocation.setLatitude(userLatitude);
        userLocation.setLongitude(userLongitude);

        Location restaurantLocation = new Location("nearbySearchResultRestaurantLocation");
        restaurantLocation.setLatitude(restaurantLocationEntity.getLatitude());
        restaurantLocation.setLongitude(restaurantLocationEntity.getLongitude());

        float distance = userLocation.distanceTo(restaurantLocation);

        return (int) Math.floor(distance);
    }
}
