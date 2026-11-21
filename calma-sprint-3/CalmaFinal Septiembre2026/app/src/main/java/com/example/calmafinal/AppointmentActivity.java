package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class AppointmentActivity extends AppCompatActivity {

    private Button btnConfirmarCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        btnConfirmarCita = findViewById(R.id.btnConfirmarCita);

        btnConfirmarCita.setOnClickListener(v -> {
            Toast.makeText(this, "¡Cita agendada con éxito bajo el convenio corporativo!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}