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
public class SavePredictionPlaceIdUseCaseTest {

    @Mock
    private PredictionsRepository predictionsRepository;

    private SavePredictionPlaceIdUseCase savePredictionPlaceIdUseCase;

    @Before
    public void setUp() {
        savePredictionPlaceIdUseCase = new SavePredictionPlaceIdUseCase(predictionsRepository);
    }

    @Test
    public void testInvoke() {
        //When
        savePredictionPlaceIdUseCase.invoke("1");

        //Then
        verify(predictionsRepository).savePredictionPlaceId("1");
        verifyNoMoreInteractions(predictionsRepository);
    }
}
