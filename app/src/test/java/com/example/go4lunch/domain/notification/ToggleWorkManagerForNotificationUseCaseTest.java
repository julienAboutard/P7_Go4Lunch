package com.example.go4lunch.domain.notification;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.notification.NotificationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ToggleWorkManagerForNotificationUseCaseTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ScheduleWorkManagerForNotificationUseCase scheduleWorkManagerForNotificationUseCase;

    private ToggleWorkManagerForNotificationUseCase toggleWorkManagerForNotificationUseCase;

    @Before
    public void setUp() {
        toggleWorkManagerForNotificationUseCase = new ToggleWorkManagerForNotificationUseCase(
            notificationRepository,
            scheduleWorkManagerForNotificationUseCase
        );
    }

    @Test
    public void enableNotification() {
        // Given
        doReturn(false).when(notificationRepository).isNotificationEnabled();

        // When
        toggleWorkManagerForNotificationUseCase.invoke();

        // Then
        verify(notificationRepository).setNotificationEnabled(true);
        verify(notificationRepository).isNotificationEnabled();
        verify(scheduleWorkManagerForNotificationUseCase).invoke();
        verifyNoMoreInteractions(notificationRepository, scheduleWorkManagerForNotificationUseCase);
    }

    @Test
    public void disableNotification() {
        // Given
        doReturn(true).when(notificationRepository).isNotificationEnabled();

        // When
        toggleWorkManagerForNotificationUseCase.invoke();

        // Then
        verify(notificationRepository).setNotificationEnabled(false);
        verify(notificationRepository).isNotificationEnabled();
        verify(scheduleWorkManagerForNotificationUseCase).invoke();
        verifyNoMoreInteractions(notificationRepository, scheduleWorkManagerForNotificationUseCase);
    }
}
