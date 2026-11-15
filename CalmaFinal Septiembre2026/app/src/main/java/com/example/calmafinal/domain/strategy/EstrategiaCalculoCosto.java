package com.example.calmafinal.domain.strategy;

/**
 * Contrato polimórfico (patrón Strategy) para calcular el costo final de una cita (RF04).
 * La clase Cita delega en esta interfaz y evita condicionales del tipo "instanceof Empleado".
 */
public interface EstrategiaCalculoCosto {

    /**
     * @param tarifaBase tarifa estándar de la cita, sin subsidios
     * @return monto final que debe pagar el usuario
     */
    double calcularCostoFinal(double tarifaBase);
}
