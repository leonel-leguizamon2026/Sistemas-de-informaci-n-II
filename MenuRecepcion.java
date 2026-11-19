import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuRecepcion {

    public static void mostrar(Scanner sc, Empleado emp, Habitacion[] habitaciones,
                               ArrayList<Reserva> reservas, ArrayList<Huesped> huespedes,
                               ArrayList<Pedido> pedidos, ArrayList<Turno> turnos,
                               ArrayList<Pago> pagos, ArrayList<Gasto> gastos,
                               ArrayList<Mantenimiento> mantenimientos) {
        int op;
        do {
            System.out.println("\n--- MENU RECEPCION (" + emp.getNombre() + " " + emp.getApellido() + ") ---");
            System.out.println("1 - Gestionar Reservas (Crear / Actualizar / Eliminar / Buscar)");
            System.out.println("2 - Check-In / Check-Out y Verificar Pago");
            System.out.println("3 - Consultar Disponibilidad de Habitaciones");
            System.out.println("0 - Cerrar Sesion");
            System.out.print("Opcion: ");

            op = leerEntero(sc);

            switch (op) {
                case 1:
                    submenuReservas(sc, emp, habitaciones, reservas, huespedes);
                    break;
                case 2:
                    submenuCheckInOut(sc, reservas, pedidos, turnos, pagos);
                    break;
                case 3:
                    consultarDisponibilidadHabitaciones(sc, habitaciones, reservas);
                    break;
                case 0:
                    System.out.println("Cerrando sesion de Recepcion...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (op != 0);
    }

    private static void submenuReservas(Scanner sc, Empleado emp, Habitacion[] habitaciones,
                                         ArrayList<Reserva> reservas, ArrayList<Huesped> huespedes) {
        int opRes;
        do {
            System.out.println("\n-- Gestion de Reservas --");
            System.out.println("1 - Crear Reserva");
            System.out.println("2 - Actualizar Reserva");
            System.out.println("3 - Eliminar Reserva");
            System.out.println("4 - Buscar Reserva");
            System.out.println("0 - Volver");
            System.out.print("Opcion: ");

            opRes = leerEntero(sc);

            switch (opRes) {
                case 1:
                    Reserva.crearReserva(sc, reservas, huespedes, habitaciones, emp);
                    break;
                case 2: {
                    Reserva r = Reserva.buscarReserva(sc, reservas);
                    if (r != null) {
                        r.actualizarReserva(sc, reservas, habitaciones);
                    }
                    break;
                }
                case 3: {
                    Reserva r = Reserva.buscarReserva(sc, reservas);
                    if (r != null) {
                        System.out.print("¿Confirma cancelar la reserva " + r.getCodReserva() + "? (S/N): ");
                        if (sc.nextLine().trim().equalsIgnoreCase("S")) {
                            r.eliminarReserva();
                        }
                    }
                    break;
                }
                case 4:
                    Reserva.buscarReserva(sc, reservas);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opRes != 0);
    }

    private static void submenuCheckInOut(Scanner sc, ArrayList<Reserva> reservas,
                                          ArrayList<Pedido> pedidos, ArrayList<Turno> turnos,
                                          ArrayList<Pago> pagos) {
        int opChk;
        do {
            System.out.println("\n-- Check-In / Check-Out --");
            System.out.println("1 - Realizar Check-In");
            System.out.println("2 - Realizar Check-Out");
            System.out.println("3 - Verificar Estado de un Pago");
            System.out.println("0 - Volver");
            System.out.print("Opcion: ");

            opChk = leerEntero(sc);

            switch (opChk) {
                case 1:
                    realizarCheckIn(sc, reservas);
                    break;
                case 2:
                    realizarCheckOut(sc, reservas, pedidos, turnos, pagos);
                    break;
                case 3: {
                    if (pagos.isEmpty()) {
                        System.out.println("No hay pagos registrados.");
                    } else {
                        for (int i = 0; i < pagos.size(); i++) {
                            System.out.println((i + 1) + ". " + pagos.get(i));
                        }
                        System.out.print("Seleccione numero de pago a verificar: ");
                        int idx = leerEntero(sc) - 1;
                        if (idx >= 0 && idx < pagos.size()) {
                            pagos.get(idx).verificarPago();
                        }
                    }
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opChk != 0);
    }

    private static void realizarCheckIn(Scanner sc, ArrayList<Reserva> reservas) {
        Reserva r = Reserva.buscarReserva(sc, reservas);
        if (r == null) return;

        if (!r.getEstado().equalsIgnoreCase("Activa")) {
            System.out.println("La reserva no esta en estado Activa (Estado actual: " + r.getEstado() + ").");
            return;
        }

        System.out.print("Verificando credito del huesped (ingrese limite/garantia aprobada o 0 si rechazada): $");
        double credito = leerDouble(sc);
        if (credito <= 0) {
            System.out.println("Credito insuficiente o rechazado. No se completa el Check-In.");
            return;
        }

        r.setEstado("Check-In");
        for (Habitacion h : r.getHabitaciones()) {
            h.setEstado("Ocupada");
        }
        System.out.println("Check-In registrado correctamente para " + r.getCodReserva());
    }

    private static void realizarCheckOut(Scanner sc, ArrayList<Reserva> reservas,
                                           ArrayList<Pedido> pedidos, ArrayList<Turno> turnos,
                                           ArrayList<Pago> pagos) {
        Reserva r = Reserva.buscarReserva(sc, reservas);
        if (r == null) return;

        if (!r.getEstado().equalsIgnoreCase("Check-In")) {
            System.out.println("La reserva no tiene un Check-In activo (Estado: " + r.getEstado() + ").");
            return;
        }

        long noches = Math.max(1, ChronoUnit.DAYS.between(r.getFechaEntrada(), r.getFechaSalida()));
        double tarifaPorNoche = 0;
        for (Habitacion h : r.getHabitaciones()) {
            tarifaPorNoche += (h.getCapacidad() == 1) ? 25000 : (h.getCapacidad() == 2) ? 40000 : 65000;
        }
        double costoEstadia = tarifaPorNoche * noches;

        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getHuesped().getDni().equalsIgnoreCase(r.getHuesped().getDni())
                    && p.getEstado().equalsIgnoreCase("Activo")
                    && p.getPago() == null) {
                pedidosPendientes.add(p);
            }
        }

        ArrayList<Turno> turnosPendientes = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getHuesped().getDni().equalsIgnoreCase(r.getHuesped().getDni())
                    && t.getEstado().equalsIgnoreCase("Confirmado")
                    && t.getPago() == null) {
                turnosPendientes.add(t);
            }
        }

        Pago.registrarPagoConsolidado(pagos, r.getHuesped(), r, pedidosPendientes, turnosPendientes, costoEstadia);

        r.setEstado("Finalizada");
        for (Habitacion h : r.getHabitaciones()) {
            h.setEstado("Pendiente Limpieza");
        }
        System.out.println("Check-Out completado. Reserva finalizada y habitaciones enviadas a Pendiente Limpieza.");
    }

    private static void consultarDisponibilidadHabitaciones(Scanner sc, Habitacion[] habitaciones, ArrayList<Reserva> reservas) {
        LocalDate entrada = leerFechaObligatoria(sc, "Fecha de ingreso (AAAA-MM-DD): ");
        LocalDate salida = leerFechaObligatoria(sc, "Fecha de egreso (AAAA-MM-DD): ");
        while (!salida.isAfter(entrada)) {
            System.out.println("La fecha de egreso debe ser posterior a la de ingreso.");
            salida = leerFechaObligatoria(sc, "Fecha de egreso (AAAA-MM-DD): ");
        }

        System.out.println("\nHabitaciones disponibles del " + entrada + " al " + salida + ":");
        boolean alguna = false;
        for (Habitacion h : habitaciones) {
            boolean estadoHabilitado = !h.getEstado().equalsIgnoreCase("Pendiente Limpieza")
                    && !h.getEstado().equalsIgnoreCase("En Mantenimiento");
            if (estadoHabilitado && h.informarDisponibilidad(entrada, salida, reservas)) {
                System.out.println("  " + h);
                alguna = true;
            }
        }
        if (!alguna) {
            System.out.println("No hay habitaciones disponibles para ese rango.");
        }
    }

    private static int leerEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un numero valido: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    private static double leerDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Ingrese un monto valido: ");
            sc.next();
        }
        double val = sc.nextDouble();
        sc.nextLine();
        return val;
    }

    private static LocalDate leerFechaObligatoria(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use AAAA-MM-DD.");
            }
        }
    }
}