package com.example.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class GetAttendantsGoingToSameRestaurantAsUserUseCase {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    @Inject
    public GetAttendantsGoingToSameRestaurantAsUserUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase
    ) {
        this.userRepository = userRepository;
        this.getCurrentLoggedUserIdUseCase = getCurrentLoggedUserIdUseCase;
    }

    public LiveData<Map<String, Integer>> invoke() {
        LiveData<List<UserWithRestaurantChoiceEntity>> userWithRestaurantChoiceEntitiesLiveData = userRepository.getUsersWithRestaurantChoiceEntities();

        return Transformations.map(userWithRestaurantChoiceEntitiesLiveData, userWithRestaurantChoiceEntities -> {
                Map<String, Integer> attendantsByRestaurantIdsMap = new HashMap<>();
                String currentUserId = getCurrentLoggedUserIdUseCase.invoke();
                if (userWithRestaurantChoiceEntities != null) {
                    for (UserWithRestaurantChoiceEntity userWithRestaurantChoice : userWithRestaurantChoiceEntities) {
                        if (!userWithRestaurantChoice.getId().equals(currentUserId)) {
                            String restaurantId = userWithRestaurantChoice.getAttendingRestaurantId();
                            Integer count = attendantsByRestaurantIdsMap.get(restaurantId);
                            int totalCount = count != null ? count : 0;
                            attendantsByRestaurantIdsMap.put(restaurantId, totalCount + 1);
                        }
                    }
                }
                return attendantsByRestaurantIdsMap;
            }
        );
    }
}
