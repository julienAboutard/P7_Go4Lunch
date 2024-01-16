package com.example.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.user.UserRepository;

import java.util.List;

import javax.inject.Inject;

public class GetLoggedUserEntitiesUseCase {

    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetLoggedUserEntitiesUseCase(@NonNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<List<LoggedUserEntity>> invoke() {
        return userRepository.getLoggedUserEntitiesLiveData();
    }
}
