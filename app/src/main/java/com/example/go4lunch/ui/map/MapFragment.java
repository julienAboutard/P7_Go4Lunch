package com.example.go4lunch.ui.map;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.gps.entity.LocationStateEntity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private MapViewModel viewModel;

    @NonNull
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        viewModel.getLocationState().observe(getViewLifecycleOwner(), locationState -> {
            LocationEntity location = ((LocationStateEntity.GpsProviderEnabled) locationState).locationEntity;

            googleMap.addMarker(
                new MarkerOptions()
                    .position(
                        new LatLng(
                            location.getLatitude(),
                            location.getLongitude()
                        )
                    )
                    .title("user")
                    .alpha(0.8f)
            );
        });

    }
}
