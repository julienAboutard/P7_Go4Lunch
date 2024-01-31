package com.example.go4lunch.ui.workmatelist;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.workmate.WorkmateEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;
import com.example.go4lunch.domain.workmate.GetWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase;
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
public class WorkmatesViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase getWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase;

    @Mock
    private GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    private WorkmatesViewModel viewModel;

    @Before
    public void setUp() {
        LoggedUserEntity loggedUserEntity = TestValues.getTestLoggedUserEntity();
        MutableLiveData<List<WorkmateEntity>> workmateEntitiesWithAndWithoutChoiceMutableLiveData = new MutableLiveData<>();

        doReturn(workmateEntitiesWithAndWithoutChoiceMutableLiveData).when(getWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase).invoke();
        doReturn(loggedUserEntity).when(getCurrentLoggedUserUseCase).invoke();

        List<WorkmateEntity> testWorkmateEntitiesWithAndWithoutChoice = getTestWorkmateEntitiesWithAndWithoutChoice();
        workmateEntitiesWithAndWithoutChoiceMutableLiveData.setValue(testWorkmateEntitiesWithAndWithoutChoice);

        viewModel = new WorkmatesViewModel(
            getWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase,
            getCurrentLoggedUserUseCase
        );
    }

    @Test
    public void nominal_case() {
        // WHEN
        List<WorkmatesViewStateItem> result = getValueForTesting(viewModel.getWorkmates());

        // THEN
        assertEquals(3, result.size());
        verify(getCurrentLoggedUserUseCase).invoke();
        verify(getWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase).invoke();
        verifyNoMoreInteractions(getWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase);
        verifyNoMoreInteractions(getCurrentLoggedUserUseCase);
    }

    private List<WorkmateEntity> getTestWorkmateEntitiesWithAndWithoutChoice() {
        List<WorkmateEntity> workmateEntitiesWithAndWithoutChoice = new ArrayList<>();

        LoggedUserEntity loggedUser1 = new LoggedUserEntity("uid1", "username1", "email1", "urlPicture1");
        WorkmateEntity workmate1 = new WorkmateEntity(loggedUser1, "restaurantId1", "restaurantName1", "restaurantVicinity1");
        workmateEntitiesWithAndWithoutChoice.add(workmate1);

        LoggedUserEntity loggedUser2 = new LoggedUserEntity("uid2", "username2", "email2", "urlPicture2");
        WorkmateEntity workmate2 = new WorkmateEntity(loggedUser2, "restaurantId2", "restaurantName2", "restaurantVicinity2");
        workmateEntitiesWithAndWithoutChoice.add(workmate2);

        LoggedUserEntity loggedUser3 = new LoggedUserEntity("uid3", "username3", "email3", "urlPicture3");
        WorkmateEntity workmate3 = new WorkmateEntity(loggedUser3, null, null, null);
        workmateEntitiesWithAndWithoutChoice.add(workmate3);

        return workmateEntitiesWithAndWithoutChoice;
    }
}
