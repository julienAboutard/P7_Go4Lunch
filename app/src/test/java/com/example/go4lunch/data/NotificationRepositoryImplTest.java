package com.example.go4lunch.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.content.SharedPreferences;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.go4lunch.data.notification.NotificationRepositoryImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NotificationRepositoryImplTest {

    private static final String KEY_NOTIFICATION_ENABLED = "NOTIFICATION_ENABLED";

    private final static String NOTIFICATION_WORKER = "NOTIFICATION_WORKER";

    @Mock
    private SharedPreferences sharedPreferences;

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private WorkManager workManager;

    private NotificationRepositoryImpl notificationRepository;

    @Before
    public void setUp() {
        doReturn(editor).when(sharedPreferences).edit();
        doReturn(editor).when(editor).putBoolean(eq(KEY_NOTIFICATION_ENABLED), anyBoolean());

        notificationRepository = new NotificationRepositoryImpl(sharedPreferences, workManager);
    }

    @Test
    public void notificationsIsEnabled() {
        // Given
        given(sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, true)).willReturn(true);

        // When
        boolean result = notificationRepository.isNotificationEnabled();

        assertTrue(result);
        verifyNoMoreInteractions(workManager);
    }

    @Test
    public void notificationsIsDisabled() {
        // Given
        given(sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, true)).willReturn(false);

        // When
        boolean result = notificationRepository.isNotificationEnabled();
        assertFalse(result);
        verifyNoMoreInteractions(workManager);
    }

    @Test
    public void setNotificationEnabled() {
        // When
        notificationRepository.setNotificationEnabled(true);

        // Then
        verify(editor).putBoolean(KEY_NOTIFICATION_ENABLED, true);
        verifyNoMoreInteractions(workManager);
    }

    @Test
    public void setNotificationDisabled() {
        // When
        notificationRepository.setNotificationEnabled(false);

        // Then
        verify(editor).putBoolean(KEY_NOTIFICATION_ENABLED, false);
        verifyNoMoreInteractions(workManager);
    }

    @Test
    public void onScheduleNotification() {
        // When
        long delay = 7200000L;
        notificationRepository.scheduleNotification(delay);

        // Then
        verify(workManager).enqueueUniquePeriodicWork(
            eq(NOTIFICATION_WORKER),
            eq(ExistingPeriodicWorkPolicy.KEEP),
            any(PeriodicWorkRequest.class)
        );
        verifyNoMoreInteractions(workManager);
    }

    @Test
    public void onCancelNotification_shouldCancelAllWorkByTag() {
        // When
        notificationRepository.cancelNotification();

        // Then
        verify(workManager).cancelAllWorkByTag(NOTIFICATION_WORKER);
        verifyNoMoreInteractions(workManager);
    }
}
