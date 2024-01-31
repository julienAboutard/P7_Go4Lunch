package com.example.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.ui.utils.SingleLiveEvent;

public class TestUtils {

    public static <T> T getValueForTesting(@NonNull final LiveData<T> liveData) {
        liveData.observeForever(t -> {
        });

        return liveData.getValue();
    }
}
