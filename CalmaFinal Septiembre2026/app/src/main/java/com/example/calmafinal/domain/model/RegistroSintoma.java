package com.example.calmafinal.domain.model;

/**
 * Autoevaluación diaria del usuario: nivel de estrés, estado de ánimo y notas de malestar
 * físico. Los registros se conservan en orden cronológico.
 */
public class RegistroSintoma {

    public static final int ESTRES_MIN = 1;
    public static final int ESTRES_MAX = 10;

    private final long id;
    private final long fechaMs;
    private final int nivelEstres;
    private final String estadoAnimo;
    private final String notasMalestarFisico;

    public RegistroSintoma(long id, long fechaMs, int nivelEstres, String estadoAnimo, String notasMalestarFisico) {
        if (nivelEstres < ESTRES_MIN || nivelEstres > ESTRES_MAX) {
            throw new IllegalArgumentException(
                    "El nivel de estrés debe estar entre " + ESTRES_MIN + " y " + ESTRES_MAX + ".");
        }
        if (estadoAnimo == null || estadoAnimo.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de ánimo es obligatorio.");
        }
        this.id = id;
        this.fechaMs = fechaMs;
        this.nivelEstres = nivelEstres;
        this.estadoAnimo = estadoAnimo.trim();
        this.notasMalestarFisico = notasMalestarFisico == null ? "" : notasMalestarFisico.trim();
    }

    public long getId() { return id; }

    public long getFechaMs() { return fechaMs; }

    public int getNivelEstres() { return nivelEstres; }

    public String getEstadoAnimo() { return estadoAnimo; }

    public String getNotasMalestarFisico() { return notasMalestarFisico; }
}
