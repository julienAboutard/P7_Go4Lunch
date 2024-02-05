package com.example.go4lunch.ui.dispatcher;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.domain.authentification.IsUserLoggedInLiveDataUseCase;
import com.example.go4lunch.domain.location.StartLocationRequestUseCase;
import com.example.go4lunch.domain.permission.HasGpsPermissionUseCase;
import com.example.go4lunch.ui.navigation.Destination;
import com.example.go4lunch.ui.utils.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DispatcherViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private HasGpsPermissionUseCase hasGpsPermissionUseCase;

    @Mock
    private IsUserLoggedInLiveDataUseCase isUserLoggedInLiveDataUseCase;

    @Mock
    private StartLocationRequestUseCase startLocationRequestUseCase;

    private MutableLiveData<Boolean> hasPermissionMutableLiveData;

    private MutableLiveData<Boolean> isUserLoggedInMutableLiveData;

    private DispatcherViewModel viewModel;

    @Before
    public void setUp() {
        hasPermissionMutableLiveData = new MutableLiveData<>();
        isUserLoggedInMutableLiveData = new MutableLiveData<>();

        doReturn(hasPermissionMutableLiveData).when(hasGpsPermissionUseCase).invoke();
        doReturn(isUserLoggedInMutableLiveData).when(isUserLoggedInLiveDataUseCase).invoke();

        viewModel = new DispatcherViewModel(hasGpsPermissionUseCase, isUserLoggedInLiveDataUseCase, startLocationRequestUseCase);
    }


    @Test
    public void nominal_case() {
        //GIVEN
        hasPermissionMutableLiveData.setValue(true);
        isUserLoggedInMutableLiveData.setValue(true);

        //WHEN
        Event<Destination> result = getValueForTesting(viewModel.getDestinationLiveData());

        //THEN
        assertEquals(result.getContentIfNotHandled(), Destination.HOME);
    }

    @Test
    public void edge_case() {
        //GIVEN
        hasPermissionMutableLiveData.setValue(false);
        isUserLoggedInMutableLiveData.setValue(false);

        //WHEN
        Event<Destination> result = getValueForTesting(viewModel.getDestinationLiveData());

        //THEN
        assertEquals(result.getContentIfNotHandled(), Destination.ONBOARDING);
    }

    @Test
    public void should_dispatch_to_permission_request() {
        //GIVEN
        hasPermissionMutableLiveData.setValue(false);
        isUserLoggedInMutableLiveData.setValue(true);

        //WHEN
        Event<Destination> result = getValueForTesting(viewModel.getDestinationLiveData());

        //THEN
        assertEquals(result.getContentIfNotHandled(), Destination.ONBOARDING);
    }

    @Test
    public void should_dispatch_to_login() {
        //GIVEN
        hasPermissionMutableLiveData.setValue(true);
        isUserLoggedInMutableLiveData.setValue(false);

        //WHEN
        Event<Destination> result = getValueForTesting(viewModel.getDestinationLiveData());

        //THEN
        assertEquals(result.getContentIfNotHandled(), Destination.LOGIN);
    }
}
