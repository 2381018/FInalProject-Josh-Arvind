package com.example.finalproject_joshua_arvind; // Pastikan package ini benar

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity"; // Tag untuk logging

    private EditText editFullName, editEmail, editPassword;
    private Button buttonRegister;
    private ProgressDialog progressDialog; // Tetap digunakan, tapi deprecated

    // Executor untuk background task (pengganti AsyncTask)
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    // Handler untuk memposting hasil ke Main Thread
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register); // Pastikan nama layout XML benar

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register"); // Set judul toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editFullName = findViewById(R.id.edit_full_name); // Pastikan ID benar
        editEmail = findViewById(R.id.edit_email);       // Pastikan ID benar
        editPassword = findViewById(R.id.edit_password);   // Pastikan ID benar
        buttonRegister = findViewById(R.id.button_register); // Pastikan ID benar

        // Listener untuk tombol kembali di toolbar
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Lambda lebih ringkas

        // Listener untuk tombol register
        buttonRegister.setOnClickListener(v -> attemptRegistration());
    }

    private void attemptRegistration() {
        // Ambil input dari pengguna
        String fullName = editFullName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Validasi input
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return; // Hentikan jika ada field kosong
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            editEmail.setError("Invalid Email"); // Beri tanda error
            editEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            editPassword.setError("Too short"); // Beri tanda error
            editPassword.requestFocus();
            return;
        }

        // Hilangkan error sebelumnya (jika ada)
        editFullName.setError(null);
        editEmail.setError(null);
        editPassword.setError(null);

        // Mulai proses registrasi di background thread
        registerUser(fullName, email, password);
    }

    private void showProgressDialog() {
        // TODO: Pertimbangkan mengganti ProgressDialog dengan ProgressBar di layout
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registering...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false); // Pengguna tidak bisa membatalkan
        }
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void registerUser(final String fullName, final String email, final String password) {
        showProgressDialog();

        executorService.execute(() -> {
            // --- Ini berjalan di Background Thread ---
            String result = null;
            try {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();

                // *** PENTING: Pastikan key ini ('fullname', 'email', 'password')
                // *** SESUAI dengan yang diharapkan oleh script PHP Anda ($_POST[...])
                params.put("fullname", fullName);
                params.put("email", email);
                params.put("password", password);

                // *** PENTING: Pastikan konfigurasi.URL_REGISTER benar!
                // *** Gunakan IP Address lokal komputer XAMPP Anda, bukan localhost.
                // *** Contoh: "http://192.168.1.10/proyek_anda/register.php"
                result = requestHandler.sendPostRequest(konfigurasi.URL_REGISTER, params);

            } catch (Exception e) {
                Log.e(TAG, "Error during network request", e);
                // Set result ke pesan error atau null untuk menandakan kegagalan koneksi
                result = "{\"success\": false, \"message\": \"Network Error: " + e.getMessage() + "\"}"; // Contoh JSON error
            }

            // Kirim hasil kembali ke Main Thread untuk update UI
            final String finalResult = result; // Harus final untuk diakses di dalam lambda/runnable
            mainThreadHandler.post(() -> {
                // --- Ini berjalan di Main Thread ---
                dismissProgressDialog();
                handleRegistrationResponse(finalResult);
            });
        });
    }

    private void handleRegistrationResponse(String jsonResponse) {
        Log.d(TAG, "Raw Server Response: " + jsonResponse); // Log respons mentah

        if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
            Toast.makeText(this, "Failed to connect or empty response from server.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            boolean success = jsonObject.optBoolean("success"); // optBoolean aman jika key tidak ada
            String message = jsonObject.optString("message", "An unknown error occurred."); // Default message

            Toast.makeText(this, message, Toast.LENGTH_LONG).show(); // Tampilkan pesan dari server

            if (success) {
                // Registrasi berhasil, kembali ke layar sebelumnya atau ke login
                Log.i(TAG, "Registration successful for email: " + editEmail.getText().toString());
                // Contoh: Kembali ke activity sebelumnya
                finish();
                // Atau buka LoginActivity
                // Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(intent);
                // finish(); // Tutup activity ini
            } else {
                // Registrasi gagal, pesan sudah ditampilkan dari server
                Log.w(TAG, "Registration failed: " + message);
                // Anda bisa menambahkan logika lain jika perlu, misal fokus ke field tertentu
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + jsonResponse, e);
            // Jika respons bukan JSON yang valid, tampilkan pesan error generik
            // atau coba tampilkan respons mentah (hati-hati jika mengandung HTML/error teknis)
            if(jsonResponse.toLowerCase().contains("<html>")){
                Toast.makeText(this, "Registration failed. Server returned an unexpected response.", Toast.LENGTH_LONG).show();
            } else {
                // Tampilkan potongan respons jika tidak terlalu panjang
                String displayResponse = jsonResponse.length() > 100 ? jsonResponse.substring(0, 100) + "..." : jsonResponse;
                Toast.makeText(this, "Registration failed: " + displayResponse , Toast.LENGTH_LONG).show();
            }
        }
    }

    // Ingat untuk membersihkan resource jika activity dihancurkan
    // Meskipun untuk single thread executor sederhana mungkin tidak krusial
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Matikan executor jika tidak diperlukan lagi (opsional untuk single execution)
        // executorService.shutdown();
        dismissProgressDialog(); // Pastikan dialog hilang jika activity hancur
    }
}