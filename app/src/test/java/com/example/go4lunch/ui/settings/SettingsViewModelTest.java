package com.example.go4lunch.ui.settings;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.domain.notification.IsNotificationEnabledUseCase;
import com.example.go4lunch.domain.notification.ToggleWorkManagerForNotificationUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private IsNotificationEnabledUseCase isNotificationEnabledUseCase;

    @Mock
    private ToggleWorkManagerForNotificationUseCase toggleWorkManagerForNotificationUseCase;

    private final MutableLiveData<Boolean> isNotificationEnabled = new MutableLiveData<>();

    private SettingsViewModel settingsViewModel;

    @Before
    public void setUp() {
        doReturn(isNotificationEnabled).when(isNotificationEnabledUseCase).invoke();

        settingsViewModel = new SettingsViewModel(isNotificationEnabledUseCase, toggleWorkManagerForNotificationUseCase);
    }

    @Test
    public void isNotificationEnabled_notificationEnabled() {
        // Given
        isNotificationEnabled.setValue(true);

        // When
        Boolean result = getValueForTesting(settingsViewModel.isNotificationEnabled());

        // Then
        assertTrue(result);
    }

    @Test
    public void isNotificationEnabled_notificationDisabled() {
        // Given
        isNotificationEnabled.setValue(false);

        // When
        Boolean result = getValueForTesting(settingsViewModel.isNotificationEnabled());

        // Then
        assertFalse(result);
    }

    @Test
    public void onToggleNotification() {
        // When
        settingsViewModel.toggleNotification();

        // Then
        verify(toggleWorkManagerForNotificationUseCase).invoke();
        verifyNoMoreInteractions(toggleWorkManagerForNotificationUseCase);
    }

    @Test
    public void onToggleNotificationTwice() {
        // When
        settingsViewModel.toggleNotification();
        settingsViewModel.toggleNotification();

        // Then
        verify(toggleWorkManagerForNotificationUseCase, times(2)).invoke();
        verifyNoMoreInteractions(toggleWorkManagerForNotificationUseCase);
    }

    @Test
    public void onStartGpsSettingsIntent() {
        // When
        Void result = getValueForTesting(settingsViewModel.getStartGpsSettingsIntentSingleLiveEvent());

        // Then
        assertTrue(true);
        verifyNoMoreInteractions(toggleWorkManagerForNotificationUseCase);
    }
}
