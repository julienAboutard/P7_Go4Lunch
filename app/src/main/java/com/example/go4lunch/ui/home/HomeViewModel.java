package com.example.go4lunch.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.ui.utils.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    @NonNull
    private final SingleLiveEvent<FragmentList> fragmentListSingleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public HomeViewModel() {

    }

    @NonNull
    public SingleLiveEvent<FragmentList> getFragmentListSingleLiveEvent() {
        return fragmentListSingleLiveEvent;
    }

    public void onChangeFragmentView(@NonNull FragmentList fragmentList) {
        fragmentListSingleLiveEvent.setValue(fragmentList);
    }
}
