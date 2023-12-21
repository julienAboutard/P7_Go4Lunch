package com.example.go4lunch.ui.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Event<T> {

    @NonNull
    private final T content;

    private boolean hasBeenHandled = false;

    public Event(@NonNull T content) {
        this.content = content;
    }

    @Nullable
    public T getContentIfNotHandled() {
        if (!hasBeenHandled) {
            hasBeenHandled = true;
            return content;
        } else {
            return null;
        }
    }
}
