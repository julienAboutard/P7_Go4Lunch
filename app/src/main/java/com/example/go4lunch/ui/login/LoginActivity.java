package com.example.go4lunch.ui.login;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.LoginActivityBinding;
import com.example.go4lunch.ui.dispatcher.DispatcherActivity;
import com.example.go4lunch.ui.signup.SignupActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;

    FirebaseAuth firebaseAuth;

    private LoginViewModel viewModel;

    public static Intent navigate(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                    if (signInAccountTask.isSuccessful()) {
                        try {
                            GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                            if (googleSignInAccount != null) {
                                AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                                FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            viewModel.onLoginComplete();
                                            startActivity(DispatcherActivity.navigate(LoginActivity.this));
                                            finish();
                                            Log.i(TAG, "Firebase auth google successful");
                                        } else {
                                            Log.e("Firebase auth error: ", task.getException().getMessage());
                                        }
                                    }
                                });
                            }
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        LoginActivityBinding viewBinding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        viewBinding.loginMailTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onMailChanged(s.toString());
            }
        });

        viewBinding.loginPwdTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onPasswordChanged(s.toString());
            }
        });

        viewBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onLoginButton().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(DispatcherActivity.navigate(LoginActivity.this));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        viewBinding.newUserMessage.setOnClickListener(view -> {
            startActivity(SignupActivity.navigate(LoginActivity.this));
            finish();
        });
        signInGoogle(viewBinding);

        // GITHUB SIGN IN
        viewBinding.githubFloatBtn.setOnClickListener(view -> {
                OAuthProvider.Builder provider = OAuthProvider
                    .newBuilder("github.com");
                Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
                if (pendingResultTask != null) {
                    pendingResultTask
                        .addOnSuccessListener(
                            authResult -> {
                                startActivity(DispatcherActivity.navigate(this));
                                finish();
                            }
                        )
                        .addOnFailureListener(
                            e -> {
                                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure of pendingResultTask: " + e.getMessage());
                            }
                        );
                } else {
                    firebaseAuth
                        .startActivityForSignInWithProvider(this, provider.build())
                        .addOnSuccessListener(
                            authResult -> {
                                {
                                    viewModel.onLoginComplete();
                                    startActivity(DispatcherActivity.navigate(this));
                                    finish();
                                }
                            }
                        )
                        .addOnFailureListener(
                            e -> {
                                Toast.makeText(LoginActivity.this, "Something went wrong!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure of startActivityForSignInWithProvider: " + e.getMessage());
                            }
                        );
                }
            }
        );

        // Twitter/X Sign In
        viewBinding.twitterXFloatBtn.setOnClickListener(view -> {
                OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
                Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
                if (pendingResultTask != null) {
                    pendingResultTask
                        .addOnSuccessListener(
                            authResult -> {
                                startActivity(DispatcherActivity.navigate(this));
                                finish();
                            }
                        )
                        .addOnFailureListener(
                            e -> {
                                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure of pendingResultTask: " + e.getMessage());
                            }
                        );
                } else {
                    firebaseAuth
                        .startActivityForSignInWithProvider(this, provider.build())
                        .addOnSuccessListener(
                            authResult -> {
                                {
                                    viewModel.onLoginComplete();
                                    startActivity(DispatcherActivity.navigate(this));
                                    finish();
                                }
                            }
                        )
                        .addOnFailureListener(
                            e -> {
                                Toast.makeText(LoginActivity.this, "Something went wrong!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure of startActivityForSignInWithProvider: " + e.getMessage());
                            }
                        );
                }
            }
        );
    }

    public void signInGoogle(LoginActivityBinding binding) {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        // GOOGLE SIGN IN
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);

        binding.googleFloatBtn.setOnClickListener(v -> {
                Intent intent = googleSignInClient.getSignInIntent();
                someActivityResultLauncher.launch(intent);
            }
        );
    }
}
