package com.example.go4lunch.data.detailsretaurant.response;

import androidx.annotation.Nullable;

import com.example.go4lunch.data.nearbysearchrestaurants.response.PhotosItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class ResultItem {

    @SerializedName("place_id")
    @Nullable
    private final String placeId;

    @SerializedName("name")
    @Nullable
    private final String name;

    @SerializedName("vicinity")
    @Nullable
    private final String vicinity;

    @SerializedName("website")
    @Nullable
    private final String website;

    @SerializedName("rating")
    @Nullable
    private final Float rating;

    @SerializedName("photos")
    @Nullable
    private final List<PhotosItem> photos;

    @SerializedName("formatted_phone_number")
    @Nullable
    private final String formattedPhoneNumber;

    public ResultItem(
        @Nullable String placeId,
        @Nullable String name,
        @Nullable String vicinity,
        @Nullable String website,
        @Nullable Float rating,
        @Nullable List<PhotosItem> photos,
        @Nullable String formattedPhoneNumber
    ) {
        this.placeId = placeId;
        this.name = name;
        this.vicinity = vicinity;
        this.website = website;
        this.rating = rating;
        this.photos = photos;
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    @Nullable
    public String getPlaceId() {
        return placeId;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getVicinity() {
        return vicinity;
    }

    @Nullable
    public String getWebsite() {
        return website;
    }

    @Nullable
    public Float getRating() {
        return rating;
    }

    @Nullable
    public List<PhotosItem> getPhotos() {
        return photos;
    }

    @Nullable
    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultItem that = (ResultItem) o;
        return Objects.equals(placeId, that.placeId) && Objects.equals(name, that.name) && Objects.equals(vicinity, this.vicinity) &&
            Objects.equals(website, that.website) && Objects.equals(rating, that.rating) &&
            Objects.equals(photos, that.photos) && Objects.equals(formattedPhoneNumber, that.formattedPhoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            placeId,
            name,
            vicinity,
            website,
            rating,
            photos,
            formattedPhoneNumber
        );
    }

    @Override
    public String toString() {
        return "ResultItem{" +
            "placeId='" + placeId + '\'' +
            ", name='" + name + '\'' +
            ", vicinity='" + vicinity + '\'' +
            ", website='" + website + '\'' +
            ", rating=" + rating +
            ", photos=" + photos +
            ", formattedPhoneNumber='" + formattedPhoneNumber + '\'' +
            '}';
    }
}
