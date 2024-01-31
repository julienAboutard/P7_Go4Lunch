package com.example.go4lunch.domain.user;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.firebaseauth.AuthRepository;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.entity.UserEntity;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.domain.authentification.IsUserLoggedInLiveDataUseCase;
import com.example.go4lunch.domain.chosedrestaurant.GetUserWithRestaurantChoiceEntityLiveDataUseCase;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class GetUserEntityUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetFavoriteRestaurantsIdsUseCase getFavoriteRestaurantsIdsUseCase;

    @Mock
    private GetUserWithRestaurantChoiceEntityLiveDataUseCase getUserWithRestaurantChoiceEntityLiveDataUseCase;

    @Mock
    private IsUserLoggedInLiveDataUseCase isUserLoggedInLiveDataUseCase ;

    @Mock
    private AuthRepository authRepository;

    private GetUserEntityUseCase getUserEntityUseCase;

    @Before
    public void setUp() {
        MutableLiveData<Set<String>> favoriteRestaurantIdsMutableLiveData = new MutableLiveData<>();
        Set<String> favoriteRestaurantsIds = new HashSet<>();
        favoriteRestaurantsIds.add("favoriteRestaurant1");
        favoriteRestaurantsIds.add("favoriteRestaurant2");
        favoriteRestaurantsIds.add("favoriteRestaurant3");
        favoriteRestaurantIdsMutableLiveData.setValue(favoriteRestaurantsIds);
        doReturn(favoriteRestaurantIdsMutableLiveData).when(getFavoriteRestaurantsIdsUseCase).invoke();


        MutableLiveData<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntityMutableLiveData = new MutableLiveData<>();
        UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity = TestValues.getTestCurrentUserWithRestaurantChoiceEntity();
        userWithRestaurantChoiceEntityMutableLiveData.setValue(userWithRestaurantChoiceEntity);
        doReturn(userWithRestaurantChoiceEntityMutableLiveData).when(getUserWithRestaurantChoiceEntityLiveDataUseCase).invoke();

        MutableLiveData<Boolean> isUserLoggedInMutableLiveData = new MutableLiveData<>();
        isUserLoggedInMutableLiveData.setValue(true);
        doReturn(isUserLoggedInMutableLiveData).when(isUserLoggedInLiveDataUseCase).invoke();

        MutableLiveData<LoggedUserEntity> loggedUserEntityMutableLiveData = new MutableLiveData<>();
        loggedUserEntityMutableLiveData.setValue(TestValues.getTestLoggedUserEntity());
        doReturn(loggedUserEntityMutableLiveData).when(authRepository).getLoggedUserLiveData();


        getUserEntityUseCase = new GetUserEntityUseCase(
            getFavoriteRestaurantsIdsUseCase,
            getUserWithRestaurantChoiceEntityLiveDataUseCase,
            isUserLoggedInLiveDataUseCase,
            authRepository
        );
    }

    @Test
    public void invoke() {
        // Given
        Set<String> favoriteRestaurantsIds = new HashSet<>();
        favoriteRestaurantsIds.add("favoriteRestaurant1");
        favoriteRestaurantsIds.add("favoriteRestaurant2");
        favoriteRestaurantsIds.add("favoriteRestaurant3");


        UserEntity expectedUserEntity = new UserEntity(
            TestValues.getTestLoggedUserEntity(),
            favoriteRestaurantsIds,
            TestValues.getTestCurrentUserWithRestaurantChoiceEntity().getAttendingRestaurantId()
        );

        // When
        UserEntity result = getValueForTesting(getUserEntityUseCase.invoke());

        // Then
        assertEquals(expectedUserEntity, result);

        verify(getFavoriteRestaurantsIdsUseCase).invoke();
        verify(getUserWithRestaurantChoiceEntityLiveDataUseCase).invoke();
        verify(isUserLoggedInLiveDataUseCase).invoke();
        verify(authRepository).getLoggedUserLiveData();
        verifyNoMoreInteractions(getFavoriteRestaurantsIdsUseCase, getUserWithRestaurantChoiceEntityLiveDataUseCase, isUserLoggedInLiveDataUseCase, authRepository);
    }
}
