package com.example.go4lunch.domain.workmate;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.workmate.WorkmateEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;
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
public class GetWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    @Mock
    private GetWorkmateEntitiesWithRestaurantChoiceListUseCase getWorkmateEntitiesWithRestaurantChoiceListUseCase;

    @Mock
    private GetLoggedUserEntitiesUseCase getLoggedUserEntitiesUseCase;

    private GetWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase getWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase;

    @Before
    public void setUp() {
        // Given
        MutableLiveData<List<WorkmateEntity>> workmateEntitiesWithRestaurantChoiceLiveData = new MutableLiveData<>();
        List<WorkmateEntity> workmateEntityList = new ArrayList<>();
        workmateEntityList.add(TestValues.getTestSpecifiedWorkmateEntity(1));
        workmateEntityList.add(TestValues.getTestSpecifiedWorkmateEntity(2));
        workmateEntityList.add(TestValues.getTestSpecifiedWorkmateEntity(3));
        workmateEntitiesWithRestaurantChoiceLiveData.setValue(workmateEntityList);
        doReturn(workmateEntitiesWithRestaurantChoiceLiveData).when(getWorkmateEntitiesWithRestaurantChoiceListUseCase).invoke();

        MutableLiveData<List<LoggedUserEntity>> loggedUserEntitiesLiveData = new MutableLiveData<>();
        List<LoggedUserEntity> loggedUserEntityList = TestValues.getFourTestLoggedUserEntities();
        loggedUserEntityList.add(TestValues.getTestLoggedUserEntity());
        loggedUserEntitiesLiveData.setValue(loggedUserEntityList);
        doReturn(loggedUserEntitiesLiveData).when(getLoggedUserEntitiesUseCase).invoke();


        LoggedUserEntity currentLoggedUser = TestValues.getTestLoggedUserEntity();
        doReturn(currentLoggedUser).when(getCurrentLoggedUserUseCase).invoke();

        getWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase = new GetWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase(
            getWorkmateEntitiesWithRestaurantChoiceListUseCase,
            getCurrentLoggedUserUseCase,
            getLoggedUserEntitiesUseCase
        );
    }

    @Test
    public void testInvoke() {
        List<WorkmateEntity> workmateEntities = getValueForTesting(getWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase.invoke());

        assertEquals(4, workmateEntities.size());
        assertEquals("TEST_USER_ID1", workmateEntities.get(0).getLoggedUserEntity().getId());
        assertEquals("TEST_RESTAURANT_ID1", workmateEntities.get(0).getAttendingRestaurantId());
        assertEquals("TEST_USER_ID2", workmateEntities.get(1).getLoggedUserEntity().getId());
        assertEquals("TEST_RESTAURANT_ID2", workmateEntities.get(1).getAttendingRestaurantId());
        assertNull(workmateEntities.get(3).getAttendingRestaurantId());
    }
}
