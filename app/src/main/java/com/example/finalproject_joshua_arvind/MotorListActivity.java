package com.example.finalproject_joshua_arvind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MotorListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motor_list); // Tampilkan layout motor_list.xml

        // Contoh tombol untuk beralih ke Order
        findViewById(R.id.orderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MotorListActivity.this, OrderActivity.class));
            }
        });
    }
}