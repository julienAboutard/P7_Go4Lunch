package com.example.go4lunch.data.api;

import com.example.go4lunch.data.autocomplete.response.AutocompletePredictionResponse;
import com.example.go4lunch.data.detailsretaurant.response.DetailsRestaurantResponse;
import com.example.go4lunch.data.nearbysearchrestaurants.response.NearbySearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesApi {

    @GET("nearbysearch/json")
    Call<NearbySearchResponse> getNearby(
        @Query("location") String location,
        @Query("type") String type,
        @Query("radius") int radius,
        @Query("key") String apiKey
    );

    @GET("details/json")
    Call<DetailsRestaurantResponse> getPlaceDetails(
        @Query("place_id") String placeId,
        @Query("key") String apiKey
    );

    @GET("autocomplete/json")
    Call<AutocompletePredictionResponse> getPredictions(
        @Query("input") String input,
        @Query("location") String location,
        @Query("radius") int radius,
        @Query("types") String types,
        @Query("key") String apiKey
    );
}
