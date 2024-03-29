package com.example.go4lunch.data.user;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.dto.LoggedUserDto;
import com.example.go4lunch.data.user.dto.UserWithRestaurantChoiceDto;
import com.example.go4lunch.data.user.entity.ChosenRestaurantEntity;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreUserRepository implements UserRepository {

    private static final String USERS_COLLECTION = "users";
    private static final String USERS_WITH_RESTAURANT_CHOICE = "users_with_restaurant_choice";

    @NonNull
    private final FirebaseFirestore firestore;

    @Inject
    public FirestoreUserRepository(@NonNull FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void upsertLoggedUserEntity(@Nullable LoggedUserEntity loggedUserEntity) {
        if (loggedUserEntity != null) {
            DocumentReference userDocumentRef = firestore
                .collection(USERS_COLLECTION)
                .document(loggedUserEntity.getId());

            userDocumentRef
                .set(
                    new LoggedUserDto(
                        loggedUserEntity.getId(),
                        loggedUserEntity.getName(),
                        loggedUserEntity.getEmail(),
                        loggedUserEntity.getPictureUrl()
                    )
                )
                .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("UserRepositoryFirestore", "User document successfully created!");
                        } else {
                            Log.e("UserRepositoryFirestore", "Error creating user document: " + task.getException());
                        }
                    }
                );
        } else {
            Log.e("UserRepositoryFirestore", "Error creating user document: userEntity is null!");
        }
    }


    @Override
    public void upsertUserRestaurantChoice(
        @Nullable LoggedUserEntity loggedUserEntity,
        @NonNull ChosenRestaurantEntity chosenRestaurantEntity
    ) {
        if (loggedUserEntity != null) {
            String userId = loggedUserEntity.getId();


            firestore
                .collection(USERS_WITH_RESTAURANT_CHOICE)
                .document(userId)
                .set(
                    new UserWithRestaurantChoiceDto(
                        null,
                        loggedUserEntity.getName(),
                        chosenRestaurantEntity.getAttendingRestaurantId(),
                        chosenRestaurantEntity.getAttendingRestaurantName(),
                        chosenRestaurantEntity.getAttendingRestaurantVicinity()
                    )
                )
                .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("UserRepositoryFirestore", "User's restaurant choice document successfully created!");
                        } else {
                            Log.e("UserRepositoryFirestore", "Error creating user's restaurant choice document: " + task.getException());
                        }
                    }
                );
        } else {
            Log.e("UserRepositoryFirestore", "Error creating user document: userEntity is null!");
        }
    }

    @Override
    public LiveData<List<UserWithRestaurantChoiceEntity>> getUsersWithRestaurantChoiceEntities() {
        MutableLiveData<List<UserWithRestaurantChoiceEntity>> usersWithRestaurantChoiceEntitiesMutableLiveData = new MutableLiveData<>();

        LocalDateTime startDateTime = getTodayDateTime();
        Timestamp startTimestamp = getStartTimestamp(startDateTime);
        Timestamp endTimestamp = getEndTimestamp(startDateTime);

        firestore
            .collection(USERS_WITH_RESTAURANT_CHOICE)
            .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    if (error != null) {
                        Log.e("UserRepositoryFirestore", "Error fetching users documents: " + error);
                        usersWithRestaurantChoiceEntitiesMutableLiveData.setValue(null);
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        List<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntities = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            UserWithRestaurantChoiceDto userWithRestaurantChoiceDto = documentSnapshot.toObject(UserWithRestaurantChoiceDto.class);
                            Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");
                            if (timestamp != null &&
                                timestamp.compareTo(startTimestamp) >= 0 &&
                                timestamp.compareTo(endTimestamp) <= 0) {
                                String userId = documentSnapshot.getId();
                                UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity = mapToUserWithRestaurantChoiceEntity(userWithRestaurantChoiceDto, userId);
                                if (userWithRestaurantChoiceEntity != null) {
                                    userWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity);
                                }
                            }
                        }
                        if (userWithRestaurantChoiceEntities.isEmpty()) {
                            usersWithRestaurantChoiceEntitiesMutableLiveData.setValue(null);
                        } else {
                            usersWithRestaurantChoiceEntitiesMutableLiveData.setValue(userWithRestaurantChoiceEntities);
                        }
                    }
                }
            );
        return usersWithRestaurantChoiceEntitiesMutableLiveData;
    }

    public LiveData<UserWithRestaurantChoiceEntity> getUserWithRestaurantChoiceEntity(@NonNull String userId) {
        MutableLiveData<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntityMutableLiveData = new MutableLiveData<>();

        LocalDateTime startDateTime = getTodayDateTime();
        Timestamp startTimestamp = getStartTimestamp(startDateTime);
        Timestamp endTimestamp = getEndTimestamp(startDateTime);

        firestore
            .collection(USERS_WITH_RESTAURANT_CHOICE)
            .document(userId)
            .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("UserRepositoryFirestore", "Error fetching user document: " + error);
                        userWithRestaurantChoiceEntityMutableLiveData.setValue(null);
                        return;
                    }

                    if (documentSnapshot != null) {
                        UserWithRestaurantChoiceDto userWithRestaurantChoiceDto = documentSnapshot.toObject(UserWithRestaurantChoiceDto.class);
                        if (userWithRestaurantChoiceDto != null) {
                            Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");
                            if (timestamp != null &&
                                timestamp.compareTo(startTimestamp) >= 0 &&
                                timestamp.compareTo(endTimestamp) <= 0) {
                                UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity = mapToUserWithRestaurantChoiceEntity(userWithRestaurantChoiceDto, userId);
                                if (userWithRestaurantChoiceEntity != null) {
                                    userWithRestaurantChoiceEntityMutableLiveData.setValue(userWithRestaurantChoiceEntity);
                                }
                            } else {
                                userWithRestaurantChoiceEntityMutableLiveData.setValue(null);
                            }
                        } else {
                            userWithRestaurantChoiceEntityMutableLiveData.setValue(null);
                        }
                    }
                }
            );
        return userWithRestaurantChoiceEntityMutableLiveData;
    }

    @Override
    public void deleteUserRestaurantChoice(@Nullable LoggedUserEntity loggedUserEntity) {
        if (loggedUserEntity != null) {
            firestore.collection(USERS_WITH_RESTAURANT_CHOICE)
                .document(loggedUserEntity.getId())
                .delete()
                .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("UserRepositoryFirestore", "User's restaurant choice successfully deleted!");
                        } else {
                            Log.e("UserRepositoryFirestore", "Error deleting user's restaurant choice: " + task.getException());
                        }
                    }
                );
        } else {
            Log.e("UserRepositoryFirestore", "Error deleting user's restaurant choice: userEntity is null!");
        }
    }

    @Override
    public LiveData<List<LoggedUserEntity>> getLoggedUserEntitiesLiveData() {
        MutableLiveData<List<LoggedUserEntity>> loggedUserEntitiesMutableLiveData = new MutableLiveData<>();

        firestore
            .collection(USERS_COLLECTION)
            .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    if (error != null) {
                        Log.e("UserRepositoryFirestore", "Error fetching users documents: " + error);
                        loggedUserEntitiesMutableLiveData.setValue(null);
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        List<LoggedUserDto> loggedUserDtos = queryDocumentSnapshots.toObjects(LoggedUserDto.class);
                        List<LoggedUserEntity> loggedUserEntities = new ArrayList<>();
                        for (LoggedUserDto loggedUserDto : loggedUserDtos) {
                            if (loggedUserDto != null) {
                                loggedUserEntities.add(mapToLoggedUserEntity(loggedUserDto));
                            }
                        }
                        loggedUserEntitiesMutableLiveData.setValue(loggedUserEntities);
                    }
                }
            );
        return loggedUserEntitiesMutableLiveData;
    }

    @Override
    public List<UserWithRestaurantChoiceEntity> getUsersWithRestaurantChoiceEntitiesAsync() {
        TaskCompletionSource<List<UserWithRestaurantChoiceEntity>> taskCompletionSource = new TaskCompletionSource<>();

        LocalDateTime startDateTime = getTodayDateTime();
        Timestamp startTimestamp = getStartTimestamp(startDateTime);
        Timestamp endTimestamp = getEndTimestamp(startDateTime);

        firestore
            .collection(USERS_WITH_RESTAURANT_CHOICE)
            .get()
            .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<UserWithRestaurantChoiceEntity> userWithRestaurantChoiceEntities = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            UserWithRestaurantChoiceDto userWithRestaurantChoiceDto = documentSnapshot.toObject(UserWithRestaurantChoiceDto.class);
                            Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");
                            if (timestamp != null &&
                                timestamp.compareTo(startTimestamp) >= 0 &&
                                timestamp.compareTo(endTimestamp) <= 0) {
                                String userId = documentSnapshot.getId();
                                UserWithRestaurantChoiceEntity userWithRestaurantChoiceEntity = mapToUserWithRestaurantChoiceEntity(userWithRestaurantChoiceDto, userId);
                                if (userWithRestaurantChoiceEntity != null) {
                                    userWithRestaurantChoiceEntities.add(userWithRestaurantChoiceEntity);
                                }
                            }
                        }
                        if (userWithRestaurantChoiceEntities.isEmpty()) {
                            taskCompletionSource.setResult(null);
                        } else {
                            taskCompletionSource.setResult(userWithRestaurantChoiceEntities);
                        }
                    } else if (task.getException() != null) {
                        taskCompletionSource.setException(task.getException());
                    }
                }
            );
        try {
            return Tasks.await(taskCompletionSource.getTask());
        } catch (ExecutionException | InterruptedException e) {
            Log.e("UserRepositoryFirestore", "Error fetching users documents: " + e);
            return null;
        }
    }

    private LoggedUserEntity mapToLoggedUserEntity(@Nullable LoggedUserDto result) {
        if (result != null &&
            result.getId() != null &&
            result.getName() != null
        ) {
            return new LoggedUserEntity(
                result.getId(),
                result.getName(),
                result.getEmail(),
                result.getPictureUrl()
            );
        } else {
            return null;
        }
    }

    private UserWithRestaurantChoiceEntity mapToUserWithRestaurantChoiceEntity(
        UserWithRestaurantChoiceDto userWithRestaurantChoiceDto,
        String userId
    ) {
        if (userWithRestaurantChoiceDto.getTimestamp() != null &&
            userWithRestaurantChoiceDto.getAttendingUsername() != null &&
            userWithRestaurantChoiceDto.getAttendingRestaurantId() != null &&
            userWithRestaurantChoiceDto.getAttendingRestaurantName() != null &&
            userWithRestaurantChoiceDto.getAttendingRestaurantVicinity() != null
        ) {
            return new UserWithRestaurantChoiceEntity(
                userId,
                userWithRestaurantChoiceDto.getTimestamp(),
                userWithRestaurantChoiceDto.getAttendingUsername(),
                userWithRestaurantChoiceDto.getAttendingRestaurantId(),
                userWithRestaurantChoiceDto.getAttendingRestaurantName(),
                userWithRestaurantChoiceDto.getAttendingRestaurantVicinity()
            );
        } else {
            return null;
        }
    }

    private LocalDateTime getTodayDateTime() {
        return LocalDateTime.now();
    }

    private Timestamp getStartTimestamp(LocalDateTime todayDateTime) {
        LocalDateTime startDateTime;
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isBefore(LocalTime.of(12, 30))) {
            // If current time is before 12:30 PM, use yesterday's lunch as the start time
            startDateTime = todayDateTime.with(LocalTime.of(12, 30)).minusDays(1);
        } else {
            // Otherwise, use today's lunch as the start time
            startDateTime = todayDateTime.with(LocalTime.of(12, 30));
        }

        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDateTime, zone);
        return new Timestamp(startZonedDateTime.toInstant().getEpochSecond(), startZonedDateTime.toInstant().getNano());
    }

    private Timestamp getEndTimestamp(LocalDateTime todayDateTime) {
        LocalDateTime endDateTime;
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isBefore(LocalTime.of(12, 30))) {
            // If current time is before 12:30PM, use today's lunch as end time
            endDateTime = todayDateTime.with(LocalTime.of(12, 29, 59, 999999999));
        } else {
            // Otherwise, use tomorrow's lunch as end time
            endDateTime = todayDateTime.with(LocalTime.of(12, 29, 59, 999999999)).plusDays(1);
        }

        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDateTime, zone);
        return new Timestamp(endZonedDateTime.toInstant().getEpochSecond(), endZonedDateTime.toInstant().getNano());
    }
}
