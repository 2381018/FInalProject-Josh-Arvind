package com.example.finalproject_joshua_arvind;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mulai halaman login pertama kali
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish(); // Tutup MainActivity agar tidak kembali lagi
    }
}