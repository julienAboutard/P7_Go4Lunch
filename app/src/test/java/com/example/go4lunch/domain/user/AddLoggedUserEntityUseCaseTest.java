package com.example.go4lunch.domain.user;

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
public class AddLoggedUserEntityUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    private AddLoggedUserEntityUseCase addLoggedUserEntityUseCase;

    @Before
    public void setUp() {
        addLoggedUserEntityUseCase = new AddLoggedUserEntityUseCase(userRepository, getCurrentLoggedUserUseCase);
    }

    @Test
    public void testInvoke() {
        //Given
        doReturn(TestValues.getTestLoggedUserEntity()).when(getCurrentLoggedUserUseCase).invoke();

        // When
        addLoggedUserEntityUseCase.invoke();

        // Then
        verify(userRepository).upsertLoggedUserEntity(TestValues.getTestLoggedUserEntity());
        verifyNoMoreInteractions(userRepository);
    }
}
