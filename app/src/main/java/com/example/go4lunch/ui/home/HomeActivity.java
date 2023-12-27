package com.example.go4lunch.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.HomeActivityBinding;
import com.example.go4lunch.ui.dispatcher.DispatcherActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    private HomeActivityBinding binding;

    public static Intent navigate(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.homeToolbar);
        initNavigationDrawer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_navigation_item_logout) {
            firebaseAuth.signOut();
            startActivity(DispatcherActivity.navigate(HomeActivity.this));
        }
        return true;
    }

    private void initNavigationDrawer(  ) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this,
            binding.homeDrawerLayout,
            binding.homeToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );

        binding.homeDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.homeNavigationView.bringToFront();
        binding.homeNavigationView.setNavigationItemSelectedListener(this);
    }
}
