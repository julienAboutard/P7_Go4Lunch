package com.example.go4lunch.ui.restaurant.list;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsEntity;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsWrapper;
import com.example.go4lunch.domain.autocomplete.GetPredictionPlaceIdUseCase;
import com.example.go4lunch.domain.gps.IsGpsEnabledUseCase;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;
import com.example.go4lunch.domain.nearbysearchrestaurants.GetNearbySearchRestaurantsWrapperUseCase;
import com.example.go4lunch.domain.permission.HasGpsPermissionUseCase;
import com.example.go4lunch.domain.workmate.GetAttendantsGoingToSameRestaurantAsUserUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final static String ERROR_MESSAGE = "ERROR_MESSAGE";

    private final static String NO_RESULT = "NO_RESULT";

    @Mock
    private GetNearbySearchRestaurantsWrapperUseCase getNearbySearchRestaurantsWrapperUseCase;

    @Mock
    private GetCurrentLocationUseCase getCurrentLocationUseCase;

    @Mock
    private HasGpsPermissionUseCase hasGpsPermissionUseCase;

    @Mock
    private IsGpsEnabledUseCase isGpsEnabledUseCase;

    @Mock
    private Resources resources;

    @Mock
    private GetPredictionPlaceIdUseCase getPredictionPlaceIdUseCase;

    @Mock
    private GetAttendantsGoingToSameRestaurantAsUserUseCase getAttendantsGoingToSameRestaurantAsUserUseCase;

    private final MutableLiveData<Boolean> isGpsEnabledLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    private final MutableLiveData<NearbySearchRestaurantsWrapper> nearbySearchRestaurantsWrapperMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<LocationEntityWrapper> locationEntityWrapperMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Map<String, Integer>> attendantsGoingToSameRestaurantAsUserLiveData = new MutableLiveData<>();

    MutableLiveData<String> predictionIdLiveData = new MutableLiveData<>();

    private RestaurantListViewModel restaurantListViewModel;

    @Before
    public void setUp() {
        List<NearbySearchRestaurantsEntity> testNearbySearchEntityList = TestValues.getTestNearbySearchEntityList(3);
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.Success(testNearbySearchEntityList));
        doReturn(nearbySearchRestaurantsWrapperMutableLiveData).when(getNearbySearchRestaurantsWrapperUseCase).invoke();

        locationEntityWrapperMutableLiveData.setValue(TestValues.getTestLocationEntityWrapperGpsEnabled());
        doReturn(locationEntityWrapperMutableLiveData).when(getCurrentLocationUseCase).invoke();

        hasGpsPermissionLiveData.setValue(true);
        doReturn(hasGpsPermissionLiveData).when(hasGpsPermissionUseCase).invoke();

        isGpsEnabledLiveData.setValue(true);
        doReturn(isGpsEnabledLiveData).when(isGpsEnabledUseCase).invoke();

        doReturn(ERROR_MESSAGE).when(resources).getString(R.string.list_error_message_generic);
        doReturn(NO_RESULT).when(resources).getString(R.string.list_error_message_no_results);
        doReturn(ERROR_MESSAGE).when(resources).getString(R.string.list_error_message_no_gps);

        Map<String, Integer> attendantsByRestaurantIdsMap = new HashMap<>();
        attendantsByRestaurantIdsMap.put(TestValues.TEST_NEARBYSEARCH_ID + "1", 2);
        attendantsByRestaurantIdsMap.put("restaurantId2", 1);
        attendantsGoingToSameRestaurantAsUserLiveData.setValue(attendantsByRestaurantIdsMap);
        doReturn(attendantsGoingToSameRestaurantAsUserLiveData).when(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();

        predictionIdLiveData.setValue(null);
        doReturn(predictionIdLiveData).when(getPredictionPlaceIdUseCase).invoke();

        restaurantListViewModel = new RestaurantListViewModel(
            getNearbySearchRestaurantsWrapperUseCase,
            getCurrentLocationUseCase,
            hasGpsPermissionUseCase,
            isGpsEnabledUseCase,
            getAttendantsGoingToSameRestaurantAsUserUseCase,
            resources,
            getPredictionPlaceIdUseCase
        );
    }

    @Test
    public void nominal_case() {
        // When
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertTrue(expectedViewStateItemList.get(0) instanceof RestaurantListViewStateItem.RestaurantListItem);
        assertEquals(3, expectedViewStateItemList.size());
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verify(getCurrentLocationUseCase).invoke();
        verify(hasGpsPermissionUseCase).invoke();
        verify(isGpsEnabledUseCase).invoke();
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verifyNoMoreInteractions(
            getNearbySearchRestaurantsWrapperUseCase,
            getCurrentLocationUseCase,
            hasGpsPermissionUseCase,
            isGpsEnabledUseCase,
            getAttendantsGoingToSameRestaurantAsUserUseCase
        );
    }

    @Test
    public void nearbySearchWrapperIsNull_shouldReturn() {
        // Given
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(null);

        // When
        List<RestaurantListViewStateItem> result = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertNull(result);
    }

    @Test
    public void locationStateIsNull_shouldReturn() {
        // Given
        locationEntityWrapperMutableLiveData.setValue(null);

        // When
        List<RestaurantListViewStateItem> result = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertNull(result);
    }

    @Test
    public void noGpsPermission_shouldReturnRestaurantListErrorItem() {
        // Given
        hasGpsPermissionLiveData.setValue(false);

        // When
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertTrue(expectedViewStateItemList.get(0) instanceof RestaurantListViewStateItem.RestaurantListErrorItem);
        assertEquals(ERROR_MESSAGE, ((RestaurantListViewStateItem.RestaurantListErrorItem) expectedViewStateItemList.get(0)).getErrorMessage());
        verify(hasGpsPermissionUseCase).invoke();
        verifyNoMoreInteractions(hasGpsPermissionUseCase);
    }

    @Test
    public void gpsProviderDisabled_shouldReturnRestaurantListErrorItem() {
        // Given
        locationEntityWrapperMutableLiveData.setValue(TestValues.getTestLocationEntityWrapperGpsDisabled());

        // When
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertTrue(expectedViewStateItemList.get(0) instanceof RestaurantListViewStateItem.RestaurantListErrorItem);
        assertEquals(
            ERROR_MESSAGE,
            ((RestaurantListViewStateItem.RestaurantListErrorItem) expectedViewStateItemList.get(0)).getErrorMessage()
        );
        verify(getCurrentLocationUseCase).invoke();
        verifyNoMoreInteractions(getCurrentLocationUseCase);
    }

    @Test
    public void gpsDisabled_shouldReturnRestaurantListErrorItem() {
        // Given
        isGpsEnabledLiveData.setValue(false);

        // When
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertTrue(expectedViewStateItemList.get(0) instanceof RestaurantListViewStateItem.RestaurantListErrorItem);
        assertEquals(
            ERROR_MESSAGE,
            ((RestaurantListViewStateItem.RestaurantListErrorItem) expectedViewStateItemList.get(0)).getErrorMessage()
        );
        verify(isGpsEnabledUseCase).invoke();
        verifyNoMoreInteractions(isGpsEnabledUseCase);
    }

    @Test
    public void nearbySearchLoadingState_shouldReturnLoading() {
        // Given
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.Loading());

        // When
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertTrue(expectedViewStateItemList.get(0) instanceof RestaurantListViewStateItem.Loading);
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verifyNoMoreInteractions(getNearbySearchRestaurantsWrapperUseCase);
    }

    @Test
    public void nearbySearchNoResults_shouldReturnLoading() {
        // Given
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.NoResults());

        // When
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertTrue(expectedViewStateItemList.get(0) instanceof RestaurantListViewStateItem.RestaurantListErrorItem);
        assertEquals(NO_RESULT, ((RestaurantListViewStateItem.RestaurantListErrorItem) expectedViewStateItemList.get(0)).getErrorMessage());
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verifyNoMoreInteractions(getNearbySearchRestaurantsWrapperUseCase);
    }

    @Test
    public void nearbySearchRequestError_shouldReturnLoading() {
        // Given
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.RequestError(new Exception(ERROR_MESSAGE)));

        // When
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertTrue(expectedViewStateItemList.get(0) instanceof RestaurantListViewStateItem.RestaurantListErrorItem);
        assertEquals(
            ERROR_MESSAGE,
            ((RestaurantListViewStateItem.RestaurantListErrorItem) expectedViewStateItemList.get(0)).getErrorMessage()
        );
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verifyNoMoreInteractions(getNearbySearchRestaurantsWrapperUseCase);
    }

    @Test
    public void placeIdIsNonNull_shouldReturnMatchingRestaurantViewState() {
        // Given
        predictionIdLiveData.setValue(TestValues.TEST_PREDICTION_ID + 0);

        // When
        List<RestaurantListViewStateItem> result = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertEquals(1, result.size());
        assertEquals(
            TestValues.getPredictionEntityList().get(0).getPlaceId(),
            ((RestaurantListViewStateItem.RestaurantListItem) result.get(0)).getId()
        );
    }

    @Test
    public void openingState_shouldReturnIsOpened() {
        // When
        NearbySearchRestaurantsEntity nearbySearchRestaurantsEntity = new NearbySearchRestaurantsEntity(
            TestValues.TEST_NEARBYSEARCH_ID + 0,
            TestValues.TEST_NEARBYSEARCH_NAME,
            TestValues.TEST_RESTAURANT_VICINITY,
            TestValues.TEST_RESTAURANT_PHOTO_URL,
            TestValues.TEST_NEARBYSEARCH_RATING,
            TestValues.TEST_NEARBYSEARCH_LOCATION_ENTITY,
            TestValues.TEST_NEARBYSEARCH_DISTANCE,
            false
        );
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(
            new NearbySearchRestaurantsWrapper.Success(Collections.singletonList(nearbySearchRestaurantsEntity))
        );
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertEquals(
            RestaurantOpeningState.IS_CLOSED,
            ((RestaurantListViewStateItem.RestaurantListItem) expectedViewStateItemList.get(0)).getRestaurantOpeningState()
        );
        assertEquals(1, expectedViewStateItemList.size());
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verifyNoMoreInteractions(getAttendantsGoingToSameRestaurantAsUserUseCase);
    }

    @Test
    public void openingStateNotDefined_shouldReturnNotDefined() {
        // When
        NearbySearchRestaurantsEntity nearbySearchRestaurantsEntity = new NearbySearchRestaurantsEntity(
            TestValues.TEST_NEARBYSEARCH_ID + 0,
            TestValues.TEST_NEARBYSEARCH_NAME,
            TestValues.TEST_RESTAURANT_VICINITY,
            TestValues.TEST_RESTAURANT_PHOTO_URL,
            TestValues.TEST_NEARBYSEARCH_RATING,
            TestValues.TEST_NEARBYSEARCH_LOCATION_ENTITY,
            TestValues.TEST_NEARBYSEARCH_DISTANCE,
            null
        );
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(
            new NearbySearchRestaurantsWrapper.Success(Collections.singletonList(nearbySearchRestaurantsEntity))
        );
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertEquals(
            RestaurantOpeningState.IS_NOT_DEFINED,
            ((RestaurantListViewStateItem.RestaurantListItem) expectedViewStateItemList.get(0)).getRestaurantOpeningState()
        );
        assertEquals(1, expectedViewStateItemList.size());
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verifyNoMoreInteractions(getAttendantsGoingToSameRestaurantAsUserUseCase);
    }

    @Test
    public void restaurantWith0rating_shouldReturn0() {
        // When
        NearbySearchRestaurantsEntity nearbySearchRestaurantsEntity = new NearbySearchRestaurantsEntity(
            TestValues.TEST_NEARBYSEARCH_ID + 0,
            TestValues.TEST_NEARBYSEARCH_NAME,
            TestValues.TEST_RESTAURANT_VICINITY,
            TestValues.TEST_RESTAURANT_PHOTO_URL,
            null,
            TestValues.TEST_NEARBYSEARCH_LOCATION_ENTITY,
            TestValues.TEST_NEARBYSEARCH_DISTANCE,
            true
        );
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(
            new NearbySearchRestaurantsWrapper.Success(Collections.singletonList(nearbySearchRestaurantsEntity))
        );
        List<RestaurantListViewStateItem> expectedViewStateItemList = getValueForTesting(restaurantListViewModel.getRestaurants());

        // Then
        assertEquals(0f, ((RestaurantListViewStateItem.RestaurantListItem) expectedViewStateItemList.get(0)).getRating(), 0.0);
        assertEquals(1, expectedViewStateItemList.size());
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verifyNoMoreInteractions(getAttendantsGoingToSameRestaurantAsUserUseCase);
    }
}
