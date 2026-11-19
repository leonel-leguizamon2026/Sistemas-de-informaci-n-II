import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reserva {
    private String codReserva;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String estado; // "Activa", "Check-In", "Finalizada", "Cancelada"

    // Colaboradores según CRC y Modelo de Dominio[span_0](start_span)[span_0](end_span)[span_1](start_span)[span_1](end_span)
    private Huesped huesped;
    private ArrayList<Habitacion> habitaciones; // Multiplicidad 1..*[span_2](start_span)[span_2](end_span)[span_3](start_span)[span_3](end_span)
    private Empleado empleado;
    private Pago pago;

    public Reserva(String codReserva, LocalDate fechaEntrada, LocalDate fechaSalida,
                   Huesped huesped, ArrayList<Habitacion> habitaciones, Empleado empleado) {
        this.codReserva = codReserva;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = "Activa";
        this.huesped = huesped;
        this.habitaciones = habitaciones;
        this.empleado = empleado;
        this.pago = null;
    }

    private static boolean habitacionDisponibleParaReserva(Habitacion h, LocalDate entrada,
                                                          LocalDate salida, ArrayList<Reserva> reservas) {
        if (h == null || h.getEstado() == null) {
            return false;
        }
        if (h.getEstado().equalsIgnoreCase("Pendiente Limpieza")
                || h.getEstado().equalsIgnoreCase("En Mantenimiento")
                || h.getEstado().equalsIgnoreCase("Ocupada")) {
            return false;
        }
        return h.informarDisponibilidad(entrada, salida, reservas);
    }

    // 1. CREAR RESERVA (CU-04.1)[span_4](start_span)[span_4](end_span)[span_5](start_span)[span_5](end_span)
    public static Reserva crearReserva(Scanner sc, ArrayList<Reserva> reservas, ArrayList<Huesped> huespedes,
                                       Habitacion[] todasHabitaciones, Empleado empleadoLogueado) {
        System.out.println("\n--- CREAR RESERVA ---");
        System.out.print("Ingrese DNI del huésped: ");
        String dni = sc.nextLine().trim();

        Huesped huesped = null;
        for (Huesped h : huespedes) {
            if (h.getDni().equalsIgnoreCase(dni)) {
                huesped = h;
                break;
            }
        }

        // Flujo alternativo 2a: Usuario no encontrado -> Crear usuario (CU-04.1a)[span_6](start_span)[span_6](end_span)[span_7](start_span)[span_7](end_span)
        if (huesped == null) {
            System.out.println("Huésped no registrado. Complete los datos para darlo de alta:");
            System.out.print("Nombre: ");
            String nombre = sc.nextLine().trim();
            System.out.print("Apellido: ");
            String apellido = sc.nextLine().trim();
            System.out.print("Teléfono: ");
            String telefono = sc.nextLine().trim();
            String codHuesped = "HUE-" + (huespedes.size() + 1);
            huesped = Huesped.crearHuesped(huespedes, dni, nombre, apellido, telefono, codHuesped);
        } else {
            System.out.println("Huésped encontrado: " + huesped);
        }

        LocalDate entrada = solicitarFecha(sc, "Ingrese fecha de entrada (AAAA-MM-DD): ");
        LocalDate salida = solicitarFecha(sc, "Ingrese fecha de salida (AAAA-MM-DD): ");
        while (!salida.isAfter(entrada)) {
            System.out.println("Error: La fecha de salida debe ser posterior a la entrada.");
            salida = solicitarFecha(sc, "Ingrese fecha de salida (AAAA-MM-DD): ");
        }

        // Consultar disponibilidad (CU-04.4) y permitir seleccionar 1 o más habitaciones (1..*)[span_8](start_span)[span_8](end_span)[span_9](start_span)[span_9](end_span)
        ArrayList<Habitacion> seleccionadas = new ArrayList<>();
        String agregarOtra;
        do {
            System.out.println("\nHabitaciones disponibles para el período:");
            boolean hayLibres = false;
            for (Habitacion h : todasHabitaciones) {
                if (habitacionDisponibleParaReserva(h, entrada, salida, reservas) && !seleccionadas.contains(h)) {
                    System.out.println("  " + h);
                    hayLibres = true;
                }
            }

            if (!hayLibres && seleccionadas.isEmpty()) {
                System.out.println("No hay habitaciones disponibles para esas fechas.");
                return null;
            } else if (!hayLibres) {
                System.out.println("No quedan más habitaciones disponibles.");
                break;
            }

            System.out.print("Ingrese el número de habitación a reservar: ");
            while (!sc.hasNextInt()) {
                System.out.print("Ingrese un número de habitación válido: ");
                sc.next();
            }
            int numHab = sc.nextInt();
            sc.nextLine();

            Habitacion habBuscada = null;
            for (Habitacion h : todasHabitaciones) {
                if (h.getNumero() == numHab) {
                    habBuscada = h;
                    break;
                }
            }

            if (habBuscada == null) {
                System.out.println("Número de habitación inexistente.");
            } else if (seleccionadas.contains(habBuscada)) {
                System.out.println("La habitación " + numHab + " ya fue agregada a esta reserva.");
            } else if (!habitacionDisponibleParaReserva(habBuscada, entrada, salida, reservas)) {
                System.out.println("La habitación " + numHab + " no se puede seleccionar (Estado actual: "
                        + habBuscada.getEstado() + " o no disponible en esas fechas).");
            } else {
                seleccionadas.add(habBuscada);
                System.out.println("Habitación " + numHab + " agregada a la reserva.");
            }

            System.out.print("¿Desea agregar otra habitación a esta reserva? (S/N): ");
            agregarOtra = sc.nextLine().trim();
        } while (agregarOtra.equalsIgnoreCase("S") || seleccionadas.isEmpty());

        String codRes = "RES-" + (100 + reservas.size() + 1);
        Reserva nuevaReserva = new Reserva(codRes, entrada, salida, huesped, seleccionadas, empleadoLogueado);
        reservas.add(nuevaReserva);
        huesped.getReservas().add(nuevaReserva);

        System.out.println("Reserva creada exitosamente: " + nuevaReserva);
        return nuevaReserva;
    }

    // 2. ACTUALIZAR RESERVA (CU-04.2)[span_10](start_span)[span_10](end_span)[span_11](start_span)[span_11](end_span)
    public void actualizarReserva(Scanner sc, ArrayList<Reserva> reservas, Habitacion[] todasHabitaciones) {
        System.out.println("\n--- ACTUALIZAR RESERVA " + this.codReserva + " ---");
        LocalDate nuevaEntrada = solicitarFecha(sc, "Nueva fecha de entrada (AAAA-MM-DD): ");
        LocalDate nuevaSalida = solicitarFecha(sc, "Nueva fecha de salida (AAAA-MM-DD): ");
        while (!nuevaSalida.isAfter(nuevaEntrada)) {
            System.out.println("Error: La fecha de salida debe ser posterior a la entrada.");
            nuevaSalida = solicitarFecha(sc, "Nueva fecha de salida (AAAA-MM-DD): ");
        }

        // Excluir esta misma reserva al revalidar disponibilidad
        ArrayList<Reserva> otrasReservas = new ArrayList<>(reservas);
        otrasReservas.remove(this);

        for (Habitacion h : this.habitaciones) {
            if (!habitacionDisponibleParaReserva(h, nuevaEntrada, nuevaSalida, otrasReservas)) {
                System.out.println("Conflicto: La Hab. " + h.getNumero()
                        + " no está disponible (Estado: " + h.getEstado() + ") en las nuevas fechas.");
                return;
            }
        }

        this.fechaEntrada = nuevaEntrada;
        this.fechaSalida = nuevaSalida;
        System.out.println("Reserva actualizada correctamente: " + this);
    }

    // 3. ELIMINAR / CANCELAR RESERVA (CU-04.3)[span_12](start_span)[span_12](end_span)[span_13](start_span)[span_13](end_span)
    public void eliminarReserva() {
        this.estado = "Cancelada";
        for (Habitacion h : this.habitaciones) {
            if (h.getEstado().equalsIgnoreCase("Ocupada")) {
                h.setEstado("Disponible");
            }
        }
        System.out.println("Reserva " + this.codReserva + " cancelada y habitaciones liberadas.");
    }

    // 4. BUSCAR RESERVA (CU-04.5, CU-04.5a y CU-04.5b)[span_14](start_span)[span_14](end_span)[span_15](start_span)[span_15](end_span)
    public static Reserva buscarReserva(Scanner sc, ArrayList<Reserva> reservas) {
        System.out.println("\n--- BUSCAR RESERVA ---");
        System.out.println("1 - Buscar por ID (CodReserva)");
        System.out.println("2 - Buscar por Nombre o DNI del Huésped");
        System.out.print("Opción: ");
        int op = sc.nextInt();
        sc.nextLine();

        if (op == 1) {
            System.out.print("Ingrese CodReserva (ej: RES-101): ");
            String id = sc.nextLine().trim();
            for (Reserva r : reservas) {
                if (r.getCodReserva().equalsIgnoreCase(id)) {
                    System.out.println("Encontrada: " + r);
                    return r;
                }
            }
        } else if (op == 2) {
            System.out.print("Ingrese Nombre, Apellido o DNI: ");
            String criterio = sc.nextLine().trim().toLowerCase();
            ArrayList<Reserva> coincidencias = new ArrayList<>();
            for (Reserva r : reservas) {
                Huesped h = r.getHuesped();
                if (h.getDni().toLowerCase().contains(criterio)
                        || h.getNombre().toLowerCase().contains(criterio)
                        || h.getApellido().toLowerCase().contains(criterio)) {
                    coincidencias.add(r);
                }
            }
            if (coincidencias.size() == 1) {
                System.out.println("Encontrada: " + coincidencias.get(0));
                return coincidencias.get(0);
            } else if (coincidencias.size() > 1) {
                System.out.println("Se encontraron varias reservas:");
                for (int i = 0; i < coincidencias.size(); i++) {
                    System.out.println((i + 1) + " - " + coincidencias.get(i));
                }
                System.out.print("Seleccione el número: ");
                int sel = sc.nextInt();
                sc.nextLine();
                if (sel >= 1 && sel <= coincidencias.size()) {
                    return coincidencias.get(sel - 1);
                }
            }
        }
        System.out.println("No se encontró ninguna reserva con ese criterio.");
        return null;
    }

    private static LocalDate solicitarFecha(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use AAAA-MM-DD (ej: 2026-11-20).");
            }
        }
    }

    // Getters y Setters
    public String getCodReserva() { return codReserva; }
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Huesped getHuesped() { return huesped; }
    public ArrayList<Habitacion> getHabitaciones() { return habitaciones; }
    public Empleado getEmpleado() { return empleado; }
    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    @Override
    public String toString() {
        StringBuilder habsStr = new StringBuilder();
        for (Habitacion h : habitaciones) {
            habsStr.append(h.getNumero()).append(" ");
        }
        return "[" + codReserva + "] Huésped: " + huesped.getNombre() + " " + huesped.getApellido()
                + " | Habs: [" + habsStr.toString().trim() + "] | " + fechaEntrada + " al " + fechaSalida
                + " | Estado: " + estado;
    }
}