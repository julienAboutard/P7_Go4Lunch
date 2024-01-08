package com.example.go4lunch.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.gps.entity.LocationStateEntity;
import com.example.go4lunch.ui.home.HomeActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng locationEntity = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(locationEntity).title("You"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationEntity));
        /*viewModel.getLocationState().observe(getViewLifecycleOwner(), locationState -> {
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

            Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();
        });*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
