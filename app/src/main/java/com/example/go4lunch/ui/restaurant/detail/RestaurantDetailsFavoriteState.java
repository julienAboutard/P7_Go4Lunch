package com.example.go4lunch.ui.restaurant.detail;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.example.go4lunch.R;

public enum RestaurantDetailsFavoriteState {

    IS_FAVORITE(R.drawable.twotone_favorite_24, R.color.ok_green_pale),
    IS_NOT_FAVORITE(R.drawable.twotone_favorite_24, R.color.black);

    @DrawableRes
    private final int drawableRes;

    @ColorRes
    private final int iconColorRes;

    RestaurantDetailsFavoriteState(
        int drawableRes,
        int iconColorRes
    ) {
        this.drawableRes = drawableRes;
        this.iconColorRes = iconColorRes;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public int getIconColorRes() {
        return iconColorRes;
    }

    @NonNull
    @Override
    public String toString() {
        return "RestaurantDetailsFavoriteState{" +
            "drawableRes=" + drawableRes +
            ", iconColorRes=" + iconColorRes +
            '}';
    }
}
