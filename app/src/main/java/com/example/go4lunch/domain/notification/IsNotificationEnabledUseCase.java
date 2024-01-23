package com.example.go4lunch.domain.notification;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.notification.NotificationRepository;

import javax.inject.Inject;

public class IsNotificationEnabledUseCase {

    @NonNull
    private final NotificationRepository notificationRepository;

    @Inject
    public IsNotificationEnabledUseCase(@NonNull NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public LiveData<Boolean> invoke() {
        return new MutableLiveData<>(notificationRepository.isNotificationEnabled());
    }
}
