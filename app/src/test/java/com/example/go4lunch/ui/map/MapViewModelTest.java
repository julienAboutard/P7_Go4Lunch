package com.example.go4lunch.ui.map;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsWrapper;
import com.example.go4lunch.domain.autocomplete.GetPredictionPlaceIdUseCase;
import com.example.go4lunch.domain.gps.IsGpsEnabledUseCase;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;
import com.example.go4lunch.domain.nearbysearchrestaurants.GetNearbySearchRestaurantsWrapperUseCase;
import com.example.go4lunch.domain.workmate.GetAttendantsGoingToSameRestaurantAsUserUseCase;
import com.example.go4lunch.ui.map.marker.RestaurantMarkerViewStateItem;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class MapViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private IsGpsEnabledUseCase isGpsEnabledUseCase;
    @Mock
    private GetCurrentLocationUseCase getCurrentLocationUseCase;

    @Mock
    private GetNearbySearchRestaurantsWrapperUseCase getNearbySearchRestaurantsWrapperUseCase;

    @Mock
    private GetAttendantsGoingToSameRestaurantAsUserUseCase getAttendantsGoingToSameRestaurantAsUserUseCase;

    @Mock
    private GetPredictionPlaceIdUseCase getPredictionPlaceIdUseCase;

    private final MutableLiveData<NearbySearchRestaurantsWrapper> nearbySearchRestaurantsWrapperMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<LocationEntityWrapper> locationEntityWrapperMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Map<String, Integer>> restaurantIdWithAttendantsMapMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isGpsEnabledMutableLiveData = new MutableLiveData<>();

    MutableLiveData<String> predictionIdLiveData = new MutableLiveData<>();

    private MapViewModel mapViewModel;

    @Before
    public void setUp() {
        NearbySearchRestaurantsWrapper nearbySearchRestaurantsWrapper = new NearbySearchRestaurantsWrapper.Success(
            TestValues.getTestNearbySearchEntityList(4)
        );
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(nearbySearchRestaurantsWrapper);
        doReturn(nearbySearchRestaurantsWrapperMutableLiveData).when(getNearbySearchRestaurantsWrapperUseCase).invoke();

        LocationEntityWrapper testLocationEntityWrapperGpsEnabled = TestValues.getTestLocationEntityWrapperGpsEnabled();
        locationEntityWrapperMutableLiveData.setValue(testLocationEntityWrapperGpsEnabled);
        doReturn(locationEntityWrapperMutableLiveData).when(getCurrentLocationUseCase).invoke();

        isGpsEnabledMutableLiveData.setValue(true);
        doReturn(isGpsEnabledMutableLiveData).when(isGpsEnabledUseCase).invoke();

        Map<String, Integer> restaurantIdWithAttendantsMap = new HashMap<>();
        restaurantIdWithAttendantsMap.put(TestValues.TEST_NEARBYSEARCH_ID + 0, 5);
        restaurantIdWithAttendantsMap.put(TestValues.TEST_NEARBYSEARCH_ID + 1, 3);
        restaurantIdWithAttendantsMap.put(TestValues.TEST_NEARBYSEARCH_ID + 2, 1);
        restaurantIdWithAttendantsMapMutableLiveData.setValue(restaurantIdWithAttendantsMap);
        doReturn(restaurantIdWithAttendantsMapMutableLiveData).when(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();


        predictionIdLiveData.setValue(null);
        doReturn(predictionIdLiveData).when(getPredictionPlaceIdUseCase).invoke();

        mapViewModel = new MapViewModel(
            isGpsEnabledUseCase,
            getCurrentLocationUseCase,
            getNearbySearchRestaurantsWrapperUseCase,
            getAttendantsGoingToSameRestaurantAsUserUseCase,
            getPredictionPlaceIdUseCase
        );
    }

    @Test
    public void nominal_case() {
        // When
        List<RestaurantMarkerViewStateItem> result = getValueForTesting(mapViewModel.getMapViewState());

        // Then
        assertEquals(4, result.size());
        assertEquals(TestValues.TEST_NEARBYSEARCH_ID + 0, result.get(0).getId());
        assertEquals(TestValues.TEST_NEARBYSEARCH_ID + 1, result.get(1).getId());
        assertEquals(TestValues.TEST_NEARBYSEARCH_ID + 2, result.get(2).getId());
        assertEquals(TestValues.TEST_NEARBYSEARCH_ID + 3, result.get(3).getId());

        verify(getCurrentLocationUseCase).invoke();
        verify(isGpsEnabledUseCase).invoke();
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verifyNoMoreInteractions(
            getCurrentLocationUseCase,
            isGpsEnabledUseCase,
            getNearbySearchRestaurantsWrapperUseCase,
            getAttendantsGoingToSameRestaurantAsUserUseCase
        );
    }

    @Test
    public void nearbySearchWrapperLoading_shouldReturnNoRestaurantMarkerViewState() {
        // Given
        NearbySearchRestaurantsWrapper nearbySearchWrapper = new NearbySearchRestaurantsWrapper.Loading();
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(nearbySearchWrapper);

        // When
        List<RestaurantMarkerViewStateItem> result = getValueForTesting(mapViewModel.getMapViewState());

        // Then
        assertNull(result);
        verify(getCurrentLocationUseCase).invoke();
        verify(isGpsEnabledUseCase).invoke();
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verifyNoMoreInteractions(getCurrentLocationUseCase, isGpsEnabledUseCase, getNearbySearchRestaurantsWrapperUseCase, getAttendantsGoingToSameRestaurantAsUserUseCase);
    }

    @Test
    public void gpsEnabledNull_shouldReturn() {
        // Given
        isGpsEnabledMutableLiveData.setValue(null);

        // When
        List<RestaurantMarkerViewStateItem> result = getValueForTesting(mapViewModel.getMapViewState());

        // Then
        assertNull(result);
    }

    @Test
    public void nearbySearchWrapperNull_shouldReturn() {
        // Given
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(null);

        // When
        List<RestaurantMarkerViewStateItem> result = getValueForTesting(mapViewModel.getMapViewState());

        // Then
        assertNull(result);
    }

    @Test
    public void locationStateEntitySuccess_shouldReturnSuccess() {
        // When
        LocationEntityWrapper result = getValueForTesting(mapViewModel.getLocationState());

        // Then
        assertTrue(result instanceof LocationEntityWrapper.GpsProviderEnabled);
    }

    @Test
    public void locationStateEntityGpsProviderDisabled_shouldReturnGpsProviderDisabled() {
        // Given
        LocationEntityWrapper locationStateEntity = new LocationEntityWrapper.GpsProviderDisabled();
        locationEntityWrapperMutableLiveData.setValue(locationStateEntity);

        // When
        LocationEntityWrapper result = getValueForTesting(mapViewModel.getLocationState());

        // Then
        assertTrue(result instanceof LocationEntityWrapper.GpsProviderDisabled);
    }

    @Test
    public void predictionPlaceIdIsNotNull_shouldReturnMatchingMarkerViewState() {
        // Given
        predictionIdLiveData.setValue(TestValues.TEST_PREDICTION_ID + 0);

        // When
        List<RestaurantMarkerViewStateItem> result = getValueForTesting(mapViewModel.getMapViewState());

        // Then
        assertEquals(1, result.size());
        assertEquals(TestValues.TEST_NEARBYSEARCH_ID + 0, result.get(0).getId());
        verify(getCurrentLocationUseCase).invoke();
        verify(isGpsEnabledUseCase).invoke();
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verify(getPredictionPlaceIdUseCase).invoke();
        verifyNoMoreInteractions(
            getCurrentLocationUseCase,
            isGpsEnabledUseCase,
            getNearbySearchRestaurantsWrapperUseCase,
            getAttendantsGoingToSameRestaurantAsUserUseCase,
            getPredictionPlaceIdUseCase
        );
    }

    @Test
    public void nearbySearchWrapperNoResult_shouldTriggerNoRestaurantFoundSingleLiveEvent() {
        // Given
        nearbySearchRestaurantsWrapperMutableLiveData.setValue(new NearbySearchRestaurantsWrapper.NoResults());
        // When
        List<RestaurantMarkerViewStateItem> result = getValueForTesting(mapViewModel.getMapViewState());

        // Then
        assertEquals(0, result.size());
        verify(getCurrentLocationUseCase).invoke();
        verify(isGpsEnabledUseCase).invoke();
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verify(getPredictionPlaceIdUseCase).invoke();
        verifyNoMoreInteractions(
            getCurrentLocationUseCase,
            isGpsEnabledUseCase,
            getNearbySearchRestaurantsWrapperUseCase,
            getAttendantsGoingToSameRestaurantAsUserUseCase,
            getPredictionPlaceIdUseCase
        );
    }

    @Test
    public void predictionPlaceIdNotMatching_shouldTriggerNoRestaurantMatchSingleLiveEvent() {
        // Given
        predictionIdLiveData.setValue(TestValues.TEST_PREDICTION_ID + 123456789);
        // When
        List<RestaurantMarkerViewStateItem> result = getValueForTesting(mapViewModel.getMapViewState());

        // Then
        assertEquals(0, result.size());
        verify(getCurrentLocationUseCase).invoke();
        verify(isGpsEnabledUseCase).invoke();
        verify(getNearbySearchRestaurantsWrapperUseCase).invoke();
        verify(getAttendantsGoingToSameRestaurantAsUserUseCase).invoke();
        verify(getPredictionPlaceIdUseCase).invoke();
        verifyNoMoreInteractions(
            getCurrentLocationUseCase,
            isGpsEnabledUseCase,
            getNearbySearchRestaurantsWrapperUseCase,
            getAttendantsGoingToSameRestaurantAsUserUseCase,
            getPredictionPlaceIdUseCase
        );
    }
}
