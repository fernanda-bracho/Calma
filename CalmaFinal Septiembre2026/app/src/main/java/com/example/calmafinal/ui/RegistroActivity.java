package com.example.calmafinal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calmafinal.R;
import com.example.calmafinal.data.dao.UsuarioDao;
import com.example.calmafinal.data.database.BaseDatos;
import com.example.calmafinal.data.mapper.UsuarioMapper;
import com.example.calmafinal.domain.model.Usuario;
import com.example.calmafinal.domain.model.UsuarioParticular;
import com.example.calmafinal.domain.service.ServicioHashContrasena;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etEmailRegistro, etPasswordRegistro;
    private Button btnRegistrarse;

    private BaseDatos db;
    private final ServicioHashContrasena hasher = new ServicioHashContrasena();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = BaseDatos.getDatabase(getApplicationContext());

        etNombre = findViewById(R.id.etNombre);
        etEmailRegistro = findViewById(R.id.etEmailRegistro);
        etPasswordRegistro = findViewById(R.id.etPasswordRegistro);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        btnRegistrarse.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        final String nombre = etNombre.getText().toString().trim();
        final String email = etEmailRegistro.getText().toString().trim();
        final String password = etPasswordRegistro.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            UsuarioDao usuarioDao = db.usuarioDao();

            if (usuarioDao.obtenerPorCorreo(email) != null) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Ya existe una cuenta con ese correo.", Toast.LENGTH_SHORT).show());
                return;
            }

            long id;
            try {
                // El formulario aún no pide código de empresa: todo registro nuevo es B2C.
                // Cuando se agregue el campo, aquí se decide entre Empleado y UsuarioParticular.
                Usuario nuevo = new UsuarioParticular(0, nombre, email, hasher.hashear(password), "");
                id = usuarioDao.insertar(UsuarioMapper.aEntidad(nuevo));
            } catch (Exception e) {
                id = -1;
            }

            final long idFinal = id;
            runOnUiThread(() -> {
                if (idFinal > 0) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Error al registrar.", Toast.LENGTH_SHORT).show();
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
