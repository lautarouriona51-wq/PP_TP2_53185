package modelo;

import certificacion.Certificable;

public class Curso extends Actividad implements Certificable {

    private int nivel;

    public Curso(int id, String titulo, int cupoMaximo, int nivel) {
        super(id, titulo, cupoMaximo);
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public double calcularCostoMateriales() {
        return 6000.0 + (nivel * 1000.0);
    }

    @Override
    public String getTipo() {
        return "Curso";
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "Certificado emitido por " + ENTIDAD_EMISORA + " a " + estudiante.getNombre()
                + " por participar en el curso " + getTitulo() + " (nivel " + nivel + ")";
    }
}
