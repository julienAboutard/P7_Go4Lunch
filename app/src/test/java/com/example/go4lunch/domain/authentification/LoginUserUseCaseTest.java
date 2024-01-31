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
public class LoginUserUseCaseTest {

    @Mock
    private AuthRepository authRepository;

    private LoginUserUSeCase loginUserUSeCase;

    @Before
    public void setUp() {
        loginUserUSeCase = new LoginUserUSeCase(authRepository);
    }

    @Test
    public void testInvoke() {
        //When
        loginUserUSeCase.invoke("test", "test");

        //Then
        verify(authRepository).logIn("test","test");
        verifyNoMoreInteractions(authRepository);
    }
}
