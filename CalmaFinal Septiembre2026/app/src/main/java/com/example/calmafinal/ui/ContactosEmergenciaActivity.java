package com.example.calmafinal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calmafinal.R;
import com.example.calmafinal.data.database.BaseDatos;
import com.example.calmafinal.data.entities.ContactoEmergenciaEntity;
import com.example.calmafinal.data.mapper.ContactoMapper;
import com.example.calmafinal.domain.model.ContactoEmergencia;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Registro de contactos de emergencia del usuario con sesión activa.
 *
 * Esta pantalla aún no tiene un acceso desde HomeActivity; al conectarla hay que enviar
 * {@link HomeActivity#EXTRA_USUARIO_ID} en el Intent.
 */
public class ContactosEmergenciaActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etRelacion;
    private Button btnGuardar;
    private BaseDatos db;
    private ExecutorService executorService;
    private long usuarioId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_emergencia);

        usuarioId = getIntent().getLongExtra(HomeActivity.EXTRA_USUARIO_ID, -1);

        etNombre = findViewById(R.id.etNombreContacto);
        etTelefono = findViewById(R.id.etTelefonoContacto);
        etRelacion = findViewById(R.id.etRelacionContacto);
        btnGuardar = findViewById(R.id.btnGuardarContacto);

        db = BaseDatos.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        btnGuardar.setOnClickListener(v -> guardarContacto());
    }

    private void guardarContacto() {
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String relacion = etRelacion.getText().toString().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || relacion.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuarioId < 0) {
            Toast.makeText(this, "Inicia sesión para guardar contactos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // El dominio valida los datos y la capa data los persiste ligados al usuario.
        final ContactoEmergencia contacto = new ContactoEmergencia(0, nombre, telefono, relacion);

        executorService.execute(() -> {
            db.contactoDao().insertar(ContactoMapper.aEntidad(usuarioId, contacto));
            runOnUiThread(() -> {
                Toast.makeText(this, "¡Contacto guardado con éxito!", Toast.LENGTH_SHORT).show();
                etNombre.setText("");
                etTelefono.setText("");
                etRelacion.setText("");
            });
        });
    }

    /**
     * Abre WhatsApp con el mensaje de alerta dirigido al primer contacto del usuario.
     * Todavía no incluye ubicación: falta obtener el GPS (permiso ACCESS_FINE_LOCATION)
     * y pasar una {@code Ubicacion} a construirMensajePanico().
     */
    public static void dispararAlertaSos(AppCompatActivity context, BaseDatos database, long usuarioId) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            List<ContactoEmergenciaEntity> lista = database.contactoDao().obtenerPorUsuario(usuarioId);
            context.runOnUiThread(() -> {
                if (lista.isEmpty()) {
                    Toast.makeText(context, "No hay contactos de emergencia registrados.", Toast.LENGTH_LONG).show();
                    return;
                }

                ContactoEmergencia principal = ContactoMapper.aDominio(lista.get(0));
                String mensaje = principal.construirMensajePanico(null);

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + principal.getTelefonoNumerico()
                            + "&text=" + Uri.encode(mensaje);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "WhatsApp no está instalado en el dispositivo.", Toast.LENGTH_SHORT).show();
                }
            });
            exec.shutdown();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
