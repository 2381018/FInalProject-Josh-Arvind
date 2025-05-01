package com.example.finalproject_joshua_arvind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_joshua_arvind.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2000; // 2 seconds
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "SekolahSabatPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is logged in
                if (isLoggedIn()) {
                    // User is logged in, go to MainActivity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // User is not logged in, go to LoginActivity
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_TIMEOUT);
    }

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}