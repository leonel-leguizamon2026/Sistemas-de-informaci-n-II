import java.util.ArrayList;
import java.util.Scanner;

public class Mantenimiento {
    private String codMant;
    private String limpieza; // Ej: "Limpia", "Pendiente Limpieza"
    private String estado; // Ej: "Pendiente", "En Proceso", "Resuelto"
    private String descripcion;

    // Colaboradores según CRC y Modelo de Dominio
    private Empleado empleado;
    private Habitacion habitacion; // Puede ser null si el desperfecto es de un sector/instalación general
    private ArrayList<Gasto> gastos;

    public Mantenimiento(String codMant, String limpieza, String estado, String descripcion,
                         Empleado empleado, Habitacion habitacion) {
        this.codMant = codMant;
        this.limpieza = limpieza;
        this.estado = estado;
        this.descripcion = descripcion;
        this.empleado = empleado;
        this.habitacion = habitacion;
        this.gastos = new ArrayList<>();
    }

    // Registrar reporte de mantenimiento / desperfecto (CU-08 / CU-06.1)
    public static Mantenimiento registrarReporte(Scanner sc, ArrayList<Mantenimiento> mantenimientos,
                                                 Habitacion[] habitaciones, Empleado empleado) {
        System.out.println("\n--- REPORTE DE MANTENIMIENTO ---");
        Habitacion habSeleccionada = null;

        System.out.print("¿El desperfecto corresponde a una habitación? (S/N): ");
        String esHab = sc.nextLine().trim();
        if (esHab.equalsIgnoreCase("S")) {
            System.out.print("Ingrese número de habitación: ");
            try {
                int num = Integer.parseInt(sc.nextLine().trim());
                for (Habitacion h : habitaciones) {
                    if (h.getNumero() == num) {
                        habSeleccionada = h;
                        break;
                    }
                }
                if (habSeleccionada == null) {
                    System.out.println("Habitación no encontrada.");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Número de habitación inválido.");
                return null;
            }
        }

        String ubicacionSector = "";
        if (habSeleccionada == null) {
            do {
                System.out.print("Sector o instalación afectada: ");
                ubicacionSector = sc.nextLine().trim();
                if (ubicacionSector.isEmpty()) {
                    System.out.println("Debe indicar el sector o instalación.");
                }
            } while (ubicacionSector.isEmpty());
        }

        String detalle;
        do {
            System.out.print("Descripción del problema o desperfecto: ");
            detalle = sc.nextLine().trim();
            if (detalle.isEmpty()) {
                System.out.println("La descripción no puede estar vacía.");
            }
        } while (detalle.isEmpty());

        String descripcionCompleta = (habSeleccionada != null)
                ? "Hab. " + habSeleccionada.getNumero() + " - " + detalle
                : ubicacionSector + " - " + detalle;

        String cod = "MANT-" + (100 + mantenimientos.size() + 1);
        String estadoLimpieza = (habSeleccionada != null) ? habSeleccionada.getEstado() : "N/A";
        Mantenimiento reporte = new Mantenimiento(cod, estadoLimpieza, "Pendiente", descripcionCompleta,
                                                empleado, habSeleccionada);
        mantenimientos.add(reporte);

        if (habSeleccionada != null) {
            habSeleccionada.setEstado("En Mantenimiento");
        }

        System.out.println("Reporte registrado exitosamente: " + reporte);
        return reporte;
    }

    // Comportamiento de la tarjeta CRC: Actualizar estado (y/o limpieza en CU-03.2)
    public void actualizarEstado(Scanner sc) {
        System.out.println("\n--- ACTUALIZAR ESTADO DE MANTENIMIENTO [" + this.codMant + "] ---");
        System.out.println("Actual: " + this);

        System.out.print("Nuevo estado de mantenimiento (Pendiente / En Proceso / Resuelto, Enter para mantener): ");
        String nuevoEstado = sc.nextLine().trim();
        if (!nuevoEstado.isEmpty()) {
            this.estado = nuevoEstado;
        }

        System.out.print("Nuevo estado de limpieza (Limpia / Pendiente Limpieza, Enter para mantener): ");
        String nuevaLimpieza = sc.nextLine().trim();
        if (!nuevaLimpieza.isEmpty()) {
            this.limpieza = nuevaLimpieza;
        }

        if (this.habitacion != null) {
            if (this.estado.equalsIgnoreCase("Resuelto")) {
                if (this.limpieza.equalsIgnoreCase("Limpia")) {
                    this.habitacion.setEstado("Disponible");
                } else {
                    this.habitacion.setEstado("Pendiente Limpieza");
                }
            } else if (this.estado.equalsIgnoreCase("En Proceso") || this.estado.equalsIgnoreCase("Pendiente")) {
                this.habitacion.setEstado("En Mantenimiento");
            }
        }

        System.out.println("Estado actualizado: " + this);
    }

    public String getCodMant() { return codMant; }
    public String getLimpieza() { return limpieza; }
    public void setLimpieza(String limpieza) { this.limpieza = limpieza; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Empleado getEmpleado() { return empleado; }
    public Habitacion getHabitacion() { return habitacion; }
    public ArrayList<Gasto> getGastos() { return gastos; }

    @Override
    public String toString() {
        String habInfo = (habitacion != null) ? "Hab. " + habitacion.getNumero() : "Sector General";
        String empInfo = (empleado != null) ? empleado.getNombre() + " " + empleado.getApellido() : "Sin asignar";
        return "[" + codMant + "] " + habInfo + " | Estado: " + estado + " | Limpieza: " + limpieza
                + " | Desc: " + descripcion + " | Resp: " + empInfo;
    }
}
