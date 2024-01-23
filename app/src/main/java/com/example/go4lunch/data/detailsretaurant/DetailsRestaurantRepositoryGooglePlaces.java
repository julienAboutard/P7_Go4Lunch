package com.example.go4lunch.data.detailsretaurant;

import static com.example.go4lunch.BuildConfig.API_KEY;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.api.GooglePlacesApi;
import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantEntity;
import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantWrapper;
import com.example.go4lunch.data.detailsretaurant.response.DetailsRestaurantResponse;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton public class DetailsRestaurantRepositoryGooglePlaces implements DetailsRestaurantRepository {

    private final GooglePlacesApi googlePlacesApi;

    private final HashMap<String, DetailsRestaurantEntity> detailsHashMap = new HashMap<>();

    @Inject
    public DetailsRestaurantRepositoryGooglePlaces(GooglePlacesApi googlePlacesApi) {
        this.googlePlacesApi = googlePlacesApi;
    }

    @Override
    public LiveData<DetailsRestaurantWrapper> getRestaurantDetails(@NonNull String placeId) {

        MutableLiveData<DetailsRestaurantWrapper> resultMutableLiveData = new MutableLiveData<>();
        DetailsRestaurantEntity detailsRestaurant = detailsHashMap.get(placeId);

        if (detailsRestaurant == null) {
            resultMutableLiveData.setValue(new DetailsRestaurantWrapper.Loading());
            googlePlacesApi.getPlaceDetails(placeId, API_KEY)
                .enqueue(
                    new Callback<DetailsRestaurantResponse>() {
                        @Override
                        public void onResponse(Call<DetailsRestaurantResponse> call, Response<DetailsRestaurantResponse> response) {
                            if (response.isSuccessful() &&
                                response.body() != null &&
                                response.body().getStatus() != null &&
                                response.body().getStatus().equals("OK")
                            ) {
                                DetailsRestaurantEntity detailsRestaurantEntity = mapToDetailsRestaurantEntity(response.body());

                                if (detailsRestaurantEntity != null) {
                                    detailsHashMap.put(placeId, detailsRestaurantEntity);
                                    resultMutableLiveData.setValue(new DetailsRestaurantWrapper.Success(detailsRestaurantEntity));
                                } else {
                                    resultMutableLiveData.setValue(new DetailsRestaurantWrapper.RequestError(new Exception("Error while fetching details")));
                                }
                            } else {
                                resultMutableLiveData.setValue(new DetailsRestaurantWrapper.RequestError(new Exception("Error while fetching details")));
                            }
                        }

                        @Override
                        public void onFailure(Call<DetailsRestaurantResponse> call, Throwable t) {
                            resultMutableLiveData.setValue(new DetailsRestaurantWrapper.RequestError(t));
                            t.printStackTrace();
                        }
                    }
                );
        } else {
            resultMutableLiveData.setValue(new DetailsRestaurantWrapper.Success(detailsRestaurant));
        }
        return resultMutableLiveData;
    }

    private DetailsRestaurantEntity mapToDetailsRestaurantEntity(@Nullable DetailsRestaurantResponse response) {

        String placeId = null;
        String name = null;
        String vicinity = null;
        String photoReference;
        Float rating;
        String formattedPhoneNumber;
        String website;
        
        if (response != null &&
            response.getResult() != null
        ) {
            if (response.getResult().getPlaceId() != null &&
                response.getResult().getName() != null &&
                response.getResult().getVicinity() != null
            ) {
                placeId = response.getResult().getPlaceId();
                name = response.getResult().getName();
                vicinity = response.getResult().getVicinity();
            }

            if (response.getResult().getPhotos() != null &&
                response.getResult().getPhotos().size() > 0 &&
                response.getResult().getPhotos().get(0).getPhotoReference() != null) {
                photoReference = response.getResult().getPhotos().get(0).getPhotoReference();
            } else {
                photoReference = null;
            }

            if (response.getResult().getRating() != null) {
                rating = response.getResult().getRating();
            } else {
                rating = null;
            }

            if (response.getResult().getFormattedPhoneNumber() != null) {
                formattedPhoneNumber = response.getResult().getFormattedPhoneNumber();
            } else {
                formattedPhoneNumber = null;
            }

            if (response.getResult().getWebsite() != null) {
                website = response.getResult().getWebsite();
            } else {
                website = null;
            }

            if (placeId != null &&
                name != null &&
                vicinity != null
            ) {
                return new DetailsRestaurantEntity(
                    placeId,
                    name,
                    vicinity,
                    photoReference,
                    rating,
                    formattedPhoneNumber,
                    website
                );
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
