package com.example.finalproject_joshua_arvind;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button btnMasuk;
    private TextView registerText;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "SekolahSabatPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Check if user is already logged in
        if (isLoggedIn()) {
            // User is already logged in, go to MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        btnMasuk = findViewById(R.id.btnMasuk);
        registerText = findViewById(R.id.registerText);

        // Set up login button click listener
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String username = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validate input
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Attempt login
                    login(username, password);
                }
            }
        });

        // Set up register text click listener
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(final String email, final String password) {
        class Login extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LoginActivity.this, "Logging In", "Please wait...", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                // Create request handler
                RequestHandler requestHandler = new RequestHandler();

                // Create post parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", email);
                params.put("password", password);

                // Make API request
                return requestHandler.sendPostRequest(konfigurasi.URL_LOGIN, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                try {
                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        // Login successful
                        String userId = jsonObject.getString("user_id");
                        String username = jsonObject.getString("username");

                        // Save login state and user info
                        saveLoginState(true, userId, username);

                        // Navigate to MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Login failed
                        String message = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Error: " + s);
                }
            }
        }

        // Execute the AsyncTask
        Login login = new Login();
        login.execute();
    }

    private void saveLoginState(boolean isLoggedIn, String userId, String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}