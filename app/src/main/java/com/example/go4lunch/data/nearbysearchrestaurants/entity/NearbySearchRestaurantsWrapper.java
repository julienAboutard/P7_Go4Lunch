package com.example.go4lunch.data.nearbysearchrestaurants.entity;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class NearbySearchRestaurantsWrapper {

    public static class Loading extends NearbySearchRestaurantsWrapper{
    }

    public static class Success extends NearbySearchRestaurantsWrapper {
        @NonNull
        private final List<NearbySearchRestaurantsEntity> results;

        public Success(@NonNull List<NearbySearchRestaurantsEntity> results) {
            this.results = results;
        }

        @NonNull
        public List<NearbySearchRestaurantsEntity> getNearbySearchRestaurantsEntityList() {
            return results;
        }
    }

    public static class NoResults extends NearbySearchRestaurantsWrapper {
    }

    public static class RequestError extends NearbySearchRestaurantsWrapper {
        private final Throwable throwable;

        public RequestError(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
