# PP_TP2 - Sistema de Eventos Universitarios

Trabajo Practico 2 - Paradigmas de Programacion - UTN FRM
Unidad 2: Organizacion, reutilizacion y recursos avanzados en POO (Java)

## Requisitos

- JDK 17 o superior
- Maven 3.8+


## Estructura del proyecto

```
src/main/java/
  App.java                          Clase principal, ejecuta los 4 ejercicios en secuencia
  modelo/
    EventoUniversitario.java        Entidad central del sistema, agrega Sala y compone Actividades
    Sala.java
    Actividad.java                  Clase abstracta, base de Charla, Taller y Curso
    Charla.java
    Taller.java                     Implementa Certificable
    Curso.java                      Implementa Certificable
    Estudiante.java
    Inscripcion.java                Contiene la clase anidada miembro TicketDeAcceso
  excepciones/
    CupoExcedidoException.java      Excepcion chequeada para cupos excedidos
  certificacion/
    Certificable.java               Interfaz para actividades que emiten certificado
  hilos/
    EnvioTicketsThread.java         Hilo independiente para el envio concurrente de tickets
```

## Ejercicio 1 - Excepciones y persistencia

- `Actividad.inscribir` lanza `CupoExcedidoException` cuando se supera el cupo maximo de la actividad.
- `EventoUniversitario.persistirEvento()` serializa el evento a un archivo `evento_<id>.dat`.
- `EventoUniversitario.recuperarEvento(id)` deserializa el evento desde ese archivo.
- En `App` se implementa un flujo try-catch-finally que intenta inscribir, persistir y recuperar un evento,
  manejando de forma granular `FileNotFoundException`, `EOFException`, `ClassNotFoundException` e `IOException`,
  ademas de `CupoExcedidoException` para la inscripcion. Se muestra un caso exitoso (persistencia y lectura)
  y un caso fallido controlado por excepcion (cupo excedido en la charla).

## Ejercicio 2 - Interfaces y certificados

- `Taller` y `Curso` implementan `Certificable` y generan certificados de asistencia.
- `Charla` no implementa `Certificable`, por lo que no emite certificados.
- En `App` se recorren las actividades certificables y se emiten certificados para los estudiantes inscriptos.

## Ejercicio 3 - Generics y wildcards

- `EventoUniversitario.filtrarActividadesPorTipo(Class<T> tipo)` devuelve listas correctamente tipadas
  (`List<Charla>`, `List<Taller>`, `List<Curso>`) usando un metodo parametrizado acotado.
- `EventoUniversitario.calcularCostoMateriales(List<? extends Actividad> actividades)` usa un wildcard
  acotado para calcular el costo de materiales de cualquier lista de actividades o subtipos.

## Ejercicio 4 - Clases anidadas e hilos

- `Inscripcion.TicketDeAcceso` es una clase anidada miembro (no estatica) de `Inscripcion`, ya que un ticket
  solo tiene sentido en el contexto de una inscripcion confirmada concreta.
- `hilos.EnvioTicketsThread` es una clase independiente que extiende `Thread` y envia, en un hilo separado,
  todos los tickets generados para las inscripciones confirmadas del evento.
- En `App`, mientras el hilo de envio se ejecuta, el hilo principal continua mostrando por consola los datos
  del evento, sus actividades y los estudiantes inscriptos, evidenciando la existencia de dos flujos de
  ejecucion concurrentes.
