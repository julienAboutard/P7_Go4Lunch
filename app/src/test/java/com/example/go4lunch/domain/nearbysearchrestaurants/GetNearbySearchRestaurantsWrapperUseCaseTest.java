package com.example.go4lunch.domain.nearbysearchrestaurants;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.nearbysearchrestaurants.NearbySearchRestaurantsRepository;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsWrapper;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetNearbySearchRestaurantsWrapperUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private NearbySearchRestaurantsRepository nearbySearchRestaurantsRepository;

    @Mock
    private GetCurrentLocationUseCase getCurrentLocationUseCase;

    private GetNearbySearchRestaurantsWrapperUseCase getNearbySearchRestaurantsWrapperUseCase;

    @Before
    public void setUp() {
        getNearbySearchRestaurantsWrapperUseCase = new GetNearbySearchRestaurantsWrapperUseCase(nearbySearchRestaurantsRepository, getCurrentLocationUseCase);
    }

    @Test
    public void testInvoke_GpsEnabled() {
        // Given
        LocationEntity location = ((LocationEntityWrapper.GpsProviderEnabled) TestValues.getTestLocationEntityWrapperGpsEnabled()).locationEntity;

        MutableLiveData<NearbySearchRestaurantsWrapper> nearbySearchWrapperMutableLiveData = new MutableLiveData<>();
        NearbySearchRestaurantsWrapper nearbySearchRestaurantsWrapper = mock(NearbySearchRestaurantsWrapper.Success.class);
        nearbySearchWrapperMutableLiveData.setValue(nearbySearchRestaurantsWrapper);
        doReturn(nearbySearchWrapperMutableLiveData)
            .when(nearbySearchRestaurantsRepository)
            .getNearbyRestaurants(
                location.getLatitude(),
                location.getLongitude(),
                TestValues.TEST_RESTAURANT_TYPE,
                TestValues.TEST_RESTAURANT_RADIUS
            );

        MutableLiveData<LocationEntityWrapper> locationStateEntityMutableLiveData = new MutableLiveData<>();
        locationStateEntityMutableLiveData.setValue(TestValues.getTestLocationEntityWrapperGpsEnabled());
        doReturn(locationStateEntityMutableLiveData).when(getCurrentLocationUseCase).invoke();

        // When
        NearbySearchRestaurantsWrapper result = getValueForTesting(getNearbySearchRestaurantsWrapperUseCase.invoke());

        // Then
        assertTrue(result instanceof NearbySearchRestaurantsWrapper.Success);
        verify(getCurrentLocationUseCase).invoke();
        verify(nearbySearchRestaurantsRepository)
            .getNearbyRestaurants(
                location.getLatitude(),
                location.getLongitude(),
                TestValues.TEST_RESTAURANT_TYPE,
                TestValues.TEST_RESTAURANT_RADIUS);
        verifyNoMoreInteractions(getCurrentLocationUseCase, nearbySearchRestaurantsRepository);
    }

    @Test
    public void testInvoke_GpsDisabled() {
        // Given
        MutableLiveData<LocationEntityWrapper> locationStateEntityMutableLiveData = new MutableLiveData<>();
        locationStateEntityMutableLiveData.setValue(TestValues.getTestLocationEntityWrapperGpsDisabled());
        doReturn(locationStateEntityMutableLiveData).when(getCurrentLocationUseCase).invoke();

        // When
        NearbySearchRestaurantsWrapper result = getValueForTesting(getNearbySearchRestaurantsWrapperUseCase.invoke());

        // Then
        assertTrue(result instanceof NearbySearchRestaurantsWrapper.RequestError);
        verify(getCurrentLocationUseCase).invoke();
        verifyNoMoreInteractions(getCurrentLocationUseCase, nearbySearchRestaurantsRepository);
    }
}
