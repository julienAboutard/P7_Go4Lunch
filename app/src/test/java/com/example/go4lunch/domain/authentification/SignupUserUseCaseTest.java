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
public class SignupUserUseCaseTest {

    @Mock
    private AuthRepository authRepository;

    private SignupUserUseCase signupUserUseCase;

    @Before
    public void setUp() {
        signupUserUseCase = new SignupUserUseCase(authRepository);
    }

    @Test
    public void testInvoke() {
        //When
        signupUserUseCase.invoke("test", "test", "test");

        //Then
        verify(authRepository).signUp("test", "test", "test");
        verifyNoMoreInteractions(authRepository);
    }
}
