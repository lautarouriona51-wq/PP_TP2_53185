package modelo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventoUniversitario implements Serializable {

    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private static int cantidadEventos = 0;
    private Sala sala;
    private List<Actividad> actividades = new ArrayList<>();

    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = id;
        this.titulo = titulo;
        this.costoBase = costoBase;
        this.gratuito = gratuito;
        cantidadEventos++;
    }

    public EventoUniversitario(EventoUniversitario otro) {
        this.id = otro.id;
        this.titulo = otro.titulo;
        this.costoBase = otro.costoBase;
        this.gratuito = otro.gratuito;
        this.sala = otro.sala;
        this.actividades = new ArrayList<>(otro.actividades);
        cantidadEventos++;
    }

    public double calcularCostoEstimado() {
        if (gratuito) {
            return 0.0;
        }
        double total = costoBase;
        for (Actividad actividad : actividades) {
            total += actividad.calcularCostoMateriales();
        }
        return total;
    }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    public void crearActividad(int idActividad, String titulo, int cupoMaximo, String tipo) {
        Actividad actividad;
        switch (tipo) {
            case "Charla":
                actividad = new Charla(idActividad, titulo, cupoMaximo, "A confirmar");
                break;
            case "Taller":
                actividad = new Taller(idActividad, titulo, cupoMaximo, false);
                break;
            case "Curso":
                actividad = new Curso(idActividad, titulo, cupoMaximo, 1);
                break;
            default:
                throw new IllegalArgumentException("Tipo de actividad no soportado: " + tipo);
        }
        actividades.add(actividad);
    }

    public void mostrarDatos() {
        System.out.println("Evento: " + titulo + " (id " + id + ")");
        System.out.println("Costo base: " + costoBase + " | Gratuito: " + gratuito);
        if (sala != null) {
            System.out.println("Sala: " + sala.getNombre());
        }
        System.out.println("Costo estimado: " + calcularCostoEstimado());
        System.out.println("Actividades:");
        for (Actividad actividad : actividades) {
            actividad.mostrarIdentificacion();
        }
    }

    public boolean persistirEvento() throws IOException {
        String archivo = "evento_" + id + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(this);
            return true;
        }
    }

    public EventoUniversitario recuperarEvento(String idBuscado) throws IOException, ClassNotFoundException {
        String archivo = "evento_" + idBuscado + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (EventoUniversitario) ois.readObject();
        }
    }

    public static int getCantidadEventos() {
        return cantidadEventos;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Sala getSala() {
        return sala;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (tipo.isInstance(actividad)) {
                resultado.add(tipo.cast(actividad));
            }
        }
        return resultado;
    }

    public double calcularCostoMateriales(List<? extends Actividad> actividadesLista) {
        double total = 0.0;
        for (Actividad actividad : actividadesLista) {
            total += actividad.calcularCostoMateriales();
        }
        return total;
    }
}
