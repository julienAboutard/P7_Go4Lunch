package com.example.go4lunch.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.autocomplete.entity.PredictionEntity;
import com.example.go4lunch.data.autocomplete.entity.PredictionEntityWrapper;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.entity.UserEntity;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.domain.authentification.IsUserLoggedInLiveDataUseCase;
import com.example.go4lunch.domain.authentification.LogoutUserUseCase;
import com.example.go4lunch.domain.autocomplete.GetPredictionsWrapperUseCase;
import com.example.go4lunch.domain.autocomplete.ResetPredictionPlaceIdUseCase;
import com.example.go4lunch.domain.autocomplete.SavePredictionPlaceIdUseCase;
import com.example.go4lunch.domain.chosedrestaurant.GetUserWithRestaurantChoiceEntityLiveDataUseCase;
import com.example.go4lunch.domain.gps.IsGpsEnabledUseCase;
import com.example.go4lunch.domain.location.StartLocationRequestUseCase;
import com.example.go4lunch.domain.user.GetUserEntityUseCase;
import com.example.go4lunch.ui.home.searchview.PredictionViewState;
import com.example.go4lunch.ui.utils.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private final SavePredictionPlaceIdUseCase savePredictionPlaceIdUseCase;

    @NonNull
    private final ResetPredictionPlaceIdUseCase resetPredictionPlaceIdUseCase;

    @NonNull
    private final MutableLiveData<Event<HomeDisplayScreen>> homeDisplayScreenMutableLiveEvent = new MutableLiveData<>();

    @NonNull
    private final LiveData<PredictionEntityWrapper> predictionsLiveData;

    @NonNull
    private final MutableLiveData<String> userQueryMutableLiveData;

    @Inject
    public HomeViewModel(
        @NonNull LogoutUserUseCase logoutUserUseCase,
        @NonNull IsGpsEnabledUseCase isGpsEnabledUseCase,
        @NonNull StartLocationRequestUseCase startLocationRequestUseCase,
        @NonNull IsUserLoggedInLiveDataUseCase isUserLoggedInLiveDataUseCase,
        @NonNull GetUserWithRestaurantChoiceEntityLiveDataUseCase getUserWithRestaurantChoiceEntityLiveDataUseCase,
        @NonNull GetUserEntityUseCase getUserEntityUseCase,
        @NonNull GetPredictionsWrapperUseCase getPredictionsWrapperUseCase,
        @NonNull SavePredictionPlaceIdUseCase savePredictionPlaceIdUseCase,
        @NonNull ResetPredictionPlaceIdUseCase resetPredictionPlaceIdUseCase
        ) {
        this.logoutUserUseCase = logoutUserUseCase;
        this.isGpsEnabledUseCase = isGpsEnabledUseCase;
        this.startLocationRequestUseCase = startLocationRequestUseCase;
        this.isUserLoggedInLiveDataUseCase = isUserLoggedInLiveDataUseCase;
        this.getUserWithRestaurantChoiceEntityLiveDataUseCase = getUserWithRestaurantChoiceEntityLiveDataUseCase;
        this.getUserEntityUseCase = getUserEntityUseCase;
        this.savePredictionPlaceIdUseCase = savePredictionPlaceIdUseCase;
        this.resetPredictionPlaceIdUseCase = resetPredictionPlaceIdUseCase;

        homeDisplayScreenMutableLiveEvent.setValue(new Event<>(HomeDisplayScreen.values()[0]));

        userQueryMutableLiveData = new MutableLiveData<>();

        predictionsLiveData = Transformations.switchMap(
            userQueryMutableLiveData, userQuery -> {
                if (userQuery == null || userQuery.isEmpty() || userQuery.length() < 3) {
                    return null;
                } else {
                    return getPredictionsWrapperUseCase.invoke(userQuery);
                }
            }
        );
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

    public LiveData<List<PredictionViewState>> getPredictionsLiveData() {
        return Transformations.switchMap(predictionsLiveData, predictions -> {
                MutableLiveData<List<PredictionViewState>> predictionViewStateMutableLiveData = new MutableLiveData<>();
                List<PredictionViewState> predictionViewStateList = new ArrayList<>();
                if (predictions instanceof PredictionEntityWrapper.Success) {
                    for (PredictionEntity prediction : ((PredictionEntityWrapper.Success) predictions).getPredictionEntityList()) {
                        predictionViewStateList.add(
                            new PredictionViewState(
                                prediction.getPlaceId(),
                                prediction.getRestaurantName()
                            )
                        );
                    }
                } else {
                    predictionViewStateMutableLiveData.setValue(Collections.emptyList());
                }
                predictionViewStateMutableLiveData.setValue(predictionViewStateList);
                return predictionViewStateMutableLiveData;
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

    public void onQueryChanged(String query) {
        if (query == null || query.isEmpty()) {
            resetPredictionPlaceIdUseCase.invoke();
            userQueryMutableLiveData.setValue(null);
            return;
        }
        userQueryMutableLiveData.setValue(query);
    }

    public void onPredictionClicked(String placeId) {
        savePredictionPlaceIdUseCase.invoke(placeId);
        userQueryMutableLiveData.setValue(null);
    }

    public void onPredictionPlaceIdReset() {
        userQueryMutableLiveData.setValue(null);
        resetPredictionPlaceIdUseCase.invoke();
    }
}
