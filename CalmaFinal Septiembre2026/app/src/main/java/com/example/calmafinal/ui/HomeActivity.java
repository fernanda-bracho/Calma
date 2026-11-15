package com.example.calmafinal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calmafinal.R;

public class HomeActivity extends AppCompatActivity {

    /** Id del usuario autenticado; lo envía LoginActivity. -1 si no llegó. */
    public static final String EXTRA_USUARIO_ID = "usuarioId";

    private Button btnMusica, btnRespiracion, btnContacto, btnPanico, btnCitas;
    private Button btnSubirBrillo, btnBajarBrillo;
    private float brilloActual = 0.5f;

    /** Usuario con sesión activa. Los módulos que dependen del usuario (contactos de emergencia, citas) lo reciben de aquí. */
    private long usuarioId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usuarioId = getIntent().getLongExtra(EXTRA_USUARIO_ID, -1);

        btnMusica = findViewById(R.id.btn_musica);
        btnRespiracion = findViewById(R.id.btn_respiracion);
        btnContacto = findViewById(R.id.btn_contacto);
        btnPanico = findViewById(R.id.btn_panico);
        btnCitas = findViewById(R.id.btn_citas);

        btnSubirBrillo = findViewById(R.id.btnSubirBrillo);
        btnBajarBrillo = findViewById(R.id.btnBajarBrillo);

        btnMusica.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, MusicaActivity.class)));

        btnRespiracion.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, RespiracionActivity.class)));

        btnContacto.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, PreviewWhatsAppActivity.class)));

        btnPanico.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, EmergencyActivity.class)));

        btnCitas.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, AppointmentActivity.class)));

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        if (layoutParams.screenBrightness >= 0) {
            brilloActual = layoutParams.screenBrightness;
        }

        btnSubirBrillo.setOnClickListener(v -> {
            brilloActual += 0.2f;
            if (brilloActual > 1.0f) {
                brilloActual = 1.0f;
                Toast.makeText(this, "Brillo al máximo ☀️", Toast.LENGTH_SHORT).show();
            }
            cambiarBrillo(brilloActual);
        });

        btnBajarBrillo.setOnClickListener(v -> {
            brilloActual -= 0.2f;
            if (brilloActual < 0.1f) {
                brilloActual = 0.1f;
                Toast.makeText(this, "Brillo al mínimo 🌙", Toast.LENGTH_SHORT).show();
            }
            cambiarBrillo(brilloActual);
        });
    }

    private void cambiarBrillo(float valorBrillo) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = valorBrillo;
        getWindow().setAttributes(layoutParams);
    }

    protected long getUsuarioId() {
        return usuarioId;
    }
}
