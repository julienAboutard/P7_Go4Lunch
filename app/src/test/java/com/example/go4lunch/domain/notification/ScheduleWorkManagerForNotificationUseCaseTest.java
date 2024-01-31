package com.example.go4lunch.domain.notification;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.notification.NotificationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleWorkManagerForNotificationUseCaseTest {

    @Mock
    private NotificationRepository notificationRepository;

    private final static long EPOCH_MILLI = 1706542496;
    private final Clock clock = Clock.fixed(Instant.ofEpochMilli(EPOCH_MILLI), ZoneOffset.UTC);

    private ScheduleWorkManagerForNotificationUseCase scheduleWorkManagerForNotificationUseCase;

    @Before
    public void setUp() {
        scheduleWorkManagerForNotificationUseCase = new ScheduleWorkManagerForNotificationUseCase(notificationRepository, clock);
    }

    @Test
    public void notificationIsEnabled() {
        // Given
        doReturn(true).when(notificationRepository).isNotificationEnabled();

        // When
        scheduleWorkManagerForNotificationUseCase.invoke();

        // Then
        verify(notificationRepository).scheduleNotification(anyLong());
        verify(notificationRepository, never()).cancelNotification();
    }

    @Test
    public void notificationIsDisabled() {
        // Given
        doReturn(false).when(notificationRepository).isNotificationEnabled();

        // When
        scheduleWorkManagerForNotificationUseCase.invoke();

        // Then
        verify(notificationRepository).cancelNotification();
        verify(notificationRepository, never()).scheduleNotification(anyLong());
    }
}
