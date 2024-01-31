package com.example.go4lunch.domain.user;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.user.FavoriteRestaurantRepository;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddFavoriteRestaurantUseCaseTest {

    @Mock
    private FavoriteRestaurantRepository favoriteRestaurantRepository;

    @Mock
    private GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    private AddFavoriteRestaurantUseCase addFavoriteRestaurantUseCase;

    @Before
    public void setUp() {
        addFavoriteRestaurantUseCase = new AddFavoriteRestaurantUseCase(
            favoriteRestaurantRepository,
            getCurrentLoggedUserIdUseCase
        );
    }

    @Test
    public void testInvoke() {
        //Given
        doReturn(TestValues.TEST_USER_ID).when(getCurrentLoggedUserIdUseCase).invoke();

        // When
        addFavoriteRestaurantUseCase.invoke(TestValues.TEST_RESTAURANT_ID);

        // Then
        verify(favoriteRestaurantRepository).addFavoriteRestaurant(TestValues.TEST_USER_ID, TestValues.TEST_RESTAURANT_ID);
        verifyNoMoreInteractions(favoriteRestaurantRepository);
    }
}
