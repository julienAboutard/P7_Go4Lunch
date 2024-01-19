package com.example.go4lunch.ui.home;

import static com.example.go4lunch.ui.home.HomeDisplayScreen.LIST_FRAGMENT;
import static com.example.go4lunch.ui.home.HomeDisplayScreen.MAP_FRAGMENT;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.entity.UserEntity;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;
import com.example.go4lunch.domain.authentification.IsUserLoggedInLiveDataUseCase;
import com.example.go4lunch.domain.authentification.LogoutUserUseCase;
import com.example.go4lunch.domain.chosedrestaurant.GetUserWithRestaurantChoiceEntityLiveDataUseCase;
import com.example.go4lunch.domain.gps.IsGpsEnabledUseCase;
import com.example.go4lunch.domain.location.StartLocationRequestUseCase;
import com.example.go4lunch.domain.user.GetUserEntityUseCase;
import com.example.go4lunch.ui.utils.Event;
import com.example.go4lunch.ui.utils.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    @NonNull
    private final LogoutUserUseCase logoutUserUseCase;

    @NonNull
    private final IsUserLoggedInLiveDataUseCase isUserLoggedInLiveDataUseCase;

    @NonNull
    private final IsGpsEnabledUseCase isGpsEnabledUseCase;

    @NonNull
    private final StartLocationRequestUseCase startLocationRequestUseCase;

    @NonNull
    private final GetUserWithRestaurantChoiceEntityLiveDataUseCase getUserWithRestaurantChoiceEntityLiveDataUseCase;

    @NonNull
    private final GetUserEntityUseCase getUserEntityUseCase;

    @NonNull
    private final MutableLiveData<Event<HomeDisplayScreen>> homeDisplayScreenMutableLiveEvent = new MutableLiveData<>();

    @Inject
    public HomeViewModel(
        @NonNull LogoutUserUseCase logoutUserUseCase,
        @NonNull IsGpsEnabledUseCase isGpsEnabledUseCase,
        @NonNull StartLocationRequestUseCase startLocationRequestUseCase,
        @NonNull IsUserLoggedInLiveDataUseCase isUserLoggedInLiveDataUseCase,
        @NonNull GetUserWithRestaurantChoiceEntityLiveDataUseCase getUserWithRestaurantChoiceEntityLiveDataUseCase,
        @NonNull GetUserEntityUseCase getUserEntityUseCase
        ) {
        this.logoutUserUseCase = logoutUserUseCase;
        this.isGpsEnabledUseCase = isGpsEnabledUseCase;
        this.startLocationRequestUseCase = startLocationRequestUseCase;
        this.isUserLoggedInLiveDataUseCase = isUserLoggedInLiveDataUseCase;
        this.getUserWithRestaurantChoiceEntityLiveDataUseCase = getUserWithRestaurantChoiceEntityLiveDataUseCase;
        this.getUserEntityUseCase = getUserEntityUseCase;
        homeDisplayScreenMutableLiveEvent.setValue(new Event<>(HomeDisplayScreen.values()[0]));
    }

    @NonNull
    public LiveData<Event<HomeDisplayScreen>> getHomeDisplayScreenLiveEvent() {
        return homeDisplayScreenMutableLiveEvent;
    }

    public LiveData<LoggedUserEntity> getUserInfoLiveData() {
        LiveData<UserEntity> userEntityLiveData = getUserEntityUseCase.invoke();
        return Transformations.switchMap(userEntityLiveData, userEntity -> {
                MutableLiveData<LoggedUserEntity> loggedUserEntityMutableLiveData = new MutableLiveData<>();
                LoggedUserEntity currentUser = userEntity.getLoggedUserEntity();
                loggedUserEntityMutableLiveData.setValue(
                    new LoggedUserEntity(
                        currentUser.getId(),
                        currentUser.getName(),
                        currentUser.getEmail(),
                        currentUser.getPictureUrl()
                    )
                );
                return loggedUserEntityMutableLiveData;
            }
        );
    }

    public LiveData<Boolean> isGpsEnabledLiveData() {
        return isGpsEnabledUseCase.invoke();
    }

    public LiveData<UserWithRestaurantChoiceEntity> getUserWithRestaurantChoice() {
        return getUserWithRestaurantChoiceEntityLiveDataUseCase.invoke();
    }
    public LiveData<Boolean> onUserLogged() {
        LiveData<Boolean> isUserLoggedInLiveData = isUserLoggedInLiveDataUseCase.invoke();
        return Transformations.switchMap(isUserLoggedInLiveData, isLogged -> {
                MutableLiveData<Boolean> isUserLoggedInMutableLiveData = new MutableLiveData<>();
                if (isLogged) {
                    isUserLoggedInMutableLiveData.setValue(true);
                } else {
                    isUserLoggedInMutableLiveData.setValue(false);
                }
                return isUserLoggedInMutableLiveData;
            }
        );
    }

    public void signOut() {
        logoutUserUseCase.invoke();
    }

    public void onResume() {
        startLocationRequestUseCase.invoke();
    }

    public void onChangeFragmentView(@NonNull HomeDisplayScreen homeDisplayScreen) {
        homeDisplayScreenMutableLiveEvent.setValue(new Event<>(homeDisplayScreen));
    }
}
