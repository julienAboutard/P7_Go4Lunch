package com.example.go4lunch.domain.autocomplete;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.autocomplete.PredictionsRepository;
import com.example.go4lunch.data.autocomplete.entity.PredictionEntity;
import com.example.go4lunch.data.autocomplete.entity.PredictionEntityWrapper;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.domain.location.GetCurrentLocationUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetPredictionsWrapperUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final int RADIUS = 1_000;
    private static final String TYPES = "restaurant";

    @Mock
    private PredictionsRepository predictionsRepository;

    @Mock
    private GetCurrentLocationUseCase getCurrentLocationUseCase;

    private GetPredictionsWrapperUseCase getPredictionsWrapperUseCase;

    @Before
    public void setUp() {
        getPredictionsWrapperUseCase = new GetPredictionsWrapperUseCase(predictionsRepository, getCurrentLocationUseCase);
    }

    @Test
    public void testInvoke() {
        // Given
        LocationEntityWrapper locationEntityWrapper = TestValues.getTestLocationEntityWrapperGpsEnabled();
        MutableLiveData<LocationEntityWrapper> locationEntityWrapperMutableLiveData = new MutableLiveData<>();
        locationEntityWrapperMutableLiveData.setValue(locationEntityWrapper);
        doReturn(locationEntityWrapperMutableLiveData).when(getCurrentLocationUseCase).invoke();

        List<PredictionEntity> predictionEntities = TestValues.getPredictionEntityList();
        MutableLiveData<PredictionEntityWrapper> predictionEntitiesLiveData = new MutableLiveData<>();
        predictionEntitiesLiveData.setValue(new PredictionEntityWrapper.Success(predictionEntities));
        doReturn(predictionEntitiesLiveData).when(predictionsRepository)
            .getPredictionsLiveData(
                "TEST",
                TestValues.TEST_LATITUDE,
                TestValues.TEST_LONGITUDE,
                RADIUS,
                TYPES
            );

        // When
        PredictionEntityWrapper result = getValueForTesting(getPredictionsWrapperUseCase.invoke("TEST"));

        // Then
        assertTrue(result instanceof PredictionEntityWrapper.Success);
        assertEquals(predictionEntitiesLiveData.getValue(), result);
        verify(predictionsRepository).getPredictionsLiveData(
            "TEST",
            TestValues.TEST_LATITUDE,
            TestValues.TEST_LONGITUDE,
            RADIUS,
            TYPES
        );
        verify(getCurrentLocationUseCase).invoke();
        verifyNoMoreInteractions(predictionsRepository, getCurrentLocationUseCase);
    }

    @Test
    public void testInvoke_gpsProviderDisabled() {
        // Given
        LocationEntityWrapper locationEntityWrapper = TestValues.getTestLocationEntityWrapperGpsDisabled();
        MutableLiveData<LocationEntityWrapper> locationEntityWrapperMutableLiveData = new MutableLiveData<>();
        locationEntityWrapperMutableLiveData.setValue(locationEntityWrapper);
        doReturn(locationEntityWrapperMutableLiveData).when(getCurrentLocationUseCase).invoke();

        // When
        PredictionEntityWrapper result = getValueForTesting(getPredictionsWrapperUseCase.invoke("TEST"));

        // Then
        assertTrue(result instanceof PredictionEntityWrapper.RequestError);
        verify(getCurrentLocationUseCase).invoke();
        verifyNoMoreInteractions(predictionsRepository, getCurrentLocationUseCase);
    }

    @Test
    public void testInvoke_NoResults() {
        // Given
        LocationEntityWrapper locationEntityWrapper = TestValues.getTestLocationEntityWrapperGpsEnabled();
        MutableLiveData<LocationEntityWrapper> locationEntityWrapperMutableLiveData = new MutableLiveData<>();
        locationEntityWrapperMutableLiveData.setValue(locationEntityWrapper);
        doReturn(locationEntityWrapperMutableLiveData).when(getCurrentLocationUseCase).invoke();

        MutableLiveData<PredictionEntityWrapper> predictionEntitiesLiveData = new MutableLiveData<>();
        predictionEntitiesLiveData.setValue(new PredictionEntityWrapper.NoResults());
        doReturn(predictionEntitiesLiveData).when(predictionsRepository)
            .getPredictionsLiveData(
                "TEST",
                TestValues.TEST_LATITUDE,
                TestValues.TEST_LONGITUDE,
                RADIUS,
                TYPES
            );

        // When
        PredictionEntityWrapper result = getValueForTesting(getPredictionsWrapperUseCase.invoke("TEST"));

        // Then
        assertTrue(result instanceof PredictionEntityWrapper.NoResults);
        assertEquals(predictionEntitiesLiveData.getValue(), result);
        verify(predictionsRepository).getPredictionsLiveData(
            "TEST",
            TestValues.TEST_LATITUDE,
            TestValues.TEST_LONGITUDE,
            RADIUS,
            TYPES
        );
        verify(getCurrentLocationUseCase).invoke();
        verifyNoMoreInteractions(predictionsRepository, getCurrentLocationUseCase);
    }
}
