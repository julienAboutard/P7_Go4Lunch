package com.example.go4lunch.ui.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.ui.home.HomeActivity;
import com.example.go4lunch.ui.login.LoginActivity;
import com.example.go4lunch.ui.navigation.Destination;
import com.example.go4lunch.ui.utils.Event;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DispatcherActivity extends AppCompatActivity {

    public static Intent navigate(Context context) {
        return new Intent(context, DispatcherActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DispatcherViewModel viewModel = new ViewModelProvider(this).get(DispatcherViewModel.class);

        viewModel.getDestinationLiveData().observe(this, event -> {
            Destination destination = event.getContentIfNotHandled();
            if (destination!= null) {
                switch (destination) {
                    case HOME:
                        startActivity(HomeActivity.navigate(DispatcherActivity.this));
                        break;
                    case LOGIN:
                        startActivity(LoginActivity.navigate(DispatcherActivity.this));
                        break;
                }
            }
        });
    }
}
