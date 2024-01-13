package com.example.go4lunch.data.gps.permission;

import androidx.lifecycle.LiveData;

public interface GpsPermissionRepository {

    LiveData<Boolean> hasGpsPermissionLiveData();

    void refreshGpsPermission();
}
