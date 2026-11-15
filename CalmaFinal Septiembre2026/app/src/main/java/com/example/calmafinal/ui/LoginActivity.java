package com.example.calmafinal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calmafinal.R;
import com.example.calmafinal.data.dao.UsuarioDao;
import com.example.calmafinal.data.database.BaseDatos;
import com.example.calmafinal.data.entities.UsuarioEntity;
import com.example.calmafinal.data.mapper.UsuarioMapper;
import com.example.calmafinal.domain.model.Usuario;
import com.example.calmafinal.domain.service.ServicioHashContrasena;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreoLogin, etPasswordLogin;
    private Button btnIniciarSesion;
    private TextView tvRegister, tvForgotPassword;

    private BaseDatos db;
    private final ServicioHashContrasena hasher = new ServicioHashContrasena();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = BaseDatos.getDatabase(getApplicationContext());

        etCorreoLogin = findViewById(R.id.etCorreoLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class)));

        tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RecuperarActivity.class)));
    }

    private void iniciarSesion() {
        final String correo = etCorreoLogin.getText().toString().trim();
        final String password = etPasswordLogin.getText().toString().trim();

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Por favor, ingresa un correo válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            UsuarioDao usuarioDao = db.usuarioDao();
            UsuarioEntity entidad = usuarioDao.obtenerPorCorreo(correo);

            // El hash es costoso (PBKDF2): se verifica aquí, en el hilo de fondo.
            final Usuario usuario = (entidad != null && hasher.verificar(password, entidad.getPasswordHash()))
                    ? UsuarioMapper.aDominio(entidad)
                    : null;

            runOnUiThread(() -> {
                if (usuario != null) {
                    Toast.makeText(this, "¡Bienvenido/a, " + usuario.getNombreCompleto() + "!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(HomeActivity.EXTRA_USUARIO_ID, usuario.getId());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
