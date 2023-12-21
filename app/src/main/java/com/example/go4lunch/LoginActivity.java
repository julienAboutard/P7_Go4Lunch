package com.example.go4lunch;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.LoginActivityBinding;

import dagger.hilt.android.AndroidEntryPoint;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private LoginActivityBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        //viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

    }
}
