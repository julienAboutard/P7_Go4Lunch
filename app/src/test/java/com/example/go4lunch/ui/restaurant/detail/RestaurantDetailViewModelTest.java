package com.example.go4lunch.ui.restaurant.detail;

import static com.example.go4lunch.utils.TestUtils.getValueForTesting;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantEntity;
import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantWrapper;
import com.example.go4lunch.data.user.entity.UserEntity;
import com.example.go4lunch.data.workmate.WorkmateEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;
import com.example.go4lunch.domain.chosedrestaurant.AddUserRestaurantChoiceUseCase;
import com.example.go4lunch.domain.chosedrestaurant.RemoveUserRestaurantChoiceUseCase;
import com.example.go4lunch.domain.detailrestaurant.GetDetailsRestaurantWrapperUseCase;
import com.example.go4lunch.domain.user.AddFavoriteRestaurantUseCase;
import com.example.go4lunch.domain.user.GetUserEntityUseCase;
import com.example.go4lunch.domain.user.RemoveFavoriteRestaurantUseCase;
import com.example.go4lunch.domain.workmate.GetWorkmateEntitiesGoingToSameRestaurantUseCase;
import com.example.go4lunch.ui.workmatelist.WorkmatesViewStateItem;
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantDetailViewModelTest {

    private static final String KEY_RESTAURANT_ID = "KEY_RESTAURANT_ID";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetDetailsRestaurantWrapperUseCase getDetailsRestaurantWrapperUseCase;

    @Mock
    private Resources resources;

    @Mock
    private AddFavoriteRestaurantUseCase addFavoriteRestaurantUseCase;

    @Mock
    private RemoveFavoriteRestaurantUseCase removeFavoriteRestaurantUseCase;

    @Mock
    private AddUserRestaurantChoiceUseCase addUserRestaurantChoiceUseCase;

    @Mock
    private RemoveUserRestaurantChoiceUseCase removeUserRestaurantChoiceUseCase;

    @Mock
    private GetWorkmateEntitiesGoingToSameRestaurantUseCase getWorkmateEntitiesGoingToSameRestaurantUseCase;

    @Mock
    private GetUserEntityUseCase getUserEntityUseCase;

    @Mock
    private GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    @Mock
    private SavedStateHandle savedStateHandle;

    private final MutableLiveData<DetailsRestaurantWrapper> detailsRestaurantWrapperMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<UserEntity> userEntityMutableLiveData = new MutableLiveData<>();

    private RestaurantDetailViewModel viewModel;

    @Before
    public void setUp() {
        doReturn("KEY_RESTAURANT_ID").when(savedStateHandle).get(KEY_RESTAURANT_ID);
        doReturn(detailsRestaurantWrapperMutableLiveData).when(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        userEntityMutableLiveData.setValue(TestValues.getCurrentUserEntity());

        doReturn(userEntityMutableLiveData).when(getUserEntityUseCase).invoke();
        doReturn(TestValues.TEST_USER_ID).when(getCurrentLoggedUserIdUseCase).invoke();

        viewModel = new RestaurantDetailViewModel(
            getDetailsRestaurantWrapperUseCase,
            resources,
            addFavoriteRestaurantUseCase,
            removeFavoriteRestaurantUseCase,
            addUserRestaurantChoiceUseCase,
            removeUserRestaurantChoiceUseCase,
            getWorkmateEntitiesGoingToSameRestaurantUseCase,
            getUserEntityUseCase,
            getCurrentLoggedUserIdUseCase,
            savedStateHandle
        );
    }

    @Test
    public void detailsRestaurantWrapperSuccess_RestaurantDetailViewStateReturnsDetailsStateItem() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(TestValues.getTestDetailsRestaurantWrapperSuccess());
        userEntityMutableLiveData.setValue(
            new UserEntity(
                TestValues.getTestLoggedUserEntity(),
                Collections.emptySet(),
                null
            )
        );

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertEquals(TestValues.getTestRestaurantDetailViewState(), result);
        assertTrue(result instanceof RestaurantDetailViewState.RestaurantDetail);
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void detailsRestaurantWrapperSuccessWithAttendingWorkmate_RestaurantDetailViewStateReturnsDetailsStateItem() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(TestValues.getTestDetailsRestaurantWrapperSuccess());
        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertEquals(TestValues.getTestRestaurantDetailViewState(), result);
        assertTrue(result instanceof RestaurantDetailViewState.RestaurantDetail);
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }


    @Test
    public void detailsRestaurantWrapperSuccess_phoneNumberIsNotNull() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(TestValues.getTestDetailsRestaurantWrapperSuccess());

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertEquals(TestValues.TEST_RESTAURANT_PHONE_NUMBER, ((RestaurantDetailViewState.RestaurantDetail) result).getPhoneNumber());
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void detailsRestaurantWrapperSuccess_phoneNumberIsNull() {
        // Given
        DetailsRestaurantWrapper detailsRestaurantWrapper = new DetailsRestaurantWrapper.Success(
            new DetailsRestaurantEntity(
                TestValues.TEST_RESTAURANT_ID,
                TestValues.TEST_RESTAURANT_NAME,
                TestValues.TEST_RESTAURANT_VICINITY,
                TestValues.TEST_RESTAURANT_PHOTO_URL,
                TestValues.TEST_NEARBYSEARCH_RATING,
                null,
                TestValues.TEST_RESTAURANT_WEBSITE
            )
        );
        detailsRestaurantWrapperMutableLiveData.setValue(detailsRestaurantWrapper);

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertNull(((RestaurantDetailViewState.RestaurantDetail) result).getPhoneNumber());
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void detailsRestaurantWrapperSuccess_websiteUrlIsNotNull() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(TestValues.getTestDetailsRestaurantWrapperSuccess());

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertEquals(TestValues.TEST_RESTAURANT_WEBSITE, ((RestaurantDetailViewState.RestaurantDetail) result).getWebsiteUrl());
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void detailsRestaurantWrapperSuccess_websiteUrlIsNull() {
        // Given
        DetailsRestaurantWrapper detailsRestaurantWrapper = new DetailsRestaurantWrapper.Success(
            new DetailsRestaurantEntity(
                TestValues.TEST_RESTAURANT_ID,
                TestValues.TEST_RESTAURANT_NAME,
                TestValues.TEST_RESTAURANT_VICINITY,
                TestValues.TEST_RESTAURANT_PHOTO_URL,
                TestValues.TEST_NEARBYSEARCH_RATING,
                TestValues.TEST_RESTAURANT_WEBSITE,
                null
            )
        );
        detailsRestaurantWrapperMutableLiveData.setValue(detailsRestaurantWrapper);
        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertNull(((RestaurantDetailViewState.RestaurantDetail) result).getWebsiteUrl());
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void edge_case_detailsRestaurantWrapperIsNull() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(null);

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertNull(result);
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void edge_case_currentUserIsNull() {
        // Given
        userEntityMutableLiveData.setValue(null);

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertNull(result);
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void onAddFavoriteRestaurant_should_call_addFavoriteRestaurantUseCase() {
        // WHEN
        viewModel.onAddFavoriteRestaurant();
        // THEN
        verify(addFavoriteRestaurantUseCase).invoke(KEY_RESTAURANT_ID);
        verifyNoMoreInteractions(addFavoriteRestaurantUseCase);
    }

    @Test
    public void detailsRestaurantWrapperIsNull_shouldReturn() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(null);

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertNull(result);
    }


    @Test
    public void currentUserIsNull_shouldReturn() {
        // Given
        userEntityMutableLiveData.setValue(null);

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertNull(result);
    }

    @Test
    public void onRemoveFavoriteRestaurant_should_call_removeFavoriteRestaurantUseCase() {
        // WHEN
        viewModel.onRemoveFavoriteRestaurant();
        // THEN
        verify(removeFavoriteRestaurantUseCase).invoke(KEY_RESTAURANT_ID);
        verifyNoMoreInteractions(removeFavoriteRestaurantUseCase);
    }

    @Test
    public void onAddUserRestaurantChoice_should_call_addUserRestaurantChoiceUseCase() {
        // When
        viewModel.onAddUserRestaurantChoice(TestValues.TEST_RESTAURANT_NAME, TestValues.TEST_RESTAURANT_VICINITY, TestValues.TEST_RESTAURANT_PHOTO_URL);
        // Then
        verify(addUserRestaurantChoiceUseCase).invoke(KEY_RESTAURANT_ID, TestValues.TEST_RESTAURANT_NAME, TestValues.TEST_RESTAURANT_VICINITY, TestValues.TEST_RESTAURANT_PHOTO_URL);
        verifyNoMoreInteractions(addUserRestaurantChoiceUseCase);
    }

    @Test
    public void onRemoveUserRestaurantChoice_should_call_removeUserRestaurantChoiceUseCase() {
        // WHEN
        viewModel.onRemoveUserRestaurantChoice();
        // THEN
        verify(removeUserRestaurantChoiceUseCase).invoke();
        verifyNoMoreInteractions(removeUserRestaurantChoiceUseCase);
    }

    @Test
    public void detailsRestaurantWrapperLoading_RestaurantDetailViewStateReturnsLoadingStateItem() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(new DetailsRestaurantWrapper.Loading());

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertEquals(TestValues.getTestRestaurantDetailViewStateLoading(), result);
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void detailsRestaurantWrapperError_RestaurantDetailViewStateReturnsErrorStateItem() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(new DetailsRestaurantWrapper.RequestError(new Throwable("TEST_ERROR")));

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());

        // Then
        assertEquals(TestValues.getTestRestaurantDetailViewStateError(), result);
        assertEquals("TEST_ERROR", ((RestaurantDetailViewState.Error) result).getErrorMessage());
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void ratingOnFive_shouldReturnRatingOnThree() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(TestValues.getTestDetailsRestaurantWrapperSuccess());

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());
        Float rating = ((RestaurantDetailViewState.RestaurantDetail) result).getRating();

        // Then
        assertEquals(3.0f, rating, 0.0);
    }

    @Test
    public void ratingNull_shouldReturn0() {
        // Given
        detailsRestaurantWrapperMutableLiveData.setValue(
            new DetailsRestaurantWrapper.Success(
                new DetailsRestaurantEntity(
                    "KEY_RESTAURANT_ID",
                    TestValues.TEST_RESTAURANT_NAME,
                    TestValues.TEST_RESTAURANT_VICINITY,
                    TestValues.TEST_RESTAURANT_PHOTO_URL,
                    null,
                    TestValues.TEST_RESTAURANT_PHONE_NUMBER,
                    TestValues.TEST_RESTAURANT_WEBSITE
                )
            )
        );

        // When
        RestaurantDetailViewState result = getValueForTesting(viewModel.getRestaurantDetails());
        Float rating = ((RestaurantDetailViewState.RestaurantDetail) result).getRating();

        // Then
        assertEquals(0f, rating, 0.0);
    }

    @Test
    public void getWorkmatesViewStateItems() {
        // Given
        List<WorkmateEntity> workmateEntities = new ArrayList<>();
        WorkmateEntity workmate1 = TestValues.getTestWorkmateEntity();
        WorkmateEntity workmate2 = TestValues.getTestWorkmateEntity();
        WorkmateEntity workmate3 = TestValues.getTestWorkmateEntity();
        workmateEntities.add(workmate1);
        workmateEntities.add(workmate2);
        workmateEntities.add(workmate3);
        MutableLiveData<List<WorkmateEntity>> workmateEntitiesMutableLiveData = new MutableLiveData<>(workmateEntities);
        doReturn(workmateEntitiesMutableLiveData).when(getWorkmateEntitiesGoingToSameRestaurantUseCase).invoke(KEY_RESTAURANT_ID);

        // When
        List<WorkmatesViewStateItem> result = getValueForTesting(viewModel.getWorkmatesGoingToRestaurant());

        // Then
        assertEquals(3, result.size());
        verify(getWorkmateEntitiesGoingToSameRestaurantUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verify(getCurrentLoggedUserIdUseCase).invoke();
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }

    @Test
    public void workmateState_workmateGoing() {
        getWorkmatesViewStateItems();

        WorkmateState result = getValueForTesting(viewModel.getWorkerState());
        assertEquals(WorkmateState.WORKMATE_GOING, result);
    }

    @Test
    public void workmateState_noWorkmate() {
        MutableLiveData<List<WorkmateEntity>> workmateEntitiesMutableLiveData = new MutableLiveData<>(Collections.emptyList());
        doReturn(workmateEntitiesMutableLiveData).when(getWorkmateEntitiesGoingToSameRestaurantUseCase).invoke(KEY_RESTAURANT_ID);

        List<WorkmatesViewStateItem> result = getValueForTesting(viewModel.getWorkmatesGoingToRestaurant());
        WorkmateState resultWork = getValueForTesting(viewModel.getWorkerState());

        assertEquals(0, result.size());
        assertEquals(WorkmateState.NO_WORKMATE, resultWork);
    }

    @Test
    public void workmateState_isWorkmateGoingWhenWorkmatesAttending() {
        // Given
        List<WorkmateEntity> workmateEntities = new ArrayList<>();
        WorkmateEntity workmate1 = TestValues.getTestWorkmateEntity_currentUser();
        WorkmateEntity workmate2 = TestValues.getTestWorkmateEntity();
        WorkmateEntity workmate3 = TestValues.getTestWorkmateEntity();
        workmateEntities.add(workmate1);
        workmateEntities.add(workmate2);
        workmateEntities.add(workmate3);
        MutableLiveData<List<WorkmateEntity>> workmateEntitiesMutableLiveData = new MutableLiveData<>(workmateEntities);
        doReturn(workmateEntitiesMutableLiveData).when(getWorkmateEntitiesGoingToSameRestaurantUseCase).invoke(KEY_RESTAURANT_ID);

        // When
        List<WorkmatesViewStateItem> result = getValueForTesting(viewModel.getWorkmatesGoingToRestaurant());

        // Then
        assertEquals(2, result.size());
        verify(getWorkmateEntitiesGoingToSameRestaurantUseCase).invoke(KEY_RESTAURANT_ID);
        verify(getUserEntityUseCase).invoke();
        verify(getCurrentLoggedUserIdUseCase).invoke();
        verify(getDetailsRestaurantWrapperUseCase).invoke(KEY_RESTAURANT_ID);
        verifyNoMoreInteractions(getDetailsRestaurantWrapperUseCase, getUserEntityUseCase, getWorkmateEntitiesGoingToSameRestaurantUseCase);
    }
}
