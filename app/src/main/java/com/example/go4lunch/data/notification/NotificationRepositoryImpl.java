package com.example.go4lunch.data.notification;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.go4lunch.ui.notification.NotificationWorker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotificationRepositoryImpl implements NotificationRepository {

    private static final String KEY_NOTIFICATION_ENABLED = "NOTIFICATION_ENABLED";
    private final static String NOTIFICATION_WORKER = "NOTIFICATION_WORKER";

    @NonNull
    private final SharedPreferences sharedPreferences;

    @NonNull
    private final WorkManager workManager;

    @Inject
    public NotificationRepositoryImpl(
        @NonNull SharedPreferences sharedPreferences,
        @NonNull WorkManager workManager
    ) {
        this.sharedPreferences = sharedPreferences;
        this.workManager = workManager;
    }

    @Override
    public boolean isNotificationEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, true);
    }

    @Override
    public void setNotificationEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply();
    }

    @Override
    public void scheduleNotification(long delay) {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
            NotificationWorker.class,
            24,
            TimeUnit.HOURS)
            .addTag(NOTIFICATION_WORKER)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build();

        workManager
            .enqueueUniquePeriodicWork(
                NOTIFICATION_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            );
    }

    @Override
    public void cancelNotification() {
        workManager.cancelAllWorkByTag(NOTIFICATION_WORKER);
    }
}
