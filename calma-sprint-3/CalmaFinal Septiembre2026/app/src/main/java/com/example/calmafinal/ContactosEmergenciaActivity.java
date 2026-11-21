package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calmafinal.data.BaseDatos;
import com.example.calmafinal.data.ContactoDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactosEmergenciaActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etRelacion;
    private Button btnGuardar;
    private BaseDatos db;
    private ContactoDao contactoDao;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_emergencia);

        // Vincular elementos de la interfaz visual
        etNombre = findViewById(R.id.etNombreContacto);
        etTelefono = findViewById(R.id.etTelefonoContacto);
        etRelacion = findViewById(R.id.etRelacionContacto);
        btnGuardar = findViewById(R.id.btnGuardarContacto);

        // Inicializar la base de datos Room y el hilo de ejecución en segundo plano
        db = BaseDatos.getDatabase(this);
        contactoDao = db.contactoDao();
        executorService = Executors.newSingleThreadExecutor();

        // Botón para guardar el contacto en la base de datos
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String relacion = etRelacion.getText().toString().trim();

            if (nombre.isEmpty() || telefono.isEmpty() || relacion.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            ContactoEmergencia nuevoContacto = new ContactoEmergencia(nombre, telefono, relacion);

            executorService.execute(() -> {
                contactoDao.insertar(nuevoContacto); // ✨ Corregido a 'insertar'
                runOnUiThread(() -> {
                    Toast.makeText(this, "¡Contacto guardado con éxito!", Toast.LENGTH_SHORT).show();
                    etNombre.setText("");
                    etTelefono.setText("");
                    etRelacion.setText("");
                });
            });
        });
    }

    // Método estático para disparar el WhatsApp de emergencia con los datos guardados
    public static void dispararAlertaSos(AppCompatActivity context, BaseDatos database) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            List<ContactoEmergencia> listaContactos = database.contactoDao().obtenerTodos();
            context.runOnUiThread(() -> {
                if (listaContactos.isEmpty()) {
                    Toast.makeText(context, "No hay contactos de emergencia registrados.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Tomamos el primer contacto registrado (puedes adaptarlo si deseas enviarlo a varios)
                ContactoEmergencia principal = listaContactos.get(0);
                String mensaje = "🚨 ¡EMERGENCIA CALMA+! Necesito apoyo inmediato. Esta es una alerta de crisis activada desde la aplicación.";

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + principal.getTelefono().replaceAll("[^0-9]", "") + "&text=" + Uri.encode(mensaje);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "WhatsApp no está instalado en el dispositivo.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}