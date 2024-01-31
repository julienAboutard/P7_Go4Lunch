package com.example.go4lunch.domain.permission;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.gps.permission.GpsPermissionRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HasGpsPermissionUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GpsPermissionRepository gpsPermissionRepository;

    private HasGpsPermissionUseCase hasGpsPermissionUseCase;

    @Before
    public void setUp() {
        hasGpsPermissionUseCase = new HasGpsPermissionUseCase(gpsPermissionRepository);
    }

    @Test
    public void hasGpsPermission() {
        // Given
        MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>(true);
        doReturn(hasGpsPermissionLiveData).when(gpsPermissionRepository).hasGpsPermissionLiveData();

        // When
        Boolean result = getValueForTesting(hasGpsPermissionUseCase.invoke());

        // Then
        assertTrue(result);
        verify(gpsPermissionRepository).hasGpsPermissionLiveData();
        verifyNoMoreInteractions(gpsPermissionRepository);
    }

    @Test
    public void hasNotGpsPermission() {
        // Given
        MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>(false);
        doReturn(hasGpsPermissionLiveData).when(gpsPermissionRepository).hasGpsPermissionLiveData();

        // When
        Boolean result = getValueForTesting(hasGpsPermissionUseCase.invoke());

        // Then
        assertFalse(result);
        verify(gpsPermissionRepository).hasGpsPermissionLiveData();
        verifyNoMoreInteractions(gpsPermissionRepository);
    }
}
