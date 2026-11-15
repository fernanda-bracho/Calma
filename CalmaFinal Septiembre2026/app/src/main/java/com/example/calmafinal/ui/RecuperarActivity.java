package com.example.calmafinal.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calmafinal.R;
import com.example.calmafinal.data.dao.UsuarioDao;
import com.example.calmafinal.data.database.BaseDatos;
import com.example.calmafinal.data.entities.UsuarioEntity;
import com.example.calmafinal.domain.service.ServicioHashContrasena;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecuperarActivity extends AppCompatActivity {

    private EditText etCorreoRecuperar, etNuevaPassword;
    private Button btnVerificarCorreo, btnCambiarPassword;
    private LinearLayout layoutNuevaPassword;

    private BaseDatos db;
    private final ServicioHashContrasena hasher = new ServicioHashContrasena();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String correoValidado = ""; // Almacena temporalmente el correo verificado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        db = BaseDatos.getDatabase(getApplicationContext());

        etCorreoRecuperar = findViewById(R.id.etCorreoRecuperar);
        etNuevaPassword = findViewById(R.id.etNuevaPassword);
        btnVerificarCorreo = findViewById(R.id.btnEnviarCorreo);
        btnCambiarPassword = findViewById(R.id.btnCambiarPassword);
        layoutNuevaPassword = findViewById(R.id.layoutNuevaPassword);

        // Ocultar el campo de nueva contraseña al iniciar la pantalla
        if (layoutNuevaPassword != null) {
            layoutNuevaPassword.setVisibility(View.GONE);
        }
        if (btnCambiarPassword != null) {
            btnCambiarPassword.setVisibility(View.GONE);
        }

        btnVerificarCorreo.setOnClickListener(v -> verificarCorreoExistente());

        if (btnCambiarPassword != null) {
            btnCambiarPassword.setOnClickListener(v -> actualizarContrasena());
        }
    }

    private void verificarCorreoExistente() {
        final String correo = etCorreoRecuperar.getText().toString().trim();

        if (correo.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingresa un formato de correo válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            UsuarioEntity usuario = db.usuarioDao().obtenerPorCorreo(correo);

            runOnUiThread(() -> {
                if (usuario != null) {
                    correoValidado = correo;
                    Toast.makeText(this, "¡Correo verificado con éxito! Ingresa tu nueva contraseña.", Toast.LENGTH_LONG).show();

                    etCorreoRecuperar.setEnabled(false);
                    btnVerificarCorreo.setVisibility(View.GONE);

                    if (layoutNuevaPassword != null) {
                        layoutNuevaPassword.setVisibility(View.VISIBLE);
                    }
                    if (btnCambiarPassword != null) {
                        btnCambiarPassword.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(this, "El correo ingresado no está registrado en CALMA+.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void actualizarContrasena() {
        final String nuevaPassword = etNuevaPassword.getText().toString().trim();

        if (nuevaPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa una nueva contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nuevaPassword.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            UsuarioDao dao = db.usuarioDao();
            dao.actualizarPasswordHash(correoValidado, hasher.hashear(nuevaPassword));

            runOnUiThread(() -> {
                Toast.makeText(this, "¡Contraseña actualizada correctamente!", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad y regresa al Login
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
