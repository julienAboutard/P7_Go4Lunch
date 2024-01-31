package com.example.go4lunch.domain.location;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.gps.location.GpsLocationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StartLocationRequestUseCaseTest {

    @Mock
    public GpsLocationRepository gpsLocationRepository;

    public StartLocationRequestUseCase startLocationRequestUseCase;

    @Before
    public void setUp() {
        startLocationRequestUseCase = new StartLocationRequestUseCase(gpsLocationRepository);
    }

    @Test
    public void testInvoke() {
        startLocationRequestUseCase.invoke();

        verify(gpsLocationRepository).startLocationRequest();
        verifyNoMoreInteractions(gpsLocationRepository);
    }
}
