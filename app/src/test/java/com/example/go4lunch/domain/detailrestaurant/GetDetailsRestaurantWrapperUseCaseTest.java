package com.example.go4lunch.domain.detailrestaurant;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.detailsretaurant.DetailsRestaurantRepository;
import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantWrapper;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetDetailsRestaurantWrapperUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private DetailsRestaurantRepository detailsRestaurantRepository;

    private GetDetailsRestaurantWrapperUseCase getDetailsRestaurantWrapperUseCase;

    @Before
    public void setUp() {
        getDetailsRestaurantWrapperUseCase = new GetDetailsRestaurantWrapperUseCase(detailsRestaurantRepository);
    }

    @Test
    public void testInvoke() {
        // Given
        MutableLiveData<DetailsRestaurantWrapper> detailsRestaurantWrapperMutableLiveData = new MutableLiveData<>();
        DetailsRestaurantWrapper detailsRestaurantWrapper = new DetailsRestaurantWrapper.Success(TestValues.getTestDetailsRestaurantEntity());
        detailsRestaurantWrapperMutableLiveData.setValue(detailsRestaurantWrapper);
        doReturn(detailsRestaurantWrapperMutableLiveData).when(detailsRestaurantRepository).getRestaurantDetails(TestValues.TEST_RESTAURANT_ID);

        // When
        DetailsRestaurantWrapper result = getValueForTesting(getDetailsRestaurantWrapperUseCase.invoke(TestValues.TEST_RESTAURANT_ID));

        // Then
        assertTrue(result instanceof DetailsRestaurantWrapper.Success);
        assertEquals(detailsRestaurantWrapper, result);
        verify(detailsRestaurantRepository).getRestaurantDetails(TestValues.TEST_RESTAURANT_ID);
        verifyNoMoreInteractions(detailsRestaurantRepository);
    }

    @Test
    public void testInvoke_RequestError() {
        // Given
        MutableLiveData<DetailsRestaurantWrapper> detailsRestaurantWrapperMutableLiveData = new MutableLiveData<>();
        detailsRestaurantWrapperMutableLiveData.setValue(new DetailsRestaurantWrapper.RequestError(null));
        doReturn(detailsRestaurantWrapperMutableLiveData).when(detailsRestaurantRepository).getRestaurantDetails(TestValues.TEST_RESTAURANT_ID);

        //When
        DetailsRestaurantWrapper result = getValueForTesting(getDetailsRestaurantWrapperUseCase.invoke(TestValues.TEST_RESTAURANT_ID));

        // Then
        assertTrue(result instanceof DetailsRestaurantWrapper.RequestError);
        verify(detailsRestaurantRepository).getRestaurantDetails(TestValues.TEST_RESTAURANT_ID);
        verifyNoMoreInteractions(detailsRestaurantRepository);
    }
}
