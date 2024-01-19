package com.example.go4lunch.data.detailsretaurant.entity;

import androidx.annotation.NonNull;

public abstract class DetailsRestaurantWrapper {

    public static class Loading extends DetailsRestaurantWrapper {
    }

    public static class Success extends DetailsRestaurantWrapper {
        @NonNull
        private final DetailsRestaurantEntity detailsRestaurantEntity;

        public Success(@NonNull DetailsRestaurantEntity detailsRestaurantEntity) {
            this.detailsRestaurantEntity = detailsRestaurantEntity;
        }

        @NonNull
        public DetailsRestaurantEntity getDetailsRestaurantEntity() {
            return detailsRestaurantEntity;
        }
    }

    public static class RequestError extends DetailsRestaurantWrapper {
        private final Throwable throwable;

        public RequestError(Throwable throwable) {
            this.throwable = throwable;
        }


        public Throwable getThrowable() {
            return throwable;
        }
    }
}
