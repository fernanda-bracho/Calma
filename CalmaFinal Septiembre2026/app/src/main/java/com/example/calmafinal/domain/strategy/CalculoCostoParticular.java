package com.example.calmafinal.domain.strategy;

/**
 * Usuario particular (B2C): paga la tarifa estándar completa, sin subsidio.
 */
public class CalculoCostoParticular implements EstrategiaCalculoCosto {

    @Override
    public double calcularCostoFinal(double tarifaBase) {
        return tarifaBase;
    }
}
