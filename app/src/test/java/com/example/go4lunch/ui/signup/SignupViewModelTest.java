package com.example.go4lunch.ui.signup;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.domain.authentification.SignupUserUseCase;
import com.example.go4lunch.domain.user.AddLoggedUserEntityUseCase;
import com.google.firebase.auth.FirebaseAuth;

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

    @Mock
    private FirebaseAuth firebaseAuth;

    private SignupViewModel signupViewModel;

    @Before
    public void setUp() {
        signupViewModel = new SignupViewModel(signupUserUseCase, addLoggedUserEntityUseCase, firebaseAuth);
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
        verify(signupUserUseCase).invoke("test", "****");
    }
}
