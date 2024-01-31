package com.example.go4lunch.utils;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.autocomplete.entity.PredictionEntity;
import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantEntity;
import com.example.go4lunch.data.detailsretaurant.entity.DetailsRestaurantWrapper;
import com.example.go4lunch.data.firebaseauth.entity.LoggedUserEntity;
import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.data.nearbysearchrestaurants.entity.NearbySearchRestaurantsEntity;
import com.example.go4lunch.data.notification.entity.NotificationEntity;
import com.example.go4lunch.data.user.entity.UserEntity;
import com.example.go4lunch.data.user.entity.UserWithRestaurantChoiceEntity;
import com.example.go4lunch.data.workmate.WorkmateEntity;
import com.example.go4lunch.ui.restaurant.detail.AttendanceState;
import com.example.go4lunch.ui.restaurant.detail.RestaurantDetailViewState;
import com.example.go4lunch.ui.restaurant.detail.RestaurantDetailsFavoriteState;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestValues {

    // region LoggedUserEntity
    public static final String TEST_USER_ID = "TEST_USER_ID";
    public static final String TEST_USER_NAME = "TEST_USER_NAME";
    public static final String TEST_USER_MAIL = "TEST_USER_MAIL";
    public static final String TEST_USER_PHOTO_URL = "TEST_USER_PHOTO_URL";

    public static LoggedUserEntity getTestLoggedUserEntity() {
        return new LoggedUserEntity(
            TEST_USER_ID,
            TEST_USER_NAME,
            TEST_USER_MAIL,
            TEST_USER_PHOTO_URL
        );
    }

    public static List<LoggedUserEntity> getFourTestLoggedUserEntities() {
        List<LoggedUserEntity> loggedUserEntityList = new ArrayList<>();
        loggedUserEntityList.add(
            new LoggedUserEntity(
                TEST_USER_ID + 1,
                TEST_USER_NAME,
                TEST_USER_MAIL,
                TEST_USER_PHOTO_URL
            )
        );
        loggedUserEntityList.add(
            new LoggedUserEntity(
                TEST_USER_ID + 2,
                TEST_USER_NAME,
                TEST_USER_MAIL,
                TEST_USER_PHOTO_URL
            )
        );
        loggedUserEntityList.add(
            new LoggedUserEntity(
                TEST_USER_ID + 3,
                TEST_USER_NAME,
                TEST_USER_MAIL,
                TEST_USER_PHOTO_URL
            )
        );
        loggedUserEntityList.add(
            new LoggedUserEntity(
                TEST_USER_ID + 4,
                TEST_USER_NAME,
                TEST_USER_MAIL,
                TEST_USER_PHOTO_URL
            )
        );
        return loggedUserEntityList;
    }
    // endregion

    // region IN RESTAURANT
    public static final String TEST_RESTAURANT_ID = "TEST_RESTAURANT_ID";

    public static final String TEST_RESTAURANT_NAME = "TEST_RESTAURANT_NAME";
    public static final String TEST_RESTAURANT_VICINITY = "TEST_RESTAURANT_VICINITY";
    public static final String TEST_RESTAURANT_PHOTO_URL = "TEST_RESTAURANT_PHOTO_URL";

    public static final Float TEST_RESTAURANT_RATING = 5f;
    public static final String TEST_RESTAURANT_PHONE_NUMBER = "TEST_RESTAURANT_PHONE_NUMBER";
    public static final String TEST_RESTAURANT_WEBSITE = "TEST_RESTAURANT_WEBSITE";

    // endregion IN

    // region OUT RESTAURANT
    public static DetailsRestaurantEntity getTestDetailsRestaurantEntity() {
        return new DetailsRestaurantEntity(
            TEST_RESTAURANT_ID,
            TEST_RESTAURANT_NAME,
            TEST_RESTAURANT_VICINITY,
            TEST_RESTAURANT_PHOTO_URL,
            TEST_RESTAURANT_RATING,
            TEST_RESTAURANT_PHONE_NUMBER,
            TEST_RESTAURANT_WEBSITE
        );
    }


    public static Set<String> getTestRestaurantIdSet(int index) {
        Set<String> restaurantIdSet = new HashSet<>();
        for (int i = 0; i < index; i++) {
            restaurantIdSet.add(TEST_RESTAURANT_ID + i);
        }
        return restaurantIdSet;
    }
    //endregion OUT

    // region LocationStateEntity

    public static final int TEST_RESTAURANT_RADIUS = 1_000;
    public static final String TEST_RESTAURANT_TYPE = "restaurant";

    public static final Double TEST_LATITUDE = 43.599998;
    public static final Double TEST_LONGITUDE = 1.43333;

    public static LocationEntityWrapper.GpsProviderEnabled getTestLocationEntityWrapperGpsEnabled() {
        return new LocationEntityWrapper.GpsProviderEnabled(
            new LocationEntity(
                TEST_LATITUDE,
                TEST_LONGITUDE
            )
        );
    }

    public static LocationEntityWrapper.GpsProviderDisabled getTestLocationEntityWrapperGpsDisabled() {
        return new LocationEntityWrapper.GpsProviderDisabled();
    }
    // endregion

    // region NearbySearchEntity

    public static final LatLng TEST_USER_LAT_LNG = new LatLng(TEST_LATITUDE, TEST_LONGITUDE);

    public static final LatLng TEST_NEARBYSEARCH_LAT_LNG = new LatLng(TEST_LATITUDE + 1, TEST_LONGITUDE + 1);

    public static final String TEST_NEARBYSEARCH_ID = "TEST_NEARBYSEARCH_ID";
    public static final String TEST_NEARBYSEARCH_NAME = "TEST_NEARBYSEARCH_NAME";
    public static final String TEST_NEARBYSEARCH_VICINITY = "TEST_NEARBYSEARCH_VICINITY";

    public static final Integer TEST_NEARBYSEARCH_DISTANCE = 1000;
    public static final String TEST_NEARBYSEARCH_PICTURE_URL = "TEST_NEARBYSEARCH_PHOTO_URL";
    public static final Float TEST_NEARBYSEARCH_RATING = 3.5f;
    public static final LocationEntity TEST_NEARBYSEARCH_LOCATION_ENTITY = new LocationEntity(
        TEST_NEARBYSEARCH_LAT_LNG.latitude,
        TEST_NEARBYSEARCH_LAT_LNG.longitude
    );
    public static final Boolean TEST_NEARBYSEARCH_OPEN_NOW = true;

    public static LocationEntity getTestUserLocationEntity() {
        return new LocationEntity(TEST_USER_LAT_LNG.latitude, TEST_USER_LAT_LNG.longitude);
    }

    public static List<NearbySearchRestaurantsEntity> getTestNearbySearchEntityList(int index) {
        List<NearbySearchRestaurantsEntity> nearbySearchRestaurantsEntities = new ArrayList<>();

        for (int i = 0; i < index; i++) {
            NearbySearchRestaurantsEntity nearbySearchRestaurantsEntity = new NearbySearchRestaurantsEntity(
                TEST_NEARBYSEARCH_ID + i,
                TEST_NEARBYSEARCH_NAME,
                TEST_NEARBYSEARCH_VICINITY,
                TEST_NEARBYSEARCH_PICTURE_URL,
                TEST_NEARBYSEARCH_RATING,
                new LocationEntity(TEST_NEARBYSEARCH_LOCATION_ENTITY.getLatitude() + i, TEST_NEARBYSEARCH_LOCATION_ENTITY.getLongitude() + i),
                TEST_NEARBYSEARCH_DISTANCE,
                TEST_NEARBYSEARCH_OPEN_NOW
            );
            nearbySearchRestaurantsEntities.add(nearbySearchRestaurantsEntity);
        }
        return nearbySearchRestaurantsEntities;
    }

    public static String ATTENDING_USER_ID = "ATTENDING_USER_ID";
    public static String ATTENDING_USER_NAME = "ATTENDING_USER_NAME";
    public static String ATTENDING_RESTAURANT_ID = "ATTENDING_RESTAURANT_ID";
    public static String ATTENDING_RESTAURANT_NAME = "ATTENDING_RESTAURANT_NAME";
    public static String ATTENDING_RESTAURANT_VICINITY = "ATTENDING_RESTAURANT_VICINITY";
    public static Timestamp TIMESTAMP = new Timestamp(1686691200, 0);

    public static UserWithRestaurantChoiceEntity getTestUserWithDifferentRestaurantChoiceEntity(String restaurantId) {
        return new UserWithRestaurantChoiceEntity(
            ATTENDING_USER_ID,
            TIMESTAMP,
            ATTENDING_USER_NAME,
            ATTENDING_RESTAURANT_ID + restaurantId,
            ATTENDING_RESTAURANT_NAME,
            ATTENDING_RESTAURANT_VICINITY
        );
    }

    public static UserWithRestaurantChoiceEntity getTestUserWithSameRestaurantChoiceEntity() {
        return new UserWithRestaurantChoiceEntity(
            ATTENDING_USER_ID,
            TIMESTAMP,
            ATTENDING_USER_NAME,
            ATTENDING_RESTAURANT_ID,
            ATTENDING_RESTAURANT_NAME,
            ATTENDING_RESTAURANT_VICINITY
        );
    }



    public static UserWithRestaurantChoiceEntity getTestCurrentUserWithRestaurantChoiceEntity() {
        return new UserWithRestaurantChoiceEntity(
            TEST_USER_ID,
            TIMESTAMP,
            ATTENDING_USER_NAME,
            ATTENDING_RESTAURANT_ID,
            ATTENDING_RESTAURANT_NAME,
            ATTENDING_RESTAURANT_VICINITY
        );
    }

    public static UserWithRestaurantChoiceEntity getSpecifiedUserWithRestaurantChoiceEntity(@NonNull int number) {
        return new UserWithRestaurantChoiceEntity(
            TEST_USER_ID + number,
            TIMESTAMP,
            TEST_USER_NAME + number,
            TEST_RESTAURANT_ID,
            TEST_RESTAURANT_NAME,
            TEST_RESTAURANT_VICINITY
        );
    }
    // endregion

    // region NotificationEntity
    public static NotificationEntity getTestNotificationEntity(List<String> workmateNamesGoingToSameRestaurant) {
        return new NotificationEntity(
            ATTENDING_RESTAURANT_NAME,
            ATTENDING_RESTAURANT_ID,
            ATTENDING_RESTAURANT_VICINITY,
            workmateNamesGoingToSameRestaurant
        );
    }

    // endregion

    // region DetailsRestaurantWrapper

    public static DetailsRestaurantWrapper getTestDetailsRestaurantWrapperLoading() {
        return new DetailsRestaurantWrapper.Loading();
    }

    public static DetailsRestaurantWrapper getTestDetailsRestaurantWrapperError() {
        return new DetailsRestaurantWrapper.RequestError(
            new Exception("TEST_ERROR")
        );
    }

    public static DetailsRestaurantWrapper.Success getTestDetailsRestaurantWrapperSuccess() {
        return new DetailsRestaurantWrapper.Success(
            new DetailsRestaurantEntity(
                "KEY_RESTAURANT_ID",
                TEST_RESTAURANT_NAME,
                TEST_RESTAURANT_VICINITY,
                TEST_RESTAURANT_PHOTO_URL,
                TEST_RESTAURANT_RATING,
                TEST_RESTAURANT_PHONE_NUMBER,
                TEST_RESTAURANT_WEBSITE
            )
        );
    }
    // endregion

    // region RestaurantDetailViewState
    public static RestaurantDetailViewState getTestRestaurantDetailViewState() {
        return new RestaurantDetailViewState.RestaurantDetail(
            "KEY_RESTAURANT_ID",
            TEST_RESTAURANT_NAME,
            TEST_RESTAURANT_VICINITY,
            null,
            3.0f,
            TEST_RESTAURANT_PHONE_NUMBER,
            TEST_RESTAURANT_WEBSITE,
            AttendanceState.IS_NOT_ATTENDING,
            RestaurantDetailsFavoriteState.IS_NOT_FAVORITE,
            true,
            true
        );
    }

    public static RestaurantDetailViewState getTestRestaurantDetailViewStateError() {
        return new RestaurantDetailViewState.Error(
            "TEST_ERROR"
        );
    }

    public static RestaurantDetailViewState getTestRestaurantDetailViewStateLoading() {
        return new RestaurantDetailViewState.Loading();
    }
    // endregion

    // region WorkmateEntity
    public static WorkmateEntity getTestWorkmateEntity_currentUser() {
        return new WorkmateEntity(
            getTestLoggedUserEntity(),
            "KEY_RESTAURANT_ID",
            ATTENDING_RESTAURANT_NAME,
            ATTENDING_RESTAURANT_VICINITY
        );
    }

    public static WorkmateEntity getTestWorkmateEntity() {
        return new WorkmateEntity(
            new LoggedUserEntity(
                "WORKMATE_ID",
                "WORKMATE_NAME",
                "WORKMATE_EMAIL",
                "WORKMATE_PHOTO_URL"
            ),
            TEST_RESTAURANT_ID,
            ATTENDING_RESTAURANT_NAME,
            ATTENDING_RESTAURANT_VICINITY
        );
    }

    public static WorkmateEntity getTestSpecifiedWorkmateEntity(@NonNull int number) {
        return new WorkmateEntity(
            new LoggedUserEntity(
                "TEST_USER_ID"+number,
                "WORKMATE_NAME",
                "WORKMATE_EMAIL",
                "WORKMATE_PHOTO_URL"
            ),
            TEST_RESTAURANT_ID+number,
            ATTENDING_RESTAURANT_NAME+number,
            ATTENDING_RESTAURANT_VICINITY+number
        );
    }
    public static List<WorkmateEntity> getThreeTestWorkmateEntities() {
        List<WorkmateEntity> workmateEntities = new ArrayList<>();
        workmateEntities.add(getTestWorkmateEntity());
        workmateEntities.add(getTestWorkmateEntity());
        workmateEntities.add(getTestWorkmateEntity());
        return workmateEntities;
    }

    public static UserEntity getCurrentUserEntity() {
        return new UserEntity(
            getTestLoggedUserEntity(),
            getTestRestaurantIdSet(4),
            TEST_RESTAURANT_ID
        );
    }
    // endregion

    // region PredictionEntity
    public static String TEST_PREDICTION_ID = "TEST_NEARBYSEARCH_ID";

    public static List<PredictionEntity> getPredictionEntityList() {
        List<PredictionEntity> predictionEntities = new ArrayList<>();
        predictionEntities.add(new PredictionEntity("TEST_NEARBYSEARCH_ID0", "TEST_DESCRIPTION_1"));
        predictionEntities.add(new PredictionEntity("TEST_NEARBYSEARCH_ID1", "TEST_DESCRIPTION_2"));
        predictionEntities.add(new PredictionEntity("TEST_NEARBYSEARCH_ID2", "TEST_DESCRIPTION_3"));
        predictionEntities.add(new PredictionEntity("TEST_NEARBYSEARCH_ID3", "TEST_DESCRIPTION_3"));
        return predictionEntities;
    }
    // endregion
}
