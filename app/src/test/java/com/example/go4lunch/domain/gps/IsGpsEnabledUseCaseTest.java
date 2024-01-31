package com.example.go4lunch.domain.gps;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.gps.location.GpsLocationRepository;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsGpsEnabledUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    public GpsLocationRepository gpsLocationRepository;

    public IsGpsEnabledUseCase isGpsEnabledUseCase;

    @Before
    public void setUp() {
        isGpsEnabledUseCase = new IsGpsEnabledUseCase(gpsLocationRepository);
    }

    @Test
    public void gpsProviderIsEnabled() {
        //Given
        MutableLiveData<LocationEntityWrapper> locationEntityWrapperMutableLiveData = new MutableLiveData<>();
        locationEntityWrapperMutableLiveData.setValue(TestValues.getTestLocationEntityWrapperGpsEnabled());
        doReturn(locationEntityWrapperMutableLiveData).when(gpsLocationRepository).getLocationStateLiveData();

        //When
        Boolean result = getValueForTesting(isGpsEnabledUseCase.invoke());

        //Then
        assertTrue(result);
        verify(gpsLocationRepository).getLocationStateLiveData();
        verifyNoMoreInteractions(gpsLocationRepository);
    }

    @Test
    public void gpsProviderIsDisabled() {
        //Given
        MutableLiveData<LocationEntityWrapper> locationEntityWrapperMutableLiveData = new MutableLiveData<>();
        locationEntityWrapperMutableLiveData.setValue(TestValues.getTestLocationEntityWrapperGpsDisabled());
        doReturn(locationEntityWrapperMutableLiveData).when(gpsLocationRepository).getLocationStateLiveData();

        //When
        Boolean result = getValueForTesting(isGpsEnabledUseCase.invoke());

        //Then
        assertFalse(result);
        verify(gpsLocationRepository).getLocationStateLiveData();
        verifyNoMoreInteractions(gpsLocationRepository);
    }
}
