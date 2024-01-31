package com.example.go4lunch.ui.signup;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.domain.authentification.SignupUserUseCase;
import com.example.go4lunch.domain.user.AddLoggedUserEntityUseCase;
import com.example.go4lunch.ui.login.LoginViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SignupViewModelTest {

    @Mock
    private AddLoggedUserEntityUseCase addLoggedUserEntityUseCase;

    @Mock
    private SignupUserUseCase signupUserUseCase;

    private SignupViewModel signupViewModel;

    @Before
    public void setUp() {
        signupViewModel = new SignupViewModel(signupUserUseCase, addLoggedUserEntityUseCase);
    }

    @Test
    public void onLoginComplete_shouldAddLoggedUser() {
        //When
        signupViewModel.onLoginComplete();

        //Then
        verify(addLoggedUserEntityUseCase).invoke();
        verifyNoMoreInteractions(addLoggedUserEntityUseCase);
    }

    @Test
    public void onSignupComplete() {
        //Given
        signupViewModel.onMailChanged("test");
        signupViewModel.onPasswordChanged("****");
        signupViewModel.onNameChanged("tester");

        //When
        signupViewModel.onSignupButton();

        //Then
        verify(signupUserUseCase).invoke("test", "****", "tester");
    }
}
