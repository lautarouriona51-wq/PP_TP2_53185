package hilos;

import modelo.Actividad;
import modelo.EventoUniversitario;
import modelo.Inscripcion;

public class EnvioTicketsThread extends Thread {

    private EventoUniversitario evento;

    public EnvioTicketsThread(EventoUniversitario evento) {
        this.evento = evento;
    }

    @Override
    public void run() {
        System.out.println("[Hilo envio] Iniciando envio de tickets para el evento " + evento.getTitulo());
        for (Actividad actividad : evento.getActividades()) {
            for (Inscripcion inscripcion : actividad.getInscripciones()) {
                if (inscripcion.getTicket() != null) {
                    inscripcion.getTicket().enviarTicket();
                }
            }
        }
        System.out.println("[Hilo envio] Finalizo el envio de todos los tickets");
    }
}
