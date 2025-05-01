package com.example.finalproject_joshua_arvind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MotorListActivity extends AppCompatActivity implements View.OnClickListener {

    // Deklarasikan view yang akan digunakan
    private CardView cbrCard, varioCard, supraCard;
    private Button orderButton;

    // Variabel untuk melacak CardView dan nama motor yang dipilih
    private CardView selectedCard = null;
    private String selectedMotorcycleName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motor_list); // Pastikan layout XML sudah benar

        // Cari view berdasarkan ID dari layout XML Anda
        cbrCard = findViewById(R.id.cbr_2016_card);
        varioCard = findViewById(R.id.vario_2020_card);
        supraCard = findViewById(R.id.supra_x_2020_card);
        orderButton = findViewById(R.id.orderbutton);

        // Validasi view
        if (cbrCard == null || varioCard == null || supraCard == null || orderButton == null) {
            Toast.makeText(this, "Error: Satu atau lebih view tidak ditemukan di layout!", Toast.LENGTH_LONG).show();
            return;
        }

        // Set OnClickListener untuk semua CardView dan Button
        cbrCard.setOnClickListener(this);
        varioCard.setOnClickListener(this);
        supraCard.setOnClickListener(this);
        orderButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.cbr_2016_card) {
            handleSelection(cbrCard, "CBR 2016");
        } else if (id == R.id.vario_2020_card) {
            handleSelection(varioCard, "Vario 2020");
        } else if (id == R.id.supra_x_2020_card) {
            handleSelection(supraCard, "Supra X 2020");
        } else if (id == R.id.orderbutton) {
            processOrder();
        }
    }

    /**
     * Menangani logika ketika sebuah CardView motor dipilih atau dibatalkan pilihannya.
     * Hanya melacak motor yang dipilih tanpa manipulasi warna.
     */
    private void handleSelection(CardView newlySelectedCard, String motorcycleName) {
        // Jika klik ulang pada motor yang sudah dipilih, batalkan pilihan
        if (selectedCard == newlySelectedCard) {
            selectedCard = null;
            selectedMotorcycleName = null;
        } else {
            selectedCard = newlySelectedCard;
            selectedMotorcycleName = motorcycleName;
        }
    }

    /**
     * Memproses tindakan ketika tombol "Pesan Sekarang" diklik.
     * Memeriksa apakah motor sudah dipilih, lalu menavigasi ke OrderActivity.
     */
    private void processOrder() {
        if (selectedCard == null || selectedMotorcycleName == null) {
            Toast.makeText(this, "Silakan pilih motor terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MotorListActivity.this, OrderActivity.class);
            intent.putExtra("SELECTED_MOTORCYCLE_NAME", selectedMotorcycleName);
            startActivity(intent);
        }
    }
}