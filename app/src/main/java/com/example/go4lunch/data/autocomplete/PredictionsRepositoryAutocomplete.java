package com.example.go4lunch.data.autocomplete;

import static com.example.go4lunch.BuildConfig.API_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.api.GooglePlacesApi;
import com.example.go4lunch.data.autocomplete.entity.PredictionEntity;
import com.example.go4lunch.data.autocomplete.entity.PredictionEntityWrapper;
import com.example.go4lunch.data.autocomplete.response.AutocompletePredictionResponse;
import com.example.go4lunch.data.autocomplete.response.PredictionsItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class PredictionsRepositoryAutocomplete implements PredictionsRepository {

    @NonNull
    private final GooglePlacesApi googleMapsApi;

    private final HashMap<String, List<PredictionEntity>> predictionsHashMap = new HashMap<>();
    private final MutableLiveData<String> predictionPlaceIdMutableLiveData = new MutableLiveData<>();

    @Inject
    public PredictionsRepositoryAutocomplete(@NonNull GooglePlacesApi googleMapsApi) {
        this.googleMapsApi = googleMapsApi;
    }

    @Override
    public LiveData<PredictionEntityWrapper> getPredictionsLiveData(
        @NonNull String input,
        @NonNull Double latitude,
        @NonNull Double longitude,
        int radius,
        @NonNull String types
    ) {
        MutableLiveData<PredictionEntityWrapper> resultsMutableLiveData = new MutableLiveData<>();

        String keyInput = input + latitude + longitude + radius + types;

        List<PredictionEntity> predictionsList = predictionsHashMap.get(keyInput);

        String location = latitude + "," + longitude;

        if (predictionsList == null) {
            googleMapsApi.getPredictions(
                input,
                location,
                radius,
                types,
                API_KEY
            ).enqueue(
                new Callback<AutocompletePredictionResponse>() {
                    @Override
                    public void onResponse(
                        @NonNull Call<AutocompletePredictionResponse> call,
                        @NonNull Response<AutocompletePredictionResponse> response
                    ) {
                        if (response.isSuccessful() &&
                            response.body() != null &&
                            response.body().getPredictions() != null
                        ) {
                            List<PredictionEntity> predictionEntities = mapToPredictionEntityList(response.body());
                            if (predictionEntities != null) {
                                predictionsHashMap.put(keyInput, predictionEntities);
                                resultsMutableLiveData.setValue(
                                    new PredictionEntityWrapper.Success(
                                        predictionEntities
                                    )
                                );
                            }
                        } else if (response.isSuccessful() &&
                            response.body() != null &&
                            response.body().getStatus() != null &&
                            response.body().getStatus().equals("ZERO_RESULTS")
                        ) {
                            List<PredictionEntity> emptyList = new ArrayList<>();
                            predictionsHashMap.put(keyInput, emptyList);
                            resultsMutableLiveData.setValue(new PredictionEntityWrapper.NoResults());
                        }
                    }

                    @Override
                    public void onFailure(
                        @NonNull Call<AutocompletePredictionResponse> call,
                        @NonNull Throwable t
                    ) {
                        resultsMutableLiveData.setValue(new PredictionEntityWrapper.RequestError(t));
                        t.printStackTrace();
                    }
                }
            );
        } else {
            resultsMutableLiveData.setValue(
                new PredictionEntityWrapper.Success(predictionsList)
            );
        }
        return resultsMutableLiveData;
    }

    private List<PredictionEntity> mapToPredictionEntityList(@Nullable AutocompletePredictionResponse response) {
        List<PredictionEntity> results = new ArrayList<>();

        if (response != null && response.getPredictions() != null) {
            for (PredictionsItem prediction : response.getPredictions()) {
                if (prediction.getPlaceId() != null &&
                    prediction.getStructuredFormatting() != null &&
                    prediction.getStructuredFormatting().getMainText() != null) {
                    results.add(
                        new PredictionEntity(
                            prediction.getPlaceId(),
                            prediction.getStructuredFormatting().getMainText()
                        )
                    );
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
        return results;
    }

    @Override
    public void savePredictionPlaceId(@NonNull String query) {
        predictionPlaceIdMutableLiveData.setValue(query);
    }

    @Override
    public LiveData<String> getPredictionPlaceIdLiveData() {
        return predictionPlaceIdMutableLiveData;
    }

    @Override
    public void resetPredictionPlaceIdQuery() {
        predictionPlaceIdMutableLiveData.setValue(null);
    }
}
