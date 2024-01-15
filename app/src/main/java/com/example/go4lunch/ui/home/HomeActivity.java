package com.example.go4lunch.ui.home;

import static com.example.go4lunch.ui.home.HomeDisplayScreen.MAP_FRAGMENT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.HomeActivityBinding;
import com.example.go4lunch.databinding.HomeNavigationHeaderBinding;
import com.example.go4lunch.ui.dispatcher.DispatcherActivity;
import com.example.go4lunch.ui.map.MapFragment;
import com.example.go4lunch.ui.restaurant.list.RestaurantListFragment;
import com.example.go4lunch.ui.workmatelist.WorkmateListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
//public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
public class HomeActivity extends AppCompatActivity {

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

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.home_navigation_item_logout) {
            viewModel.signOut();
        } else if (item.getItemId() == R.id.home_navigation_item_lunch) {
            int a = 4;
        } else if (item.getItemId() == R.id.home_navigation_item_settings) {
            int b= 5;
        }
        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    private void initNavigationDrawer() {
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
        //binding.homeNavigationView.setNavigationItemSelectedListener(this);
    }

    private void setFragmentObserver() {

        HomeNavigationHeaderBinding navigationHeaderBinding = HomeNavigationHeaderBinding.bind(
            binding.homeNavigationView.getHeaderView(0)
        );

        viewModel.getUserInfoLiveData().observe(this, currentLoggedUser -> {
            Log.d("Controle", "mail "+currentLoggedUser.getEmail()+" name "+currentLoggedUser.getName()+" picture "+currentLoggedUser.getPictureUrl());
                Glide.with(this)
                    .load(currentLoggedUser.getPictureUrl())
                    .fallback(R.drawable.outline_person_24)
                    .error(R.drawable.baseline_error_outline_24)
                    .transform(new CenterCrop(), new RoundedCorners(25))
                    .into(navigationHeaderBinding.navHeaderAvatarUser);

                navigationHeaderBinding.navHeaderMailUser.setText(currentLoggedUser.getEmail());
                navigationHeaderBinding.navHeaderNameUser.setText(currentLoggedUser.getName());
            }
        );

        viewModel.getUserWithRestaurantChoice().observe(this, userWithRestaurantChoice -> {
                binding.homeNavigationView.setNavigationItemSelectedListener(
                    item -> {
                        if (item.getItemId() == R.id.home_navigation_item_lunch) {
                            if (userWithRestaurantChoice != null) {

                            } else {
                                Toast.makeText(
                                        this, R.string.toast_message_user_no_restaurant_chosen,
                                        Toast.LENGTH_SHORT)
                                    .show();
                            }
                        } else if (item.getItemId() == R.id.home_navigation_item_settings) {

                        } else if (item.getItemId() == R.id.home_navigation_item_logout) {
                            viewModel.signOut();
                        }
                        return true;
                    }
                );
            }
        );

        viewModel.getHomeDisplayScreenLiveEvent().observe(this, homeDisplayScreenEvent -> {
            HomeDisplayScreen homeDisplayScreen = homeDisplayScreenEvent.getContentIfNotHandled();

            if (homeDisplayScreen != null) {
                switch (homeDisplayScreen) {
                    case MAP_FRAGMENT:
                        changeFragment(MapFragment.newInstance());
                        break;

                    case LIST_FRAGMENT:
                        changeFragment(RestaurantListFragment.newInstance());
                        break;

                    case WORKMATES_FRAGMENT:
                        changeFragment(WorkmateListFragment.newInstance());
                        break;
                }
            }
        });

        viewModel.onUserLogged().observe(this, loggingState -> {
                if (!loggingState) {
                    startActivity(DispatcherActivity.navigate(HomeActivity.this));
                }
            }
        );
    }

    private void initBottomNavigator() {

        BottomNavigationView bottomNavigationView = binding.homeBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.bottom_bar_map);

        binding.homeBottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bottom_bar_map) {
                    viewModel.onChangeFragmentView(MAP_FRAGMENT);
                } else if (item.getItemId() == R.id.bottom_bar_restaurant_list) {
                    viewModel.onChangeFragmentView(HomeDisplayScreen.LIST_FRAGMENT);
                } else if (item.getItemId() == R.id.bottom_bar_workmate_list) {
                    viewModel.onChangeFragmentView(HomeDisplayScreen.WORKMATES_FRAGMENT);
                } else if (item.getItemId() == R.id.bottom_bar_chat_list) {
                    viewModel.onChangeFragmentView(HomeDisplayScreen.CHAT_FRAGMENT);
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
