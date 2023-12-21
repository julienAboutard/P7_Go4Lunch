package com.example.go4lunch.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.LoginActivityBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    public static Intent navigate(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginActivityBinding viewBinding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }
}
