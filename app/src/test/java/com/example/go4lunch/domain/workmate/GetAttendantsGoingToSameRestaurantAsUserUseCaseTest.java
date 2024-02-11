package com.example.go4lunch.domain.workmate;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
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
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class GetAttendantsGoingToSameRestaurantAsUserUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    private GetAttendantsGoingToSameRestaurantAsUserUseCase getAttendantsGoingToSameRestaurantAsUserUseCase;

    @Before
    public void setUp() {
        getAttendantsGoingToSameRestaurantAsUserUseCase = new GetAttendantsGoingToSameRestaurantAsUserUseCase(userRepository, getCurrentLoggedUserIdUseCase);
    }

    @Test
    public void testInvoke() {
        //Given
        MutableLiveData<List<UserWithRestaurantChoiceEntity>> userWithRestaurantChoiceEntitiesLiveData = new MutableLiveData<>();
        List<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntities = new ArrayList<>();
        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity1 = TestValues.getTestUserWithSameRestaurantChoiceEntity();
        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity2 = TestValues.getTestUserWithSameRestaurantChoiceEntity();
        UserWithRestaurantChoiceEntity currentUserWithRestaurantChoiceEntity = TestValues.getTestCurrentUserWithRestaurantChoiceEntity();
        userWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity1);
        userWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity2);
        userWithRestaurantChoiceEntities.add(currentUserWithRestaurantChoiceEntity);

        userWithRestaurantChoiceEntitiesLiveData.setValue(userWithRestaurantChoiceEntities);
        doReturn(userWithRestaurantChoiceEntitiesLiveData).when(userRepository).getUsersWithRestaurantChoiceEntities();

        doReturn(TestValues.TEST_USER_ID).when(getCurrentLoggedUserIdUseCase).invoke();

        //When
        Map<String, Integer> result = getValueForTesting(getAttendantsGoingToSameRestaurantAsUserUseCase.invoke());

        //Then
        Integer resultCount = result.get(TestValues.ATTENDING_RESTAURANT_ID);
        assertTrue(result.containsKey(TestValues.ATTENDING_RESTAURANT_ID));
        assert resultCount != null;
        assertEquals(3, resultCount.intValue());

        verify(userRepository).getUsersWithRestaurantChoiceEntities();
        verify(getCurrentLoggedUserIdUseCase).invoke();
        verifyNoMoreInteractions(userRepository, getCurrentLoggedUserIdUseCase);
    }
}
