package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Inscripcion implements Serializable {

    private Estudiante estudiante;
    private LocalDate fecha;
    private String estado;
    private TicketDeAcceso ticket;

    public Inscripcion(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.fecha = LocalDate.now();
        this.estado = "Pendiente";
    }

    public void confirmar() {
        this.estado = "Confirmada";
    }

    public boolean isConfirmada() {
        return "Confirmada".equals(estado);
    }

    public TicketDeAcceso generarTicket() {
        if (!isConfirmada()) {
            throw new IllegalStateException("No se puede generar un ticket para una inscripcion no confirmada");
        }
        this.ticket = new TicketDeAcceso();
        return this.ticket;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public TicketDeAcceso getTicket() {
        return ticket;
    }

    public class TicketDeAcceso implements Serializable {

        private String idTicket;
        private LocalDate fechaEmision;

        public TicketDeAcceso() {
            this.idTicket = "TCK-" + estudiante.getLegajo() + "-" + System.nanoTime();
            this.fechaEmision = LocalDate.now();
        }

        public void enviarTicket() {
            System.out.println("[Hilo envio] Enviando ticket " + idTicket + " a " + estudiante.getNombre());
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("[Hilo envio] Ticket " + idTicket + " enviado correctamente");
        }

        public String getIdTicket() {
            return idTicket;
        }

        public LocalDate getFechaEmision() {
            return fechaEmision;
        }
    }
}
