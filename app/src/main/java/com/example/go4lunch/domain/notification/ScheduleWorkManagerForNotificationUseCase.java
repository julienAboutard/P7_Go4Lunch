package com.example.go4lunch.domain.notification;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.notification.NotificationRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalTime;

import javax.inject.Inject;

public class ScheduleWorkManagerForNotificationUseCase {

    @NonNull
    private final NotificationRepository notificationRepository;

    @NonNull
    private final Clock clock;

    @Inject
    public ScheduleWorkManagerForNotificationUseCase(
        @NonNull NotificationRepository notificationRepository,
        @NonNull Clock clock
    ) {
        this.notificationRepository = notificationRepository;
        this.clock = clock;
    }

    public void invoke() {
        if (notificationRepository.isNotificationEnabled()) {
            notificationRepository.scheduleNotification(calculateDelayUntilNoon());
        } else {
            notificationRepository.cancelNotification();
        }
    }

    private long calculateDelayUntilNoon() {
        Duration delay = Duration.between(LocalTime.now(clock), LocalTime.NOON);
        if (delay.isNegative()) {
            delay = delay.plusDays(1);
        }
        return delay.toMillis();
    }
}
