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
public class ResetPredictionPlaceIdUseCaseTest {

    @Mock
    private PredictionsRepository predictionsRepository;

    private ResetPredictionPlaceIdUseCase resetPredictionPlaceIdUseCase;

    @Before
    public void setUp() {
        resetPredictionPlaceIdUseCase = new ResetPredictionPlaceIdUseCase(predictionsRepository);
    }

    @Test
    public void testInvoke() {
        //When
        resetPredictionPlaceIdUseCase.invoke();

        //Then
        verify(predictionsRepository).resetPredictionPlaceIdQuery();
        verifyNoMoreInteractions(predictionsRepository);
    }
}
