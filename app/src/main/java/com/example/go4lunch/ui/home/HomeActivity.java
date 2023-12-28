package com.example.go4lunch.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.HomeActivityBinding;
import com.example.go4lunch.ui.dispatcher.DispatcherActivity;
import com.example.go4lunch.ui.map.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    private HomeActivityBinding binding;

    private HomeViewModel viewModel;

    public static Intent navigate(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setSupportActionBar(binding.homeToolbar);
        setFragmentObserver();
        initNavigationDrawer();
        initBottomNavigator();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.home_navigation_item_logout) {
            firebaseAuth.signOut();
            startActivity(DispatcherActivity.navigate(HomeActivity.this));
        } else if (item.getItemId() == R.id.home_navigation_item_lunch) {
            int a = 4;
        } else if (item.getItemId() == R.id.home_navigation_item_settings) {
            int b= 5;
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

    private void setFragmentObserver() {

        viewModel.getFragmentListSingleLiveEvent().observe(this, fragmentList -> {
            switch (fragmentList) {
                case MAP_FRAGMENT:
                    changeFragment(MapFragment.newInstance());
            }
        });
    }

    private void initBottomNavigator() {

        BottomNavigationView bottomNavigationView = binding.homeBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.bottom_bar_map);

        binding.homeBottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bottom_bar_map) {
                    viewModel.onChangeFragmentView(FragmentList.MAP_FRAGMENT);
                } else if (item.getItemId() == R.id.bottom_bar_restaurant_list) {
                    viewModel.onChangeFragmentView(FragmentList.LIST_FRAGMENT);
                } else if (item.getItemId() == R.id.bottom_bar_workmate_list) {
                    viewModel.onChangeFragmentView(FragmentList.WORKMATES_FRAGMENT);
                } else if (item.getItemId() == R.id.bottom_bar_chat_list) {
                    viewModel.onChangeFragmentView(FragmentList.CHAT_FRAGMENT);
                }
                return false;
            }
        });
    }
    private void changeFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.home_frame_layout, fragment)
            .commit();
    }
}
