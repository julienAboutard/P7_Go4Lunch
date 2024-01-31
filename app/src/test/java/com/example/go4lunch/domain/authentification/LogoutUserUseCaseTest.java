package com.example.go4lunch.domain.authentification;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.firebaseauth.AuthRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LogoutUserUseCaseTest {

    @Mock private AuthRepository authRepository;

    private LogoutUserUseCase logoutUserUseCase;

    @Before
    public void setUp() {
        logoutUserUseCase = new LogoutUserUseCase(authRepository);
    }

    @Test
    public void testInvoke() {
        // When
        logoutUserUseCase.invoke();

        // Then
        verify(authRepository).logOut();
        verifyNoMoreInteractions(authRepository);
    }
}
