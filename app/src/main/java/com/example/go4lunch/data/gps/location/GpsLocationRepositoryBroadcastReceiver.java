package com.example.go4lunch.data.gps.location;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class GpsLocationRepositoryBroadcastReceiver extends BroadcastReceiver implements GpsLocationRepository {

    private static final long LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final int SMALLEST_DISPLACEMENT_THRESHOLD_METER = 100;

    @NonNull
    private final Context context;

    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;

    @NonNull
    private final MutableLiveData<LocationEntity> gpsLocationEntityMutableLiveData = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> isGpsEnabledMutableLiveData;


    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location location = locationResult.getLastLocation();

            if (location != null) {
                LocationEntity locationEntity = new LocationEntity(location.getLatitude(), location.getLongitude());
                gpsLocationEntityMutableLiveData.setValue(locationEntity);

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    boolean isGpsEnabledAtStart = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    isGpsEnabledMutableLiveData.setValue(isGpsEnabledAtStart);
                }
            }
        }
    };

    @Inject
    public GpsLocationRepositoryBroadcastReceiver(
        @NonNull @ApplicationContext Context context,
        @NonNull FusedLocationProviderClient fusedLocationProviderClient
    ) {
        this.context = context;
        this.fusedLocationProviderClient = fusedLocationProviderClient;

        isGpsEnabledMutableLiveData = new MutableLiveData<>();
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            isGpsEnabledMutableLiveData.setValue(
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            );
        }
    }

    @Override
    public LiveData<LocationEntityWrapper> getLocationStateLiveData() {
        MediatorLiveData<LocationEntityWrapper> gpsResponseMediatorLiveData = new MediatorLiveData<>();
        Observer<Object> observer = o -> {
            LocationEntityWrapper locationEntityWrapper;

            Boolean isGpsEnabled = isGpsEnabledMutableLiveData.getValue();
            LocationEntity locationEntity = gpsLocationEntityMutableLiveData.getValue();

            if (isGpsEnabled != null && !isGpsEnabled) {
                locationEntityWrapper = new LocationEntityWrapper.GpsProviderDisabled();
            } else if (locationEntity == null) {
                return;
            } else {
                locationEntityWrapper = new LocationEntityWrapper.GpsProviderEnabled(locationEntity);
            }
            gpsResponseMediatorLiveData.setValue(locationEntityWrapper);
        };
        gpsResponseMediatorLiveData.addSource(gpsLocationEntityMutableLiveData, observer);
        gpsResponseMediatorLiveData.addSource(isGpsEnabledMutableLiveData, observer);

        return Transformations.distinctUntilChanged(gpsResponseMediatorLiveData);
    }

    @Override
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public void startLocationRequest() {

        LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_INTERVAL_MS)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(LOCATION_REQUEST_INTERVAL_MS)
            .setMinUpdateDistanceMeters(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
            .build();

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        );
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action != null && action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isGpsEnabledMutableLiveData.setValue(isGpsEnabled);
            }
        }
    }

    @Override
    public void stopLocationRequest() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

}
