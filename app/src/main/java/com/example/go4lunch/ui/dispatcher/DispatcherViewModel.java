package com.example.go4lunch.ui.dispatcher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.ui.navigation.Destination;
import com.example.go4lunch.ui.utils.Event;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DispatcherViewModel extends ViewModel {

    private final MutableLiveData<Event<Destination>> destinationMutableLiveData = new MutableLiveData<>();

    @Inject
    public DispatcherViewModel(
        AuthRepository authRepository
    ) {
        if (authRepository.getCurrentUser() == null) {
            destinationMutableLiveData.setValue(new Event<>(Destination.LOGIN));
        } else {
            destinationMutableLiveData.setValue(new Event<>(Destination.HOME));
        }
    }

    public LiveData<Event<Destination>> getDestinationLiveData() {
        return destinationMutableLiveData;
    }
}
