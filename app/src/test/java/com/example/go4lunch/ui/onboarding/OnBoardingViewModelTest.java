package com.example.go4lunch.ui.onboarding;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.domain.permission.HasGpsPermissionUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnBoardingViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private HasGpsPermissionUseCase hasGpsPermissionUseCase;

    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    private OnBoardingViewModel onBoardingViewModel;

    @Before
    public void setUp() {
        doReturn(hasGpsPermissionLiveData).when(hasGpsPermissionUseCase).invoke();

        onBoardingViewModel = new OnBoardingViewModel(hasGpsPermissionUseCase);
    }


    @Test
    public void edge_case() {
        // Given
        hasGpsPermissionLiveData.setValue(null);

        // When
        OnBoardingViewAction result = getValueForTesting(onBoardingViewModel.getOnBoardingViewAction());

        // Then
        assertNull(result);
    }

    @Test
    public void shouldShowRationaleAndHasGpsPermission_shouldContinueToAuth() {
        // Given
        hasGpsPermissionLiveData.setValue(true);

        // When
        OnBoardingViewAction result = getValueForTesting(onBoardingViewModel.getOnBoardingViewAction());

        // Then
        assertEquals(OnBoardingViewAction.CONTINUE_TO_AUTHENTICATION, result);
    }

    @Test
    public void shouldShowRationaleAndNoGpsPermission_shouldAskGpsPermission() {
        // Given
        hasGpsPermissionLiveData.setValue(false);

        // When
        OnBoardingViewAction result = getValueForTesting(onBoardingViewModel.getOnBoardingViewAction());

        // Then
        assertEquals(OnBoardingViewAction.ASK_GPS_PERMISSION, result);
    }

    @Test
    public void hasGpsPermissionBeenAskedAndHasNoGpsPermission_shouldShowRationale() {
        // Given
        onBoardingViewModel.onAllowClicked(true);
        hasGpsPermissionLiveData.setValue(false);

        // When
        OnBoardingViewAction result = getValueForTesting(onBoardingViewModel.getOnBoardingViewAction());

        // Then
        assertEquals(OnBoardingViewAction.SHOW_RATIONALE, result);
    }


    @Test
    public void onChangeAppSettingsClicked_shouldSetOnBoardingViewActionGoAppSettings() {
        // Given
        onBoardingViewModel.onChangeAppSettingsClicked();

        // When
        OnBoardingViewAction result = getValueForTesting(onBoardingViewModel.getOnBoardingViewAction());

        // Then
        assertEquals(OnBoardingViewAction.GO_APP_SETTINGS, result);
    }

}
