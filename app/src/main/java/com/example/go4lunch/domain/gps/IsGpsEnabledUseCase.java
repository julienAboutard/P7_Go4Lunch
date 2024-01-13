package com.example.go4lunch.domain.gps;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.gps.location.GpsLocationRepository;

import javax.inject.Inject;

public class IsGpsEnabledUseCase {

    @NonNull
    private final GpsLocationRepository gpsLocationRepository;

    @Inject
    public IsGpsEnabledUseCase(@NonNull GpsLocationRepository gpsLocationRepository) {
        this.gpsLocationRepository = gpsLocationRepository;
    }

    public LiveData<Boolean> invoke() {
        return Transformations.switchMap(gpsLocationRepository.getLocationStateLiveData(), gpsResponse -> {
                MutableLiveData<Boolean> isGpsEnabledLiveData = new MutableLiveData<>();
                if (gpsResponse instanceof LocationEntityWrapper.GpsProviderDisabled) {
                    isGpsEnabledLiveData.setValue(false);
                } else if (gpsResponse instanceof LocationEntityWrapper.GpsProviderEnabled) {
                    isGpsEnabledLiveData.setValue(true);
                }
                return isGpsEnabledLiveData;
            }
        );
    }

}
