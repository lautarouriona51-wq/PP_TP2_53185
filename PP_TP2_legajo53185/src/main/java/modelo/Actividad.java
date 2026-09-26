package modelo;

import excepciones.CupoExcedidoException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Actividad implements Serializable {

    private int id;
    private String titulo;
    private int cupoMaximo;
    public static final int CUPO_MINIMO = 1;
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Actividad(int id, String titulo, int cupoMaximo) {
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
    }

    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if (inscripciones.size() >= cupoMaximo) {
            throw new CupoExcedidoException("Cupo excedido para la actividad " + titulo);
        }
        Inscripcion inscripcion = new Inscripcion(estudiante);
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    public void mostrarInscripciones() {
        System.out.println("Inscripciones de " + titulo + ":");
        for (Inscripcion inscripcion : inscripciones) {
            System.out.println(" - " + inscripcion.getEstudiante() + " | estado: " + inscripcion.getEstado());
        }
    }

    public final void mostrarIdentificacion() {
        System.out.println("Actividad #" + id + " - " + titulo + " (" + getTipo() + ") - cupo maximo: " + cupoMaximo
                + " - inscriptos: " + inscripciones.size());
    }

    public abstract double calcularCostoMateriales();

    public abstract String getTipo();

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }
}
