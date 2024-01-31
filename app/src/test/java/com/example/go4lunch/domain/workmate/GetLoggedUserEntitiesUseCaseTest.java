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
import com.example.go4lunch.utils.TestValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetLoggedUserEntitiesUseCaseTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepository userRepository;

    private GetLoggedUserEntitiesUseCase getLoggedUserEntitiesUseCase;

    @Before
    public void setUp() {
        getLoggedUserEntitiesUseCase = new GetLoggedUserEntitiesUseCase(userRepository);
    }

    @Test
    public void testInvoke() {
        //Given
        MutableLiveData<List<LoggedUserEntity>> loggedUserEntitiesLiveData = new MutableLiveData<>();
        loggedUserEntitiesLiveData.setValue(TestValues.getFourTestLoggedUserEntities());
        doReturn(loggedUserEntitiesLiveData).when(userRepository).getLoggedUserEntitiesLiveData();

        //When
        List<LoggedUserEntity> result = getValueForTesting(getLoggedUserEntitiesUseCase.invoke());

        assertEquals(4, result.size());
        assertEquals(TestValues.getFourTestLoggedUserEntities(), result);
        verify(userRepository).getLoggedUserEntitiesLiveData();
        verifyNoMoreInteractions(userRepository);
    }
}
