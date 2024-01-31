package com.example.go4lunch.domain.authentification;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetCurrentLoggedUserUseCaseTest {

    @Mock
    private AuthRepository authRepository;

    private GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    @Before
    public void setUp() {
        getCurrentLoggedUserUseCase = new GetCurrentLoggedUserUseCase(authRepository);
    }

    @Test
    public void testInvoke() {
        //Given
        LoggedUserEntity loggedUserEntity = TestValues.getTestLoggedUserEntity();
        doReturn(loggedUserEntity).when(authRepository).getCurrentUser();

        // When
        getCurrentLoggedUserUseCase.invoke();

        // Then
        verify(authRepository).getCurrentUser();
        verifyNoMoreInteractions(authRepository);
    }
}
