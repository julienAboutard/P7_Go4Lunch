package com.example.go4lunch.domain.chosedrestaurant;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RemoveUserRestaurantChoiceUseCaseTest {

    @Mock
    private UserRepository userRepository ;

    @Mock
    private GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    private RemoveUserRestaurantChoiceUseCase removeUserRestaurantChoiceUseCase;

    @Before
    public void setUp() {
        removeUserRestaurantChoiceUseCase = new RemoveUserRestaurantChoiceUseCase(userRepository, getCurrentLoggedUserUseCase);
    }

    @Test
    public void testInvoke() {
        //Given
        doReturn(TestValues.getTestLoggedUserEntity()).when(getCurrentLoggedUserUseCase).invoke();

        // When
        removeUserRestaurantChoiceUseCase.invoke();

        // Then
        verify(getCurrentLoggedUserUseCase).invoke();
        verify(userRepository).deleteUserRestaurantChoice(
            TestValues.getTestLoggedUserEntity()
        );
        verifyNoMoreInteractions(userRepository, getCurrentLoggedUserUseCase);
    }
}
