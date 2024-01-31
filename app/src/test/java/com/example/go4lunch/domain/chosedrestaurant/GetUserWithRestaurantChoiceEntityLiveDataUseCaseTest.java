package com.example.go4lunch.domain.chosedrestaurant;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetUserWithRestaurantChoiceEntityLiveDataUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthRepository authRepository;

    private GetUserWithRestaurantChoiceEntityLiveDataUseCase getUserWithRestaurantChoiceEntityLiveDataUseCase;

    @Before
    public void setUp() {
        getUserWithRestaurantChoiceEntityLiveDataUseCase = new GetUserWithRestaurantChoiceEntityLiveDataUseCase(
            userRepository,
            authRepository
        );
    }

    @Test
    public void testInvoke() {
        //Given
        MutableLiveData<LoggedUserEntity> currentLoggedUserMutableLiveData = new MutableLiveData<>();
        currentLoggedUserMutableLiveData.setValue(TestValues.getTestLoggedUserEntity());
        doReturn(currentLoggedUserMutableLiveData).when(authRepository).getLoggedUserLiveData();

        MutableLiveData<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntityMutableLiveData = new MutableLiveData<>();
        userWithRestaurantChoiceEntityMutableLiveData.setValue(TestValues.getTestCurrentUserWithRestaurantChoiceEntity());
        doReturn(userWithRestaurantChoiceEntityMutableLiveData).when(userRepository).getUserWithRestaurantChoiceEntity(TestValues.TEST_USER_ID);

        // When
        UserWithRestaurantChoiceEntity result = getValueForTesting(getUserWithRestaurantChoiceEntityLiveDataUseCase.invoke());

        // Then
        assertEquals(TestValues.getTestCurrentUserWithRestaurantChoiceEntity(), result);
        verify(userRepository).getUserWithRestaurantChoiceEntity(TestValues.TEST_USER_ID);
        verify(authRepository).getLoggedUserLiveData();
        verifyNoMoreInteractions(userRepository, authRepository);
    }
}
