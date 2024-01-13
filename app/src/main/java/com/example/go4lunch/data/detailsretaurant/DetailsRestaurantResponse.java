package com.example.go4lunch.data.detailsretaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailsRestaurantResponse {

    @SerializedName("result")
    @Expose
    @Nullable
    private final ResultItem result;

    @SerializedName("html_attributions")
    @Nullable
    private final List<Object> htmlAttributions;

    @SerializedName("status")
    @Nullable
    private final String status;

    public DetailsRestaurantResponse(
        @Nullable ResultItem result,
        @Nullable List<Object> htmlAttributions,
        @Nullable String status
    ) {
        this.result = result;
        this.htmlAttributions = htmlAttributions;
        this.status = status;
    }

    @Nullable
    public ResultItem getResult() {
        return result;
    }

    @Nullable
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    @NonNull
    @Override
    public String toString() {
        return "DetailsRestaurantResponse{" +
            "result=" + result +
            ", htmlAttributions=" + htmlAttributions +
            ", status='" + status + '\'' +
            '}';
    }
}
