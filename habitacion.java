import java.time.LocalDate;
import java.util.ArrayList;

public class Habitacion {
    private int numero;
    private int capacidad; // 1: Single, 2: Doble, 4: Suite
    private String estado; // "Disponible", "Ocupada", "Pendiente Limpieza", "En Mantenimiento"

    public Habitacion(int numero, int capacidad, String estado) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    // Comportamiento de la tarjeta CRC: Informar disponibilidad
    public boolean informarDisponibilidad() {
        return this.estado.equalsIgnoreCase("Disponible");
    }

    // Sobrecarga para verificar disponibilidad en un rango de fechas (CU-04.4)
   public boolean informarDisponibilidad(LocalDate fechaEntrada, LocalDate fechaSalida, ArrayList<Reserva> reservas) {
    if (this.estado == null || this.estado.equalsIgnoreCase("Pendiente Limpieza")|| this.estado.equalsIgnoreCase("En Mantenimiento")) {
        return false;}
    if (reservas == null) return true;

    for (Reserva r : reservas) {
        if (r.getEstado() == null || r.getEstado().equalsIgnoreCase("Cancelada") || r.getEstado().equalsIgnoreCase("Finalizada")) {
            continue;
        }
        if (r.getHabitaciones().contains(this)) {
            boolean seSuperpone = fechaEntrada.isBefore(r.getFechaSalida()) && fechaSalida.isAfter(r.getFechaEntrada());
            if (seSuperpone) {
                return false;
            }
        }
    }return true;}

    public String getTipoDescripcion() {
        if (capacidad == 1) return "Single";
        if (capacidad == 2) return "Doble";
        return "Suite";
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Hab. " + numero + " (" + getTipoDescripcion() + " - Cap: " + capacidad + ") [" + estado + "]";
    }
}