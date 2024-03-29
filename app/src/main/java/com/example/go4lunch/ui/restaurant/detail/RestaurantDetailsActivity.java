package com.example.go4lunch.ui.restaurant.detail;

import static com.example.go4lunch.ui.restaurant.detail.WorkmateState.NO_WORKMATE;
import static com.example.go4lunch.ui.restaurant.detail.WorkmateState.WORKMATE_GOING;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.RestaurantDetailActivityBinding;
import com.example.go4lunch.ui.workmatelist.OnWorkmateClickedListener;
import com.example.go4lunch.ui.workmatelist.WorkmateListAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantDetailsActivity extends AppCompatActivity {

    private RestaurantDetailActivityBinding binding;

    private RestaurantDetailViewModel viewModel;

    public static final String KEY_RESTAURANT_ID = "KEY_RESTAURANT_ID";

    public static Intent navigate(
        @NonNull Context context,
        @NonNull String restaurantId
    ) {
        Intent intent = new Intent(context, RestaurantDetailsActivity.class);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RestaurantDetailActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.detailRestaurantToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);

        initRecyclerView();
        setupObservers();
    }

    private void initRecyclerView() {
        WorkmateListAdapter adapter = new WorkmateListAdapter(restaurantId -> {
        }
        );

        binding.detailRestaurantWorkmatesList.setAdapter(adapter);
        binding.detailRestaurantWorkmatesList.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getWorkmatesGoingToRestaurant().observe(this, adapter::submitList
        );

        viewModel.getWorkerState().observe(this, workerState -> {
                if (workerState == WORKMATE_GOING) {
                    binding.detailNoWorkmatesTv.setVisibility(View.GONE);
                } else if (workerState == NO_WORKMATE) {
                    binding.detailNoWorkmatesTv.setVisibility(View.VISIBLE);
                }
            }
        );
    }

    private void setupObservers() {
        viewModel.getRestaurantDetails().observe(this, restaurantDetail -> {
                if (restaurantDetail instanceof RestaurantDetailViewState.Loading) {
                    binding.detailRestaurantLayout.setVisibility(View.GONE);
                    binding.detailRestaurantLoadingStateLayout.setVisibility(View.VISIBLE);
                }

                if (restaurantDetail instanceof RestaurantDetailViewState.RestaurantDetail) {
                    binding.detailRestaurantLayout.setVisibility(View.VISIBLE);
                    binding.detailRestaurantLoadingStateLayout.setVisibility(View.GONE);

                    RestaurantDetailViewState.RestaurantDetail detail = (RestaurantDetailViewState.RestaurantDetail) restaurantDetail;
                    binding.detailRestaurantName.setText(detail.getName());
                    binding.detailRestaurantAddress.setText(detail.getVicinity());
                    binding.detailRestaurantRatingBar.setRating(detail.getRating());
                    binding.detailRestaurantWebsiteButton.setEnabled(detail.isWebsiteAvailable());
                    binding.detailRestaurantCallButton.setEnabled(detail.isPhoneNumberAvailable());

                    AttendanceState attendanceState = detail.getAttendanceState();
                    if (attendanceState != null) {
                        binding.detailRestaurantChoseFab.setText(attendanceState.getText());
                        binding.detailRestaurantChoseFab.setBackgroundTintList(
                            ContextCompat.getColorStateList(
                                this,
                                attendanceState.getBackgroundColorRes()
                            )
                        );
                        binding.detailRestaurantChoseFab.setIconTint(
                            ContextCompat.getColorStateList(
                                this,
                                attendanceState.getIconColorRes()
                            )
                        );
                    }
                    if (attendanceState == AttendanceState.IS_ATTENDING) {
                        binding.detailRestaurantChoseFab.setOnClickListener(v -> viewModel.onRemoveUserRestaurantChoice());
                    } else if (attendanceState == AttendanceState.IS_NOT_ATTENDING) {
                        binding.detailRestaurantChoseFab.setOnClickListener(v ->
                            viewModel.onAddUserRestaurantChoice(
                                detail.getName(),
                                detail.getVicinity(),
                                detail.getPictureUrl()
                            )
                        );
                    }

                    RestaurantDetailsFavoriteState restaurantDetailsFavoriteState = detail.getRestaurantFavoriteState();
                    if (restaurantDetailsFavoriteState != null) {
                        binding.detailRestaurantLikeButton.setIcon(
                            ContextCompat.getDrawable(this,
                                restaurantDetailsFavoriteState.getDrawableRes()
                            )
                        );
                        binding.detailRestaurantLikeButton.setIconTint(
                            ContextCompat.getColorStateList(this,
                                restaurantDetailsFavoriteState.getIconColorRes()
                            )
                        );
                    }

                    if (restaurantDetailsFavoriteState == RestaurantDetailsFavoriteState.IS_FAVORITE) {
                        binding.detailRestaurantLikeButton.setOnClickListener(v -> viewModel.onRemoveFavoriteRestaurant()
                        );
                    } else if (detail.getRestaurantFavoriteState() == RestaurantDetailsFavoriteState.IS_NOT_FAVORITE) {
                        binding.detailRestaurantLikeButton.setOnClickListener(v -> viewModel.onAddFavoriteRestaurant()
                        );
                    }

                    Glide.with(this)
                        .load(detail.getPictureUrl())
                        .centerCrop()
                        .error(R.drawable.restaurant_table)
                        .fallback(R.drawable.restaurant_table)
                        .into(binding.detailRestaurantPicture);

                    binding.detailRestaurantWebsiteButton.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detail.getWebsiteUrl()));
                            startActivity(intent);
                        }
                    );

                    binding.detailRestaurantCallButton.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", detail.getPhoneNumber(), null));
                            startActivity(intent);
                        }
                    );
                }
            }
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
