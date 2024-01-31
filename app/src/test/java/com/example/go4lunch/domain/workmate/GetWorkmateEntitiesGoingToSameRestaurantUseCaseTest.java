package com.example.go4lunch.domain.workmate;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.workmate.WorkmateEntity;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetWorkmateEntitiesGoingToSameRestaurantUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetWorkmateEntitiesWithRestaurantChoiceListUseCase getWorkmateEntitiesWithRestaurantChoiceListUseCase;

    private GetWorkmateEntitiesGoingToSameRestaurantUseCase getWorkmateEntitiesGoingToSameRestaurantUseCase;

    @Before
    public void setUp() {
        getWorkmateEntitiesGoingToSameRestaurantUseCase = new GetWorkmateEntitiesGoingToSameRestaurantUseCase(
            getWorkmateEntitiesWithRestaurantChoiceListUseCase
        );
    }

    @Test
    public void testInvoke() {
        //Given
        MutableLiveData<List<WorkmateEntity>> workmateEntitiesMutableLiveData = new MutableLiveData<>();
        List<WorkmateEntity> workmateEntities = TestValues.getThreeTestWorkmateEntities();
        workmateEntitiesMutableLiveData.setValue(workmateEntities);
        doReturn(workmateEntitiesMutableLiveData).when(getWorkmateEntitiesWithRestaurantChoiceListUseCase).invoke();

        //When
        List<WorkmateEntity> result = getValueForTesting(getWorkmateEntitiesGoingToSameRestaurantUseCase.invoke(TestValues.TEST_RESTAURANT_ID));

        //Then
        assertEquals(3, result.size());
        assertEquals(workmateEntities, result);
    }

    @Test
    public void FourWorkmatesWithRestaurantChoice_threeWorkmatesGoingToSameRestaurant() {
        //Given
        MutableLiveData<List<WorkmateEntity>> workmateEntitiesMutableLiveData = new MutableLiveData<>();
        List<WorkmateEntity> workmateEntities = TestValues.getThreeTestWorkmateEntities();
        WorkmateEntity workmateGoingToBK = new WorkmateEntity(
            new LoggedUserEntity(
                "WORKMATE_ID",
                "WORKMATE_NAME",
                "WORKMATE_EMAIL",
                "WORKMATE_PHOTO_URL"
            ),
            "BK",
            TestValues.ATTENDING_RESTAURANT_NAME,
            TestValues.ATTENDING_RESTAURANT_VICINITY
        );
        workmateEntities.add(workmateGoingToBK);
        workmateEntities.add(workmateGoingToBK);
        workmateEntitiesMutableLiveData.setValue(workmateEntities);
        doReturn(workmateEntitiesMutableLiveData).when(getWorkmateEntitiesWithRestaurantChoiceListUseCase).invoke();

        //When
        List<WorkmateEntity> result = getValueForTesting(
            getWorkmateEntitiesGoingToSameRestaurantUseCase.invoke(TestValues.TEST_RESTAURANT_ID)
        );

        //Then
        assertEquals(3, result.size());

        //When
         result = getValueForTesting(
            getWorkmateEntitiesGoingToSameRestaurantUseCase.invoke("BK")
        );

         //Then
        assertEquals(2, result.size());
    }
}
