package com.example.go4lunch.ui.map;

import static android.content.ContentValues.TAG;
import static com.example.go4lunch.ui.utils.BitmapFromVector.getBitmapFromVector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.data.gps.entity.LocationEntity;
import com.example.go4lunch.data.gps.entity.LocationEntityWrapper;
import com.example.go4lunch.ui.map.marker.OnMarkerClickedListener;
import com.example.go4lunch.ui.map.marker.RestaurantMarkerViewStateItem;
import com.example.go4lunch.ui.restaurant.detail.RestaurantDetailsActivity;
import com.example.go4lunch.ui.workmatelist.OnWorkmateClickedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, OnMarkerClickedListener {

    private MapViewModel viewModel;
    private final List<Marker> markers = new ArrayList<>();

    private Marker userMarker = null;

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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this.requireContext(), R.raw.clean_map));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);

        viewModel.getMapViewState().observe(getViewLifecycleOwner(), mapViewState -> {
                clearMarkers();
                for (RestaurantMarkerViewStateItem item : mapViewState) {
                    MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(item.getLatLng().latitude, item.getLatLng().longitude))
                        .title(item.getName())
                        .icon(getBitmapFromVector(getContext(), R.drawable.restaurant_location,item.getColorAttendance()));

                    Marker marker = googleMap.addMarker(markerOptions);
                    if (marker != null) {
                        marker.setTag(item.getId());
                    }

                    markers.add(marker);
                }

                googleMap.setOnInfoWindowClickListener(
                    marker -> {
                        String restaurantId = (String) marker.getTag();
                        if (restaurantId != null) {
                            onMarkerClicked(restaurantId);
                        }
                    }
                );
            }
        );

        viewModel.getLocationState().observe(getViewLifecycleOwner(), locationEntityWrapper -> {
                if (locationEntityWrapper instanceof LocationEntityWrapper.GpsProviderEnabled) {

                    if (userMarker != null) {
                        userMarker.remove();
                    }

                    LocationEntity location = ((LocationEntityWrapper.GpsProviderEnabled) locationEntityWrapper).locationEntity;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    float zoomLevel = 15f;

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(zoomLevel)
                        .build();

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    googleMap.moveCamera(cameraUpdate);
                }
            }
        );
    }

    @Override
    public void onMarkerClicked(@NonNull String restaurantId) {
        startActivity(RestaurantDetailsActivity.navigate(requireContext(), restaurantId));
    }

    private void clearMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }


}
