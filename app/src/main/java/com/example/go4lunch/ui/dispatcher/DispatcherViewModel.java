package com.example.go4lunch.ui.dispatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.domain.authentification.IsUserLoggedInLiveDataUseCase;
import com.example.go4lunch.domain.location.StartLocationRequestUseCase;
import com.example.go4lunch.domain.permission.HasGpsPermissionUseCase;
import com.example.go4lunch.ui.navigation.Destination;
import com.example.go4lunch.ui.utils.Event;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DispatcherViewModel extends ViewModel {

    private final MediatorLiveData<Event<Destination>> destinationMutableLiveData = new MediatorLiveData<>();

    @NonNull
    private final StartLocationRequestUseCase startLocationRequestUseCase;

    @Inject
    public DispatcherViewModel(
        @NonNull HasGpsPermissionUseCase hasGpsPermissionUseCase,
        @NonNull IsUserLoggedInLiveDataUseCase isUserLoggedInLiveDataUseCase,
        @NonNull StartLocationRequestUseCase startLocationRequestUseCase
    ) {

        this.startLocationRequestUseCase = startLocationRequestUseCase;

        LiveData<Boolean> permissionLiveData = hasGpsPermissionUseCase.invoke();
        LiveData<Boolean> isUserLoggedInLiveData = isUserLoggedInLiveDataUseCase.invoke();

        destinationMutableLiveData.addSource(permissionLiveData, hasPermission -> combine(hasPermission, isUserLoggedInLiveData.getValue())
        );
        destinationMutableLiveData.addSource(isUserLoggedInLiveData, isUserLoggedIn -> combine(permissionLiveData.getValue(), isUserLoggedIn)
        );
    }

    public LiveData<Event<Destination>> getDestinationLiveData() {
        return destinationMutableLiveData;
    }

    private void combine(
        @Nullable Boolean hasPermission,
        @Nullable Boolean isUserLoggedIn
    ) {
        if (hasPermission == null || isUserLoggedIn == null) {
            return;
        }

        if (hasPermission) {
            startLocationRequestUseCase.invoke();
            if (!isUserLoggedIn) {
                destinationMutableLiveData.setValue(new Event<>(Destination.LOGIN));
            } else {
                destinationMutableLiveData.setValue(new Event<>(Destination.HOME));
            }
        } else {
            destinationMutableLiveData.setValue(new Event<>(Destination.ONBOARDING));
        }
    }
}
