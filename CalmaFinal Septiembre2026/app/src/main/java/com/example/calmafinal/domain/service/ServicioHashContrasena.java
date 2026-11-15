package com.example.calmafinal.domain.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Hashing de contraseñas con PBKDF2 y sal aleatoria por usuario. Java puro, sin Android.
 *
 * Formato almacenado: {@code algoritmo$iteraciones$salHex$hashHex}
 *
 * El algoritmo queda guardado dentro del propio hash, así se puede verificar una contraseña
 * aunque más adelante cambie el algoritmo preferido. PBKDF2WithHmacSHA256 solo existe desde
 * Android 8 (API 26); como minSdk es 24, en Android 7.x se usa PBKDF2WithHmacSHA1.
 */
public class ServicioHashContrasena {

    private static final String[] ALGORITMOS_PREFERIDOS = {
            "PBKDF2WithHmacSHA256",
            "PBKDF2WithHmacSHA1"
    };
    private static final int ITERACIONES = 120_000;
    private static final int BYTES_SAL = 16;
    private static final int BITS_HASH = 256;

    private final SecureRandom random = new SecureRandom();

    /** Genera el hash que debe guardarse en lugar de la contraseña. */
    public String hashear(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        byte[] sal = new byte[BYTES_SAL];
        random.nextBytes(sal);

        for (String algoritmo : ALGORITMOS_PREFERIDOS) {
            try {
                byte[] hash = derivar(algoritmo, contrasena.toCharArray(), sal, ITERACIONES);
                return algoritmo + "$" + ITERACIONES + "$" + aHex(sal) + "$" + aHex(hash);
            } catch (NoSuchAlgorithmException e) {
                // Este dispositivo no ofrece el algoritmo: se prueba el siguiente.
            }
        }
        throw new IllegalStateException("No hay un algoritmo PBKDF2 disponible en este dispositivo.");
    }

    /** Comprueba una contraseña contra un hash producido por {@link #hashear(String)}. */
    public boolean verificar(String contrasena, String hashAlmacenado) {
        if (contrasena == null || hashAlmacenado == null) {
            return false;
        }
        String[] partes = hashAlmacenado.split("\\$");
        if (partes.length != 4) {
            return false;
        }
        try {
            int iteraciones = Integer.parseInt(partes[1]);
            byte[] sal = deHex(partes[2]);
            byte[] esperado = deHex(partes[3]);
            byte[] calculado = derivar(partes[0], contrasena.toCharArray(), sal, iteraciones, esperado.length * 8);
            return MessageDigest.isEqual(esperado, calculado); // comparación en tiempo constante
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(String algoritmo, char[] contrasena, byte[] sal, int iteraciones)
            throws NoSuchAlgorithmException {
        return derivar(algoritmo, contrasena, sal, iteraciones, BITS_HASH);
    }

    private static byte[] derivar(String algoritmo, char[] contrasena, byte[] sal, int iteraciones, int bits)
            throws NoSuchAlgorithmException {
        PBEKeySpec spec = new PBEKeySpec(contrasena, sal, iteraciones, bits);
        try {
            return SecretKeyFactory.getInstance(algoritmo).generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Parámetros de hashing inválidos.", e);
        } finally {
            spec.clearPassword();
        }
    }

    private static String aHex(byte[] datos) {
        StringBuilder sb = new StringBuilder(datos.length * 2);
        for (byte b : datos) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] deHex(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Hexadecimal inválido.");
        }
        byte[] datos = new byte[hex.length() / 2];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return datos;
    }
}
