package com.example.go4lunch.domain.user;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.user.FavoriteRestaurantRepository;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class GetFavoriteRestaurantsIdsUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FavoriteRestaurantRepository favoriteRestaurantRepository;

    @Mock
    private GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase ;

    private GetFavoriteRestaurantsIdsUseCase getFavoriteRestaurantsIdsUseCase;

    @Before
    public void setUp() {
        getFavoriteRestaurantsIdsUseCase = new GetFavoriteRestaurantsIdsUseCase(
            favoriteRestaurantRepository,
            getCurrentLoggedUserIdUseCase
        );
    }

    @Test
    public void testInvoke() {
        //Given
        MutableLiveData<Set<String>> restaurantIds = new MutableLiveData<>();
        restaurantIds.setValue(TestValues.getTestRestaurantIdSet(5));
        doReturn(TestValues.TEST_USER_ID).when(getCurrentLoggedUserIdUseCase).invoke();
        doReturn(restaurantIds).when(favoriteRestaurantRepository).getUserFavoriteRestaurantIdsLiveData(TestValues.TEST_USER_ID);

        // When
        getFavoriteRestaurantsIdsUseCase.invoke();

        // Then
        verify(favoriteRestaurantRepository).getUserFavoriteRestaurantIdsLiveData(getCurrentLoggedUserIdUseCase.invoke());
        verifyNoMoreInteractions(favoriteRestaurantRepository);
    }
}
