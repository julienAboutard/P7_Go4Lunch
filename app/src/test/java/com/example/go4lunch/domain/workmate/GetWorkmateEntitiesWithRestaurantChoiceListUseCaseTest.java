package com.example.go4lunch.domain.workmate;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.data.workmate.WorkmateEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetWorkmateEntitiesWithRestaurantChoiceListUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    @Mock
    private GetLoggedUserEntitiesUseCase getLoggedUserEntitiesUseCase;

    private GetWorkmateEntitiesWithRestaurantChoiceListUseCase getWorkmateEntitiesWithRestaurantChoiceListUseCase;

    @Before
    public void setUp() {
        List<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntities = new ArrayList<>();

        UserWithRestaurantChoiceEntity currentUserWithRestaurantChoice = TestValues.getTestCurrentUserWithRestaurantChoiceEntity();
        userWithRestaurantChoiceEntities.add(TestValues.getSpecifiedUserWithRestaurantChoiceEntity(1));
        userWithRestaurantChoiceEntities.add(TestValues.getSpecifiedUserWithRestaurantChoiceEntity(2));
        userWithRestaurantChoiceEntities.add(currentUserWithRestaurantChoice);
        MutableLiveData<List<UserWithRestaurantChoiceEntity>> userWithRestaurantChoiceEntitiesLiveData = new MutableLiveData<>();
        userWithRestaurantChoiceEntitiesLiveData.setValue(userWithRestaurantChoiceEntities);
        doReturn(userWithRestaurantChoiceEntitiesLiveData).when(userRepository).getUsersWithRestaurantChoiceEntities();

        String currentLoggedUserId = TestValues.TEST_USER_ID;
        doReturn(currentLoggedUserId).when(getCurrentLoggedUserIdUseCase).invoke();

        List<LoggedUserEntity> loggedUserEntities = TestValues.getFourTestLoggedUserEntities();
        MutableLiveData<List<LoggedUserEntity>> loggedUserEntitiesLiveData = new MutableLiveData<>();
        loggedUserEntitiesLiveData.setValue(loggedUserEntities);
        doReturn(loggedUserEntitiesLiveData).when(getLoggedUserEntitiesUseCase).invoke();

        getWorkmateEntitiesWithRestaurantChoiceListUseCase = new GetWorkmateEntitiesWithRestaurantChoiceListUseCase(
            userRepository,
            getCurrentLoggedUserIdUseCase,
            getLoggedUserEntitiesUseCase
        );
    }

    @Test
    public void testInvoke() {
        // When
        List<WorkmateEntity> result = getValueForTesting(getWorkmateEntitiesWithRestaurantChoiceListUseCase.invoke());

        // Then
        assertEquals(2, result.size());
        assert result.get(0).getAttendingRestaurantName() != null;
        assertEquals(TestValues.TEST_RESTAURANT_NAME, result.get(0).getAttendingRestaurantName());
        assertEquals(TestValues.TEST_USER_NAME, result.get(0).getLoggedUserEntity().getName());

        verify(userRepository).getUsersWithRestaurantChoiceEntities();
        verify(getCurrentLoggedUserIdUseCase).invoke();
        verify(getLoggedUserEntitiesUseCase).invoke();
        verifyNoMoreInteractions(userRepository, getCurrentLoggedUserIdUseCase, getLoggedUserEntitiesUseCase);
    }
}
