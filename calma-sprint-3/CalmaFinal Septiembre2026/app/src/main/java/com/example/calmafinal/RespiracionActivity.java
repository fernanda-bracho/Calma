package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.animation.Animator;

public class RespiracionActivity extends AppCompatActivity {

    private View circleView;
    private TextView tvInstruccion;
    private Button btnIniciarCiclo;
    private AnimatorSet animatorSet;

    // 🎯 VARIABLES PARA EL CONTADOR DE CICLOS
    private TextView tvContadorCiclos;
    private int currentCycle = 1;
    private static final int MAX_CYCLES = 4;

    // 💨 Duraciones específicas para el ciclo 4-7-8
    private static final long DURATION_INHALAR = 4000; // 4 segundos
    private static final long DURATION_AGUANTAR = 7000; // 7 segundos (PAUSA)
    private static final long DURATION_EXHALAR = 8000; // 8 segundos

    // Escalas mínima y máxima para la animación
    private static final float SCALE_MIN = 1.0f; // Tamaño normal (inicial)
    private static final float SCALE_MAX = 2.0f; // Doble tamaño

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respiracion);

        circleView = findViewById(R.id.circleView);
        tvInstruccion = findViewById(R.id.tvInstruccion);
        tvContadorCiclos = findViewById(R.id.tvContadorCiclos);
        btnIniciarCiclo = findViewById(R.id.btnIniciarCiclo); // ✨ Inicialización del botón

        // Establecer el estado inicial
        resetUI();

        // ✨ Configurar el listener del botón
        btnIniciarCiclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reiniciar todo y empezar el ciclo
                iniciarCicloDesdeBoton();
            }
        });


    }

    /**
     * Prepara la interfaz para un nuevo inicio de ciclo y lo arranca.
     */
    private void iniciarCicloDesdeBoton() {
        // Desactivar el botón para que el usuario no lo presione de nuevo a mitad del ciclo
        btnIniciarCiclo.setEnabled(false);
        btnIniciarCiclo.setText("EN PROGRESO..."); // Oculta el botón o cambia el texto

        // Reiniciar las variables del ciclo
        currentCycle = 1;
        resetUI(); // Asegura que el círculo esté en su estado inicial (tamaño 1.0)

        // Iniciar la animación
        startBreathingCycle();
    }

    /**
     * Restablece el TextView del contador y el círculo a su estado inicial.
     */
    private void resetUI() {
        tvContadorCiclos.setText("Ciclo: " + currentCycle + "/" + MAX_CYCLES);
        tvInstruccion.setText("¡Presiona INICIAR! ✅");

        // Asegurar que el círculo esté en la escala mínima (1.0) al inicio
        circleView.setScaleX(SCALE_MIN);
        circleView.setScaleY(SCALE_MIN);
    }


    /**
     * Inicia el ciclo de respiración 4-7-8.
     */
    private void startBreathingCycle() {

        // 1. 🌬️ INHALACIÓN (Escalar de 1.0 a 2.0 en 4 segundos)
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(circleView, "scaleX", SCALE_MIN, SCALE_MAX);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(circleView, "scaleY", SCALE_MIN, SCALE_MAX);
        scaleUpX.setDuration(DURATION_INHALAR);
        scaleUpY.setDuration(DURATION_INHALAR);

        scaleUpX.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                tvInstruccion.setText("INHALA (4s) 🌬️");
            }
        });

        // 2. 🧘 PAUSA (Mantener la escala en 2.0 durante 7 segundos)
        ObjectAnimator holdScaleX = ObjectAnimator.ofFloat(circleView, "scaleX", SCALE_MAX, SCALE_MAX);
        holdScaleX.setDuration(DURATION_AGUANTAR);

        holdScaleX.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                tvInstruccion.setText("AGUANTA (7s) 🧘");
            }
        });

        // 3. 😌 EXHALACIÓN (Escalar de 2.0 a 1.0 en 8 segundos)
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(circleView, "scaleX", SCALE_MAX, SCALE_MIN);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(circleView, "scaleY", SCALE_MAX, SCALE_MIN);
        scaleDownX.setDuration(DURATION_EXHALAR);
        scaleDownY.setDuration(DURATION_EXHALAR);

        scaleDownX.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                tvInstruccion.setText("EXHALA (8s) 😌");
            }
            // ✨ En el final de la exhalación es donde se comprueba el ciclo.
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                currentCycle++;

                if (currentCycle <= MAX_CYCLES) {
                    // Si no hemos terminado los 4 ciclos, actualiza el texto y repite.
                    tvContadorCiclos.setText("Ciclo: " + currentCycle + "/" + MAX_CYCLES);
                    // Para repetir, simplemente reiniciamos la animación.
                    startBreathingCycle();
                } else {
                    // Si ya terminamos, mostramos el mensaje final y reactivamos el botón.
                    tvInstruccion.setText("¡Ciclo Completo! ✅");
                    tvContadorCiclos.setText("Ciclo: " + MAX_CYCLES + "/" + MAX_CYCLES);
                    btnIniciarCiclo.setEnabled(true);
                    btnIniciarCiclo.setText("INICIAR CICLO");
                }
            }
        });

        // Crear el conjunto de animaciones: INHALAR -> PAUSA -> EXHALAR
        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                scaleUpX,   // INHALAR (4s)
                holdScaleX, // AGUANTAR (7s)
                scaleDownX  // EXHALAR (8s)
        );

        // Iniciar el ciclo de respiración
        animatorSet.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Detener la animación cuando la Activity no está en primer plano
        if (animatorSet != null) {
            animatorSet.cancel();
            // Si la animación se cancela, reactivamos el botón
            btnIniciarCiclo.setEnabled(true);
            btnIniciarCiclo.setText("INICIAR CICLO");
        }
    }

    // Clase auxiliar para simplificar los Listeners de animación
    private static class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {}
        @Override
        public void onAnimationEnd(Animator animation) {}
        @Override
        public void onAnimationCancel(Animator animation) {}
        @Override
        public void onAnimationRepeat(Animator animation) {}
    }
}