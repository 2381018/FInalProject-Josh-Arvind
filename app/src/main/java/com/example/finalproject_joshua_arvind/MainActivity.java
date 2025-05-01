package com.example.finalproject_joshua_arvind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends BaseAuthActivity {

    private TextView welcomeText;
    private static final String PREF_NAME = "SekolahSabatPrefs";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MWB Rental");

        // Initialize views
        welcomeText = findViewById(R.id.welcomeText);

        // Get username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "User");

        // Set welcome message
        welcomeText.setText("Welcome, " + username + "!");

        Button goToListMotor = findViewById(R.id.motorList);
        goToListMotor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MotorListActivity.class);
            startActivity(intent);
        });
    }
}