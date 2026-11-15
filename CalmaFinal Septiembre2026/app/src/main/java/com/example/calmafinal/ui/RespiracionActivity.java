package com.example.calmafinal.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calmafinal.R;
import com.example.calmafinal.domain.service.ControladorRespiracion;
import com.example.calmafinal.domain.service.ControladorRespiracion.Fase;

/**
 * Solo presentación: los tiempos y los ciclos los lleva {@link ControladorRespiracion}; esta
 * Activity traduce cada fase a texto y a la animación del círculo.
 */
public class RespiracionActivity extends AppCompatActivity {

    private static final float ESCALA_MIN = 1.0f; // tamaño normal
    private static final float ESCALA_MAX = 2.0f; // doble tamaño

    private View circleView;
    private TextView tvInstruccion;
    private TextView tvContadorCiclos;
    private Button btnIniciarCiclo;

    private ControladorRespiracion controlador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respiracion);

        circleView = findViewById(R.id.circleView);
        tvInstruccion = findViewById(R.id.tvInstruccion);
        tvContadorCiclos = findViewById(R.id.tvContadorCiclos);
        btnIniciarCiclo = findViewById(R.id.btnIniciarCiclo);

        controlador = new ControladorRespiracion(new ControladorRespiracion.Listener() {
            @Override
            public void onFaseIniciada(Fase fase, int ciclo, int totalCiclos, long duracionMs) {
                // El controlador avisa desde un hilo de fondo.
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        mostrarFase(fase, ciclo, totalCiclos, duracionMs);
                    }
                });
            }

            @Override
            public void onSesionCompletada() {
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        tvInstruccion.setText("¡Ciclo Completo! ✅");
                        btnIniciarCiclo.setEnabled(true);
                        btnIniciarCiclo.setText("INICIAR CICLO");
                    }
                });
            }
        });

        resetUI();

        btnIniciarCiclo.setOnClickListener(v -> {
            btnIniciarCiclo.setEnabled(false);
            btnIniciarCiclo.setText("EN PROGRESO...");
            resetUI();
            controlador.iniciar();
        });
    }

    private void mostrarFase(Fase fase, int ciclo, int totalCiclos, long duracionMs) {
        tvContadorCiclos.setText("Ciclo: " + ciclo + "/" + totalCiclos);

        switch (fase) {
            case INHALAR:
                tvInstruccion.setText("INHALA (4s) 🌬️");
                circleView.animate().scaleX(ESCALA_MAX).scaleY(ESCALA_MAX).setDuration(duracionMs).start();
                break;
            case AGUANTAR:
                tvInstruccion.setText("AGUANTA (7s) 🧘");
                // El círculo se mantiene grande: no hay animación.
                break;
            case EXHALAR:
                tvInstruccion.setText("EXHALA (8s) 😌");
                circleView.animate().scaleX(ESCALA_MIN).scaleY(ESCALA_MIN).setDuration(duracionMs).start();
                break;
        }
    }

    private void resetUI() {
        tvContadorCiclos.setText("Ciclo: 1/" + controlador.getTotalCiclos());
        tvInstruccion.setText("¡Presiona INICIAR! ✅");
        circleView.animate().cancel();
        circleView.setScaleX(ESCALA_MIN);
        circleView.setScaleY(ESCALA_MIN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener la sesión cuando la Activity deja de estar en primer plano.
        controlador.cancelar();
        circleView.animate().cancel();
        btnIniciarCiclo.setEnabled(true);
        btnIniciarCiclo.setText("INICIAR CICLO");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controlador.cancelar();
    }
}
