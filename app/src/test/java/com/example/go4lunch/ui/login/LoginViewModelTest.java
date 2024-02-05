package com.example.go4lunch.ui.login;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.domain.user.AddLoggedUserEntityUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {

    @Mock
    private AddLoggedUserEntityUseCase addLoggedUserEntityUseCase;

    @Mock
    private AuthRepository authRepository;

    private LoginViewModel loginViewModel;

    @Before
    public void setUp() {
        loginViewModel = new LoginViewModel(authRepository, addLoggedUserEntityUseCase);
    }

    @Test
    public void onLoginComplete_shouldAddLoggedUser() {
        //When
        loginViewModel.onLoginComplete();

        //Then
        verify(addLoggedUserEntityUseCase).invoke();
        verifyNoMoreInteractions(addLoggedUserEntityUseCase);
    }

    @Test
    public void onLoginComplete_shouldLogUSer() {
        //Given
        loginViewModel.onMailChanged("test");
        loginViewModel.onPasswordChanged("****");

        //When
        loginViewModel.onLoginButton();

        //Then
        verify(authRepository).logIn("test", "****");
        verifyNoMoreInteractions(authRepository);
    }
}
