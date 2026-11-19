import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Turno {
    private String codTurno;
    private LocalDate dia;
    private String estado; // "Confirmado", "Cancelado"
    private String horario;
    private String tipo; // Ej: "Masaje Descontracturante", "Circuito Hídrico", "Sauna"

    // Colaboradores según CRC y Modelo de Dominio
    private Huesped huesped;
    private Empleado empleado;
    private Pago pago; // Multiplicidad 0..1

    public Turno(String codTurno, LocalDate dia, String horario, String tipo, Huesped huesped, Empleado empleado) {
        this.codTurno = codTurno;
        this.dia = dia;
        this.estado = "Confirmado";
        this.horario = horario;
        this.tipo = tipo;
        this.huesped = huesped;
        this.empleado = empleado;
        this.pago = null;
    }

    // 1. BUSCAR DISPONIBILIDAD (CU-02.4)
    public static boolean buscarDisponibilidad(ArrayList<Turno> turnos, LocalDate dia, String horario, String tipo) {
        for (Turno t : turnos) {
            if (!t.getEstado().equalsIgnoreCase("Cancelado") && t.getDia().equals(dia) && t.getHorario().equalsIgnoreCase(horario)
                    && t.getTipo().equalsIgnoreCase(tipo)) {
                return false; // Horario ocupado para ese servicio
            }
        }
        return true;
    }

    // 2. REGISTRAR TURNO (CU-02.1 + CU-09 Forma de pago + CU-10 Buscar Huésped/Cliente)
    public static Turno registrarTurno(Scanner sc, ArrayList<Turno> turnos, ArrayList<Huesped> huespedes,
                                       ArrayList<Reserva> reservas, ArrayList<Pago> pagos, Empleado empleado) {
        System.out.println("\n--- REGISTRAR TURNO SPA ---");
        System.out.print("Ingrese DNI del cliente/huésped: ");
        String dni = sc.nextLine().trim();

        Huesped huesped = null;
        for (Huesped h : huespedes) {
            if (h.getDni().equalsIgnoreCase(dni)) {
                huesped = h;
                break;
            }
        }

        boolean esHuespedRegistrado = (huesped != null);
        if (!esHuespedRegistrado) {
            System.out.println("El DNI no figura como huésped registrado. Debe abonar el turno en el momento.");
        }

        System.out.print("Servicio / Tipo (ej: Masaje, Sauna, Hidrico): ");
        String tipo = sc.nextLine().trim();
        System.out.print("Fecha del turno (AAAA-MM-DD): ");
        LocalDate dia = LocalDate.parse(sc.nextLine().trim());
        System.out.print("Horario (ej: 15:00): ");
        String horario = sc.nextLine().trim();

        // Incluye CU-02.4 Buscar disponibilidad
        if (!buscarDisponibilidad(turnos, dia, horario, tipo)) {
            System.out.println("Horario no disponible para ese servicio en la fecha indicada.");
            return null;
        }

        System.out.print("Ingrese valor del servicio: $");
        double valor = sc.nextDouble();
        sc.nextLine();

        String codTurno = "TUR-" + (100 + turnos.size() + 1);
        Turno nuevoTurno = new Turno(codTurno, dia, horario, tipo, huesped, empleado);

        boolean cobrarAhora = true;
        if (esHuespedRegistrado) {
            System.out.println("Modalidad de pago (CU-09):");
            System.out.println("1 - Pago directo");
            System.out.println("2 - Cargar a Check-out (Cuenta de habitación)");
            System.out.print("Opción: ");
            int opPago = sc.nextInt();
            sc.nextLine();

            if (opPago == 2) {
                boolean alojado = reservas.stream()
                        .anyMatch(r -> r.getHuesped().getDni().equalsIgnoreCase(dni) && r.getEstado().equalsIgnoreCase("Check-In"));
                if (!alojado) {
                    System.out.println("Sin estadía activa. Debe abonar el turno en el momento.");
                    cobrarAhora = true;
                } else {
                    cobrarAhora = false;
                }
            }
        } else {
            System.out.println("Modalidad de pago: Pago en el momento (obligatorio para quien no figura como huésped).");
        }

        if (cobrarAhora) {
            System.out.print("¿Confirma pago en el momento de $" + valor + "? (S/N): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                System.out.println("Pago no confirmado. Turno no registrado.");
                return null;
            }
            String codPago = "PAG-" + (100 + pagos.size() + 1);
            Pago pago = new Pago(codPago, valor, LocalDate.now(), "Aprobado", huesped);
            pago.getTurnos().add(nuevoTurno);
            nuevoTurno.setPago(pago);
            pagos.add(pago);
            if (huesped != null) {
                huesped.getPagos().add(pago);
            }
        }

        turnos.add(nuevoTurno);
        if (huesped != null) {
            huesped.getTurnos().add(nuevoTurno);
        }
        System.out.println("Turno registrado: " + nuevoTurno);
        return nuevoTurno;
    }

    // 3. ACTUALIZAR TURNO (CU-02.2)
    public void actualizarTurno(Scanner sc, ArrayList<Turno> turnos) {
        System.out.println("\n--- ACTUALIZAR TURNO " + this.codTurno + " ---");
        System.out.print("Nueva fecha (AAAA-MM-DD): ");
        LocalDate nuevoDia = LocalDate.parse(sc.nextLine().trim());
        System.out.print("Nuevo horario (ej: 17:00): ");
        String nuevoHorario = sc.nextLine().trim();

        ArrayList<Turno> otros = new ArrayList<>(turnos);
        otros.remove(this);

        if (!buscarDisponibilidad(otros, nuevoDia, nuevoHorario, this.tipo)) {
            System.out.println("Falta de disponibilidad en el nuevo horario.");
            return;
        }

        this.dia = nuevoDia;
        this.horario = nuevoHorario;
        System.out.println("Turno actualizado: " + this);
    }

    // 4. ELIMINAR TURNO (CU-02.3)
    public void eliminarTurno() {
        this.estado = "Cancelado";
        if (this.pago != null) {
            System.out.println("Anulando pago asociado " + this.pago.getCodPago());
            this.pago.actualizarPago(0, "Anulado");
            this.pago = null;
        }
        System.out.println("Turno " + this.codTurno + " eliminado/cancelado.");
    }

    public String getCodTurno() { return codTurno; }
    public LocalDate getDia() { return dia; }
    public String getEstado() { return estado; }
    public String getHorario() { return horario; }
    public String getTipo() { return tipo; }
    public Huesped getHuesped() { return huesped; }
    public Empleado getEmpleado() { return empleado; }
    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    @Override
    public String toString() {
        String estadoPago = (pago != null) ? "Pagado (" + pago.getCodPago() + ")" : "Pendiente (Cargo Hab.)";
        String nombreCliente = (huesped != null) ? huesped.getNombre() : "No registrado (Pago en el momento)";
        return "[" + codTurno + "] " + tipo + " | " + dia + " " + horario + " | Cliente: " + nombreCliente + " | " + estadoPago + " | Estado: " + estado;
    }
}