package com.example.go4lunch.domain.permission;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.gps.permission.GpsPermissionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HasGpsPermissionUseCase {

    @NonNull
    private final GpsPermissionRepository gpsPermissionRepository;

    @Inject
    public HasGpsPermissionUseCase(@NonNull GpsPermissionRepository gpsPermissionRepository) {
        this.gpsPermissionRepository = gpsPermissionRepository;
    }

    public LiveData<Boolean> invoke() {
        return gpsPermissionRepository.hasGpsPermissionLiveData();
    }
}
