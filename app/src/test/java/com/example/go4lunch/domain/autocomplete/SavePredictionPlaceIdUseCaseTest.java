package com.example.go4lunch.domain.autocomplete;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.autocomplete.PredictionsRepository;

import org.junit.Before;
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
