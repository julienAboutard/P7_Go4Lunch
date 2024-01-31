package com.example.go4lunch.domain.authentification;

import static org.junit.Assert.assertEquals;
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
public class GetCurrentLoggedUserIdUseCaseTest {

    @Mock
    private AuthRepository authRepository;

    private GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    @Before
    public void setUp() {
        getCurrentLoggedUserIdUseCase = new GetCurrentLoggedUserIdUseCase(authRepository);
    }

    @Test
    public void success() {
        //Given
        LoggedUserEntity loggedUserEntity = TestValues.getTestLoggedUserEntity();
        String userId = loggedUserEntity.getId();
        doReturn(loggedUserEntity.getId()).when(authRepository).getCurrentLoggedUserId();

        // When
        String result = getCurrentLoggedUserIdUseCase.invoke();

        // Then
        assertEquals(userId, result);
        verify(authRepository).getCurrentLoggedUserId();

        verifyNoMoreInteractions(authRepository);
    }

    @Test(expected = IllegalStateException.class)
    public void failure() {
        // Given
        doReturn(null).when(authRepository).getCurrentLoggedUserId();

        // When
        getCurrentLoggedUserIdUseCase.invoke();
    }
}
