package com.example.go4lunch.domain.notification;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.go4lunch.data.notification.entity.NotificationEntity;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetNotificationEntityUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    private GetNotificationEntityUseCase getNotificationEntityUseCase;

    @Before
    public void setUp() {
        getNotificationEntityUseCase = new GetNotificationEntityUseCase(userRepository, getCurrentLoggedUserIdUseCase);
    }

    @Test
    public void notificationEntity_withOneWorkmateGoingToSameRestaurant() {
        // Given
        List<UserWithRestaurantChoiceEntity> usersWithRestaurantChoiceEntities = new ArrayList<>();
        UserWithRestaurantChoiceEntity currentUserWithRestaurantChoiceEntity = TestValues.getTestCurrentUserWithRestaurantChoiceEntity();
        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity1 = TestValues.getTestUserWithSameRestaurantChoiceEntity();
        usersWithRestaurantChoiceEntities.add(currentUserWithRestaurantChoiceEntity);
        usersWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity1);

        doReturn(usersWithRestaurantChoiceEntities).when(userRepository).getUsersWithRestaurantChoiceEntitiesAsync();
        doReturn(TestValues.TEST_USER_ID).when(getCurrentLoggedUserIdUseCase).invoke();

        List<String> expectedWorkmatesNames = Collections.singletonList(
            TestValues.getTestUserWithDifferentRestaurantChoiceEntity(null).getAttendingUsername()
        );

        // When
        NotificationEntity expectedNotificationEntity = TestValues.getTestNotificationEntity(expectedWorkmatesNames);
        NotificationEntity result = getNotificationEntityUseCase.invoke();


        // Then
        verify(getCurrentLoggedUserIdUseCase).invoke();
        verify(userRepository).getUsersWithRestaurantChoiceEntitiesAsync();
        assertEquals(expectedNotificationEntity, result);
        verifyNoMoreInteractions(getCurrentLoggedUserIdUseCase, userRepository);
    }

    @Test
    public void notificationEntity_withTwoWorkmatesGoingToSameRestaurant() {
        // Given
        List<UserWithRestaurantChoiceEntity> usersWithRestaurantChoiceEntities = new ArrayList<>();
        UserWithRestaurantChoiceEntity currentUserWithRestaurantChoiceEntity = TestValues.getTestCurrentUserWithRestaurantChoiceEntity();
        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity1 = TestValues.getTestUserWithSameRestaurantChoiceEntity();
        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity2 = TestValues.getTestUserWithSameRestaurantChoiceEntity();
        usersWithRestaurantChoiceEntities.add(currentUserWithRestaurantChoiceEntity);
        usersWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity1);
        usersWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity2);

        doReturn(usersWithRestaurantChoiceEntities).when(userRepository).getUsersWithRestaurantChoiceEntitiesAsync();
        doReturn(TestValues.TEST_USER_ID).when(getCurrentLoggedUserIdUseCase).invoke();

        List<String> expectedWorkmatesNames = Arrays.asList(
            TestValues.getTestUserWithDifferentRestaurantChoiceEntity(null).getAttendingUsername(),
            TestValues.getTestUserWithDifferentRestaurantChoiceEntity(null).getAttendingUsername()
        );

        // When
        NotificationEntity expectedNotificationEntity = TestValues.getTestNotificationEntity(expectedWorkmatesNames);
        NotificationEntity result = getNotificationEntityUseCase.invoke();


        // Then
        verify(getCurrentLoggedUserIdUseCase).invoke();
        verify(userRepository).getUsersWithRestaurantChoiceEntitiesAsync();
        assertEquals(expectedNotificationEntity, result);
        verifyNoMoreInteractions(getCurrentLoggedUserIdUseCase, userRepository);
    }

    @Test
    public void notificationEntity_withNoWorkmatesGoingToSameRestaurant() {
        // Given
        List<UserWithRestaurantChoiceEntity> usersWithRestaurantChoiceEntities = new ArrayList<>();
        UserWithRestaurantChoiceEntity currentUserWithRestaurantChoiceEntity = TestValues.getTestCurrentUserWithRestaurantChoiceEntity();
        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity1 = TestValues.getTestUserWithDifferentRestaurantChoiceEntity("123");
        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity2 = TestValues.getTestUserWithDifferentRestaurantChoiceEntity("456");
        usersWithRestaurantChoiceEntities.add(currentUserWithRestaurantChoiceEntity);
        usersWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity1);
        usersWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity2);

        doReturn(usersWithRestaurantChoiceEntities).when(userRepository).getUsersWithRestaurantChoiceEntitiesAsync();
        doReturn(TestValues.TEST_USER_ID).when(getCurrentLoggedUserIdUseCase).invoke();


        // When
        NotificationEntity result = getNotificationEntityUseCase.invoke();

        //Then
        verify(getCurrentLoggedUserIdUseCase).invoke();
        verify(userRepository).getUsersWithRestaurantChoiceEntitiesAsync();
        assert result != null;
        assertEquals(0, result.getWorkmates().size());
        verifyNoMoreInteractions(getCurrentLoggedUserIdUseCase, userRepository);
    }
}
