package com.example.go4lunch.domain.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.data.notification.entity.NotificationEntity;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.domain.authentification.GetCurrentLoggedUserIdUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetNotificationEntityUseCase {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase;

    @Inject
    public GetNotificationEntityUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetCurrentLoggedUserIdUseCase getCurrentLoggedUserIdUseCase
    ) {
        this.userRepository = userRepository;
        this.getCurrentLoggedUserIdUseCase = getCurrentLoggedUserIdUseCase;
    }

    @Nullable
    public NotificationEntity invoke() {
        List<UserWithRestaurantChoiceEntity> usersWithRestaurantChoiceEntities = userRepository.getUsersWithRestaurantChoiceEntitiesAsync();
        String currentLoggedUserId = getCurrentLoggedUserIdUseCase.invoke();

        List<String> workmatesGoingToSameRestaurantAsUser = new ArrayList<>();
        String restaurantId = null;
        String restaurantName = null;
        String restaurantVicinity = null;

        if (usersWithRestaurantChoiceEntities != null) {

            for (UserWithRestaurantChoiceEntity user : usersWithRestaurantChoiceEntities) {
                if (user.getId().equals(currentLoggedUserId)) {
                    restaurantId = user.getAttendingRestaurantId();
                    restaurantName = user.getAttendingRestaurantName();
                    restaurantVicinity = user.getAttendingRestaurantVicinity();

                    for (UserWithRestaurantChoiceEntity workmate : usersWithRestaurantChoiceEntities) {
                        if (!workmate.getId().equals(user.getId()) && workmate.getAttendingRestaurantId().equals(restaurantId)) {
                            workmatesGoingToSameRestaurantAsUser.add(workmate.getAttendingUsername());
                        }
                    }
                }
            }
            if (restaurantId != null) {
                return new NotificationEntity(
                    restaurantName,
                    restaurantId,
                    restaurantVicinity,
                    workmatesGoingToSameRestaurantAsUser
                );
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
