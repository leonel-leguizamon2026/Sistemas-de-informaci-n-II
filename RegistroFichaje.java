import java.time.LocalDateTime;

public class RegistroFichaje {
    private int legajo;
    private String nombreEmpleado;
    private String rol;
    private String tipo; // "Entrada" o "Salida"
    private LocalDateTime fechaHora;

    public RegistroFichaje(int legajo, String nombreEmpleado, String rol, String tipo, LocalDateTime fechaHora) {
        this.legajo = legajo;
        this.nombreEmpleado = nombreEmpleado;
        this.rol = rol;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
    }

    public int getLegajo() { return legajo; }
    public String getNombreEmpleado() { return nombreEmpleado; }
    public String getRol() { return rol; }
    public String getTipo() { return tipo; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    @Override
    public String toString() {
        return "[" + fechaHora + "] " + tipo + " - Legajo " + legajo
                + " (" + nombreEmpleado + " | " + rol + ")";
    }
}