package com.example.calmafinal.ui;

import com.example.calmafinal.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EmergencyActivity extends AppCompatActivity {

    private Button btnEmergenciaSos;
    private LinearLayout tarjetaSoporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        btnEmergenciaSos = findViewById(R.id.btnEmergenciaSos);
        tarjetaSoporte = findViewById(R.id.tarjetaSoporte);

        btnEmergenciaSos.setOnClickListener(v -> {
            Toast.makeText(this, "🚨 ¡Alerta SOS activada! Notificando a contactos corporativos y de emergencia...", Toast.LENGTH_LONG).show();
        });

        tarjetaSoporte.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo canales de soporte corporativo Calma+ Empresas...", Toast.LENGTH_SHORT).show();
        });
    }
}