package com.example.go4lunch.domain.chosedrestaurant;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.ChosenRestaurantEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddUserRestaurantChoiceUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    private AddUserRestaurantChoiceUseCase addUserRestaurantChoiceUseCase;

    @Before
    public void setUp() {
        addUserRestaurantChoiceUseCase = new AddUserRestaurantChoiceUseCase(userRepository, getCurrentLoggedUserUseCase);
    }

    @Test
    public void invoke() {
        //Given
        doReturn(TestValues.getTestLoggedUserEntity()).when(getCurrentLoggedUserUseCase).invoke();

        // When
        addUserRestaurantChoiceUseCase.invoke(
            TestValues.TEST_RESTAURANT_ID,
            TestValues.TEST_RESTAURANT_NAME,
            TestValues.TEST_RESTAURANT_VICINITY,
            TestValues.TEST_NEARBYSEARCH_PICTURE_URL
        );

        // Then
        verify(getCurrentLoggedUserUseCase).invoke();
        verify(userRepository).upsertUserRestaurantChoice(
            TestValues.getTestLoggedUserEntity(),
            new ChosenRestaurantEntity(
                null,
                TestValues.TEST_RESTAURANT_ID,
                TestValues.TEST_RESTAURANT_NAME,
                TestValues.TEST_RESTAURANT_VICINITY,
                TestValues.TEST_NEARBYSEARCH_PICTURE_URL
            )
        );
        verifyNoMoreInteractions(userRepository, getCurrentLoggedUserUseCase);
    }
}
