package com.example.go4lunch.ui.onboarding;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.OnboardingActivityBinding;
import com.example.go4lunch.ui.dispatcher.DispatcherActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnBoardingActivity extends AppCompatActivity {

    private OnBoardingViewModel viewModel;

    private ActivityResultLauncher<String[]> permissionLauncher;

    public static Intent navigate(Context context) {
        return new Intent(context, OnBoardingActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnboardingActivityBinding binding = OnboardingActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OnBoardingViewModel.class);

        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
            }
        );

        viewModel.getOnBoardingViewAction().observe(this, viewAction -> {
                switch (viewAction) {
                    case CONTINUE_TO_AUTHENTICATION:
                        continueWithPermissions();
                        break;
                    case ASK_GPS_PERMISSION:
                        permissionLauncher.launch(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.POST_NOTIFICATIONS
                            }
                        );
                        break;
                    case SHOW_RATIONALE:
                        showRequestPermissionRationale();
                        break;
                    case GO_APP_SETTINGS:
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        break;
                }
            }
        );

        binding.onboardingAllowButton.setOnClickListener(v -> viewModel.onAllowClicked(
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
        )
        );
    }

    private void showRequestPermissionRationale() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.show_rationale_title)
            .setMessage(R.string.show_rationale_permission_message)
            .setPositiveButton(R.string.change_settings_dialog_button, (dialog, which) -> viewModel.onChangeAppSettingsClicked()
            )
            .setCancelable(false)
            .create()
            .show();
    }

    private void continueWithPermissions() {
        startActivity(DispatcherActivity.navigate(OnBoardingActivity.this));
        finish();
    }
}
