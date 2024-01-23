package com.example.go4lunch.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.domain.notification.IsNotificationEnabledUseCase;
import com.example.go4lunch.domain.notification.ToggleWorkManagerForNotificationUseCase;
import com.example.go4lunch.ui.utils.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    @NonNull
    private final IsNotificationEnabledUseCase isNotificationEnabledUseCase;

    @NonNull
    private final ToggleWorkManagerForNotificationUseCase toggleWorkManagerForNotificationUseCase;

    private final SingleLiveEvent<Void> startGpsSettingsIntent;

    @Inject
    public SettingsViewModel(
        @NonNull IsNotificationEnabledUseCase isNotificationEnabledUseCase,
        @NonNull ToggleWorkManagerForNotificationUseCase toggleWorkManagerForNotificationUseCase
    ) {
        this.isNotificationEnabledUseCase = isNotificationEnabledUseCase;
        this.toggleWorkManagerForNotificationUseCase = toggleWorkManagerForNotificationUseCase;

        startGpsSettingsIntent = new SingleLiveEvent<>();
    }

    public LiveData<Boolean> isNotificationEnabled() {
        return isNotificationEnabledUseCase.invoke();
    }

    public void toggleNotification() {
        toggleWorkManagerForNotificationUseCase.invoke();
    }

    public void startGpsSettingsIntent() {
        startGpsSettingsIntent.call();
    }

    public SingleLiveEvent<Void> getStartGpsSettingsIntentSingleLiveEvent() {
        return startGpsSettingsIntent;
    }
}
