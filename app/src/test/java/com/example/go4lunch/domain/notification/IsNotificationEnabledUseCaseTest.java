package com.example.go4lunch.domain.notification;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.notification.NotificationRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsNotificationEnabledUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private NotificationRepository notificationRepository;

    private IsNotificationEnabledUseCase isNotificationEnabledUseCase;

    @Before
    public void setUp() {
        isNotificationEnabledUseCase = new IsNotificationEnabledUseCase(notificationRepository);
    }

    @Test
    public void notificationIsEnabled() {
        // Given
        doReturn(true).when(notificationRepository).isNotificationEnabled();

        // When
        Boolean result = getValueForTesting(isNotificationEnabledUseCase.invoke());

        // Then
        assertEquals(true, result);
    }

    @Test
    public void notificationIsDisabled() {
        // Given
        doReturn(false).when(notificationRepository).isNotificationEnabled();

        // When
        Boolean result = getValueForTesting(isNotificationEnabledUseCase.invoke());

        // Then
        assertEquals(false, result);
    }
}
