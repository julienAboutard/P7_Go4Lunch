package com.example.go4lunch.domain.notification;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.notification.NotificationRepository;

import javax.inject.Inject;

public class ToggleWorkManagerForNotificationUseCase {

    @NonNull
    private final NotificationRepository notificationRepository;

    @NonNull
    private final ScheduleWorkManagerForNotificationUseCase scheduleWorkManagerForNotificationUseCase;

    @Inject
    public ToggleWorkManagerForNotificationUseCase(
        @NonNull NotificationRepository notificationRepository,
        @NonNull ScheduleWorkManagerForNotificationUseCase scheduleWorkManagerForNotificationUseCase
    ) {
        this.notificationRepository = notificationRepository;
        this.scheduleWorkManagerForNotificationUseCase = scheduleWorkManagerForNotificationUseCase;
    }

    public void invoke() {
        notificationRepository.setNotificationEnabled(!notificationRepository.isNotificationEnabled());
        scheduleWorkManagerForNotificationUseCase.invoke();
    }
}
