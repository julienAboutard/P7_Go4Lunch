package com.example.go4lunch.data.nearbysearchrestaurants.response;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    @SerializedName("open_now")
    private boolean openNow;

    public Boolean isOpenNow() {
        return openNow;
    }
}
