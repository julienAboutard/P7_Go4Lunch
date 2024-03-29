package com.example.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.workmate.WorkmateEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase {

    @NonNull
    private final GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase;

    @NonNull
    private final MediatorLiveData<List<WorkmateEntity>> workmateEntitiesWithAndWithoutRestaurantChoiceMediatorLiveData = new MediatorLiveData<>();

    @Inject
    public GetWorkmateEntitiesWithAndWithoutRestaurantChoiceUseCase(
        @NonNull GetWorkmateEntitiesWithRestaurantChoiceListUseCase getWorkmateEntitiesWithRestaurantChoiceListUseCase,
        @NonNull GetCurrentLoggedUserUseCase getCurrentLoggedUserUseCase,
        @NonNull GetLoggedUserEntitiesUseCase getLoggedUserEntitiesUseCase
    ) {
        this.getCurrentLoggedUserUseCase = getCurrentLoggedUserUseCase;

        LiveData<List<WorkmateEntity>> workmateEntitiesWithRestaurantChoiceLiveData = getWorkmateEntitiesWithRestaurantChoiceListUseCase.invoke();
        LiveData<List<LoggedUserEntity>> loggedUserEntitiesLiveData = getLoggedUserEntitiesUseCase.invoke();

        workmateEntitiesWithAndWithoutRestaurantChoiceMediatorLiveData.addSource(workmateEntitiesWithRestaurantChoiceLiveData, workmateEntitiesWithRestaurantChoice -> combine(workmateEntitiesWithRestaurantChoice, loggedUserEntitiesLiveData.getValue())
        );

        workmateEntitiesWithAndWithoutRestaurantChoiceMediatorLiveData.addSource(loggedUserEntitiesLiveData, loggedUserEntities -> combine(workmateEntitiesWithRestaurantChoiceLiveData.getValue(), loggedUserEntities)
        );
    }

    public LiveData<List<WorkmateEntity>> invoke() {
        return workmateEntitiesWithAndWithoutRestaurantChoiceMediatorLiveData;
    }

    private void combine(
        @Nullable List<WorkmateEntity> workmateEntitiesWithRestaurantChoice,
        @Nullable List<LoggedUserEntity> loggedUserEntities
    ) {
        LoggedUserEntity currentLoggedUser = getCurrentLoggedUserUseCase.invoke();

        if (currentLoggedUser == null || loggedUserEntities == null) {
            return;
        }

        List<WorkmateEntity> workmateEntitiesWithAndWithoutRestaurantChoice = new ArrayList<>();

        // Map workmate entities with restaurant choice
        if (workmateEntitiesWithRestaurantChoice != null) {
            for (WorkmateEntity workmateEntity : workmateEntitiesWithRestaurantChoice) {
                if (!workmateEntity.getLoggedUserEntity().getId().equals(currentLoggedUser.getId())) {
                    workmateEntitiesWithAndWithoutRestaurantChoice.add(workmateEntity);
                }
            }
        }

        // Map logged user entities without restaurant choice
        for (LoggedUserEntity loggedUserEntity : loggedUserEntities) {
            if (!loggedUserEntity.getId().equals(currentLoggedUser.getId())) {
                boolean hasRestaurantChoice = false;
                if (workmateEntitiesWithRestaurantChoice != null) {
                    hasRestaurantChoice = workmateEntitiesWithRestaurantChoice.stream()
                        .anyMatch(entity -> entity.getLoggedUserEntity().getId().equals(loggedUserEntity.getId()));
                }

                if (!hasRestaurantChoice) {
                    workmateEntitiesWithAndWithoutRestaurantChoice.add(
                        new WorkmateEntity(
                            new LoggedUserEntity(
                                loggedUserEntity.getId(),
                                loggedUserEntity.getName(),
                                loggedUserEntity.getEmail(),
                                loggedUserEntity.getPictureUrl()
                            ),
                            null,
                            null,
                            null
                        )
                    );
                }
            }
        }
        workmateEntitiesWithAndWithoutRestaurantChoiceMediatorLiveData.setValue(workmateEntitiesWithAndWithoutRestaurantChoice);
    }
}
