package com.example.go4lunch.domain.autocomplete;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.autocomplete.PredictionsRepository;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetPredictionPlaceIdUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private PredictionsRepository predictionsRepository;

    private GetPredictionPlaceIdUseCase getPredictionPlaceIdUseCase;

    @Before
    public void setUp() {
        getPredictionPlaceIdUseCase = new GetPredictionPlaceIdUseCase(predictionsRepository);
    }

    @Test
    public void testInvoke() {
        //Given
        MutableLiveData<String> placeIdMutableLiveData = new MutableLiveData<>();
        placeIdMutableLiveData.setValue(TestValues.TEST_PREDICTION_ID);
        doReturn(placeIdMutableLiveData).when(predictionsRepository).getPredictionPlaceIdLiveData();

        //When
        String result = getValueForTesting(getPredictionPlaceIdUseCase.invoke());

        //Then
        assertEquals(result, TestValues.TEST_PREDICTION_ID);
        verify(predictionsRepository).getPredictionPlaceIdLiveData();
        verifyNoMoreInteractions(predictionsRepository);
    }
}
