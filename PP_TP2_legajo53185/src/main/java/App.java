import certificacion.Certificable;
import excepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import modelo.Actividad;
import modelo.Charla;
import modelo.Curso;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Sala;
import modelo.Taller;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        Estudiante est1 = new Estudiante("1001", "Juan Perez");
        Estudiante est2 = new Estudiante("1002", "Maria Lopez");
        Estudiante est3 = new Estudiante("1003", "Carlos Gomez");
        Estudiante est4 = new Estudiante("1004", "Ana Torres");

        EventoUniversitario evento = new EventoUniversitario("EV001", "Semana de la Tecnologia", 20000.0, false);
        Sala sala = new Sala(1, "Auditorio Principal");
        evento.asignarSala(sala);

        evento.crearActividad(1, "Introduccion a la IA", 2, "Charla");
        evento.crearActividad(2, "Taller de Java", 3, "Taller");
        evento.crearActividad(3, "Curso de Bases de Datos", 2, "Curso");

        List<Actividad> actividades = evento.getActividades();
        Actividad charla = actividades.get(0);
        Actividad taller = actividades.get(1);
        Actividad curso = actividades.get(2);

        System.out.println("=== Ejercicio 1: excepciones y persistencia ===");
        try {
            charla.inscribir(est1);
            charla.inscribir(est2);
            System.out.println("Inscripciones exitosas en la charla");
            charla.inscribir(est3);
        } catch (CupoExcedidoException e) {
            System.out.println("No se pudo inscribir: " + e.getMessage());
        }

        try {
            boolean persistido = evento.persistirEvento();
            if (persistido) {
                System.out.println("Evento persistido correctamente");
            }

            EventoUniversitario recuperado = evento.recuperarEvento(evento.getId());
            System.out.println("Evento recuperado desde archivo: " + recuperado.getTitulo());
        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo del evento: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("El archivo del evento esta vacio o corrupto: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo reconstruir la clase del evento: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de entrada/salida al persistir o recuperar el evento: " + e.getMessage());
        } finally {
            System.out.println("Finalizo el intento de inscripcion, persistencia y lectura del evento");
        }

        System.out.println();
        System.out.println("=== Ejercicio 2: certificados ===");
        try {
            taller.inscribir(est1);
            taller.inscribir(est2);
            curso.inscribir(est3);
        } catch (CupoExcedidoException e) {
            System.out.println("No se pudo inscribir: " + e.getMessage());
        }

        List<String> certificados = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (actividad instanceof Certificable) {
                Certificable certificable = (Certificable) actividad;
                for (Inscripcion inscripcion : actividad.getInscripciones()) {
                    certificados.add(certificable.generarCertificado(inscripcion.getEstudiante()));
                }
            }
        }
        System.out.println("Certificados emitidos:");
        for (String certificado : certificados) {
            System.out.println(certificado);
        }

        evento.mostrarDatos();

        System.out.println();
        System.out.println("=== Ejercicio 3: generics y wildcards ===");
        List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
        List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
        List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

        System.out.println("Cantidad de charlas: " + charlas.size());
        System.out.println("Cantidad de talleres: " + talleres.size());
        System.out.println("Cantidad de cursos: " + cursos.size());

        System.out.println("Costo de materiales en charlas: " + evento.calcularCostoMateriales(charlas));
        System.out.println("Costo de materiales en talleres: " + evento.calcularCostoMateriales(talleres));
        System.out.println("Costo de materiales en cursos: " + evento.calcularCostoMateriales(cursos));

        System.out.println();
        System.out.println("=== Ejercicio 4: clases anidadas e hilos ===");
        for (Inscripcion inscripcion : charla.getInscripciones()) {
            inscripcion.confirmar();
        }
        for (Inscripcion inscripcion : taller.getInscripciones()) {
            inscripcion.confirmar();
        }

        for (Actividad actividad : actividades) {
            for (Inscripcion inscripcion : actividad.getInscripciones()) {
                if (inscripcion.isConfirmada()) {
                    inscripcion.generarTicket();
                }
            }
        }

        EnvioTicketsThread hiloEnvio = new EnvioTicketsThread(evento);
        hiloEnvio.start();

        for (int i = 0; i < 3; i++) {
            System.out.println("[Hilo principal] Datos del evento: " + evento.getTitulo());
            for (Actividad actividad : actividades) {
                System.out.println("[Hilo principal] Actividad " + actividad.getTitulo()
                        + " - inscriptos: " + actividad.getInscripciones().size());
            }
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            hiloEnvio.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();
        System.out.println("Cantidad total de eventos creados: " + EventoUniversitario.getCantidadEventos());
        System.out.println("Estudiante sin actividad asignada en este flujo: " + est4);
    }
}
