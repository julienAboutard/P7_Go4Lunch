package com.example.go4lunch.ui.home;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

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
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class HomeViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private LogoutUserUseCase logoutUserUseCase;

    @Mock
    private IsGpsEnabledUseCase isGpsEnabledUseCase;

    @Mock
    private StartLocationRequestUseCase startLocationRequestUseCase;

    @Mock
    private IsUserLoggedInLiveDataUseCase isUserLoggedInLiveDataUseCase;

    @Mock
    private GetPredictionsWrapperUseCase getPredictionsWrapperUseCase;

    @Mock
    private GetUserWithRestaurantChoiceEntityLiveDataUseCase getUserWithRestaurantChoiceEntityLiveDataUseCase;


    @Mock
    private SavePredictionPlaceIdUseCase savePredictionPlaceIdUseCase;

    @Mock
    private ResetPredictionPlaceIdUseCase resetPredictionPlaceIdUseCase;
    @Mock
    private GetUserEntityUseCase getUserEntityUseCase;

    private final MutableLiveData<Boolean> isUserLoggedInMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isGpsEnabledMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<UserEntity> currentUserEntityMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntityMutableLiveData = new MutableLiveData<>();

    private HomeViewModel homeViewModel;

    @Before
    public void setUp() {
        doReturn(isUserLoggedInMutableLiveData).when(isUserLoggedInLiveDataUseCase).invoke();


        doReturn(isGpsEnabledMutableLiveData).when(isGpsEnabledUseCase).invoke();

        UserEntity userEntity = TestValues.getCurrentUserEntity();
        currentUserEntityMutableLiveData.setValue(userEntity);
        doReturn(currentUserEntityMutableLiveData).when(getUserEntityUseCase).invoke();

        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity = TestValues.getTestCurrentUserWithRestaurantChoiceEntity();
        userWithRestaurantChoiceEntityMutableLiveData.setValue(userWithRestaurantChoiceEntity);
        doReturn(userWithRestaurantChoiceEntityMutableLiveData).when(getUserWithRestaurantChoiceEntityLiveDataUseCase).invoke();

        List<PredictionEntity> predictionEntities = TestValues.getPredictionEntityList();
        MutableLiveData<PredictionEntityWrapper> predictionEntityWrapperMutableLiveData = new MutableLiveData<>();
        predictionEntityWrapperMutableLiveData.setValue(new PredictionEntityWrapper.Success(predictionEntities));
        doReturn(predictionEntityWrapperMutableLiveData).when(getPredictionsWrapperUseCase).invoke("TEST");

        homeViewModel = new HomeViewModel(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase,
            getPredictionsWrapperUseCase,
            savePredictionPlaceIdUseCase,
            resetPredictionPlaceIdUseCase
        );
    }

    @Test
    public void getUserInfo() {
        // When
        LoggedUserEntity result = getValueForTesting(homeViewModel.getUserInfoLiveData());

        // Then
        assertEquals(TestValues.getTestLoggedUserEntity(), result);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }

    @Test
    public void onUserLogged_isLoggedIn() {
        // Given
        isUserLoggedInMutableLiveData.setValue(true);

        // When
        boolean result = getValueForTesting(homeViewModel.onUserLogged());

        // Then
        assertTrue(result);
    }

    @Test
    public void onUserLogged_isLoggedOut() {
        // Given
        isUserLoggedInMutableLiveData.setValue(false);

        // When
        boolean result = getValueForTesting(homeViewModel.onUserLogged());

        // Then
        assertFalse(result);
    }


    @Test
    public void getUserWithRestaurantChoice() {
        // When
        UserWithRestaurantChoiceEntity result = getValueForTesting(homeViewModel.getUserWithRestaurantChoice());

        assertEquals(TestValues.getTestCurrentUserWithRestaurantChoiceEntity(), result);
        assertEquals(TestValues.TEST_USER_ID, result.getId());
        assertEquals(TestValues.ATTENDING_RESTAURANT_ID, result.getAttendingRestaurantId());
        verify(getUserWithRestaurantChoiceEntityLiveDataUseCase).invoke();
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }

    @Test
    public void gpsIsEnabled() {
        // Given
        isGpsEnabledMutableLiveData.setValue(true);

        // When
        boolean result = getValueForTesting(homeViewModel.isGpsEnabledLiveData());

        // Then
        assertTrue(result);
        verify(isGpsEnabledUseCase).invoke();
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }


    @Test
    public void gpsIsDisabled() {
        // Given
        isGpsEnabledMutableLiveData.setValue(false);

        // When
        boolean result = getValueForTesting(homeViewModel.isGpsEnabledLiveData());

        // Then
        assertFalse(result);
        verify(isGpsEnabledUseCase).invoke();
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }


    @Test
    public void testSignOut() {
        // When
        homeViewModel.signOut();

        // Then
        verify(logoutUserUseCase).invoke();
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }

    @Test
    public void onGetFragmentState_initialState() {
        // When
        Event<HomeDisplayScreen> result = getValueForTesting(homeViewModel.getHomeDisplayScreenLiveEvent());

        // Then
        assertEquals(HomeDisplayScreen.MAP_FRAGMENT, result.getContentIfNotHandled());
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }

    @Test
    public void onGetFragmentState_listFragment() {
        // Given
        homeViewModel.onChangeFragmentView(HomeDisplayScreen.LIST_FRAGMENT);
        // When
        Event<HomeDisplayScreen> result = getValueForTesting(homeViewModel.getHomeDisplayScreenLiveEvent());

        // Then
        assertEquals(HomeDisplayScreen.LIST_FRAGMENT, result.getContentIfNotHandled());
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase,
            savePredictionPlaceIdUseCase
        );
    }

    @Test
    public void getPredictionList_withQuery() {
        // Given
        homeViewModel.onQueryChanged("TEST");

        // When
        List<PredictionViewState> result = getValueForTesting(homeViewModel.getPredictionsLiveData());
        int expectedSize = TestValues.getPredictionEntityList().size();

        // Then
        assertEquals(expectedSize, result.size());
        verify(getPredictionsWrapperUseCase).invoke("TEST");
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase,
            savePredictionPlaceIdUseCase
        );
    }

    @Test
    public void getPredictionList_withQueryLengthLowerThan2() {
        // Given
        homeViewModel.onQueryChanged("TE");

        // When
        List<PredictionViewState> result = getValueForTesting(homeViewModel.getPredictionsLiveData());

        // Then
        assertNull(result);
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase,
            savePredictionPlaceIdUseCase
        );
    }

    @Test
    public void getPredictionList_withNullQuery() {
        // Given
        homeViewModel.onQueryChanged(null);

        // When
        List<PredictionViewState> result = getValueForTesting(homeViewModel.getPredictionsLiveData());

        // Then
        assertNull(result);
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase,
            savePredictionPlaceIdUseCase
        );
    }

    @Test
    public void onNullQuery_shouldResetPredictionPlaceId() {
        // Given
        homeViewModel.onQueryChanged(null);

        // When
        List<PredictionViewState> result = getValueForTesting(homeViewModel.getPredictionsLiveData());

        // Then
        assertNull(result);
        verify(resetPredictionPlaceIdUseCase).invoke();
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            savePredictionPlaceIdUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }

    @Test
    public void onEmptyQuery_shouldResetPredictionPlaceId() {
        // Given
        homeViewModel.onQueryChanged("");

        // When
        List<PredictionViewState> result = getValueForTesting(homeViewModel.getPredictionsLiveData());

        // Then
        assertNull(result);
        verify(resetPredictionPlaceIdUseCase).invoke();
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            savePredictionPlaceIdUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }

    @Test
    public void onGetFragmentState_workmatesFragment() {
        // Given
        homeViewModel.onChangeFragmentView(HomeDisplayScreen.WORKMATES_FRAGMENT);
        // When
        Event<HomeDisplayScreen> result = getValueForTesting(homeViewModel.getHomeDisplayScreenLiveEvent());

        // Then
        assertEquals(HomeDisplayScreen.WORKMATES_FRAGMENT, result.getContentIfNotHandled());
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }

    @Test
    public void onResume() {
        // When
        homeViewModel.onResume();

        // Then
        verify(startLocationRequestUseCase).invoke();
        verifyNoMoreInteractions(
            logoutUserUseCase,
            isGpsEnabledUseCase,
            startLocationRequestUseCase,
            isUserLoggedInLiveDataUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            getUserEntityUseCase
        );
    }
}
