package com.example.go4lunch.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.ui.utils.Event;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    @NonNull
    private final MutableLiveData<Event<HomeDisplayScreen>> homeDisplayScreenMutableLiveEvent = new MutableLiveData<>();

    @Inject
    public HomeViewModel() {
        homeDisplayScreenMutableLiveEvent.setValue(new Event<>(HomeDisplayScreen.values()[0]));
    }

    @NonNull
    public LiveData<Event<HomeDisplayScreen>> getHomeDisplayScreenLiveEvent() {
        return homeDisplayScreenMutableLiveEvent;
    }

    public void onChangeFragmentView(@NonNull HomeDisplayScreen homeDisplayScreen) {
        homeDisplayScreenMutableLiveEvent.setValue(new Event<>(homeDisplayScreen));
    }
}
