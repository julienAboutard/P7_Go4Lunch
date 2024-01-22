package com.example.go4lunch.data.autocomplete.entity;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class PredictionEntityWrapper {

    public static class Loading extends PredictionEntityWrapper {
    }

    public static class Success extends PredictionEntityWrapper {
        @NonNull
        private final List<PredictionEntity> results;

        public Success(@NonNull List<PredictionEntity> results) {
            this.results = results;
        }

        @NonNull
        public List<PredictionEntity> getPredictionEntityList() {
            return results;
        }
    }

    public static class NoResults extends PredictionEntityWrapper {
    }

    public static class RequestError extends PredictionEntityWrapper {
        private final Throwable throwable;

        public RequestError(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
