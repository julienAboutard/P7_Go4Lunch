package com.example.go4lunch;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;

import com.example.go4lunch.data.gps.location.GpsLocationRepositoryBroadcastReceiver;
import com.example.go4lunch.data.gps.permission.GpsPermissionRepositoryImpl;
import com.example.go4lunch.domain.notification.ScheduleWorkManagerForNotificationUseCase;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MainApplication extends Application implements Application.ActivityLifecycleCallbacks, Configuration.Provider {

    @Inject
    HiltWorkerFactory hiltWorkerFactory;

    @Inject
    GpsPermissionRepositoryImpl gpsPermissionRepositoryImpl;
    @Inject
    GpsLocationRepositoryBroadcastReceiver gpsLocationRepositoryBroadcastReceiver;

    @Inject
    ScheduleWorkManagerForNotificationUseCase scheduleWorkManagerForNotificationUseCase;

    private int activityCount;

    private boolean isGpsReceiverRegistered = false;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);
        registerGpsReceiver();
        scheduleWorkManagerForNotificationUseCase.invoke();
    }

    private void registerGpsReceiver() {
        IntentFilter intentFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        intentFilter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(gpsLocationRepositoryBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(gpsLocationRepositoryBroadcastReceiver, intentFilter);
        }
        isGpsReceiverRegistered = true;
    }

    @Override
    public void onActivityCreated(
        @NonNull Activity activity,
        @Nullable Bundle savedInstanceState
    ) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        activityCount++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        gpsPermissionRepositoryImpl.refreshGpsPermission();
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        activityCount--;
        if (activityCount == 0 && isGpsReceiverRegistered) {
            gpsLocationRepositoryBroadcastReceiver.stopLocationRequest();
            unregisterReceiver(gpsLocationRepositoryBroadcastReceiver);
            isGpsReceiverRegistered = false;
        }
    }

    @Override
    public void onActivitySaveInstanceState(
        @NonNull Activity activity,
        @NonNull Bundle outState
    ) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build();
    }
}
