package com.example.go4lunch.ui.map.marker;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class RestaurantMarkerViewStateItem {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final LatLng latLng;

    @ColorRes
    private final int colorAttendance;

    public RestaurantMarkerViewStateItem(@NonNull String id, @NonNull String name, @NonNull LatLng latLng, int colorAttendance) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
        this.colorAttendance = colorAttendance;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public LatLng getLatLng() {
        return latLng;
    }

    @ColorRes
    public int getColorAttendance() {
        return colorAttendance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantMarkerViewStateItem that = (RestaurantMarkerViewStateItem) o;
        return colorAttendance == that.colorAttendance && Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) && Objects.equals(latLng, that.latLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, latLng, colorAttendance);
    }

    @Override
    public String toString() {
        return "RestaurantMarkerViewStateItem{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", latLng=" + latLng +
            ", colorAttendance=" + colorAttendance +
            '}';
    }
}
