import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuSpa {

    public static void mostrar(Scanner sc, Empleado emp, Habitacion[] habitaciones,
                               ArrayList<Turno> turnos, ArrayList<Huesped> huespedes,
                               ArrayList<Reserva> reservas, ArrayList<Pago> pagos,
                               ArrayList<Mantenimiento> mantenimientos) {
        int op;
        do {
            System.out.println("\n--- MENU SPA / SERVICIOS (" + emp.getNombre() + " " + emp.getApellido() + ") ---");
            System.out.println("1 - Registrar Turno (CU-02.1)");
            System.out.println("2 - Actualizar Turno (CU-02.2)");
            System.out.println("3 - Eliminar Turno (CU-02.3)");
            System.out.println("4 - Buscar Disponibilidad de Turno (CU-02.4)");
            System.out.println("5 - Reportar Mantenimiento (CU-08)");
            System.out.println("0 - Cerrar Sesion");
            System.out.print("Opcion: ");

            op = leerEntero(sc);

            switch (op) {
                case 1:
                    Turno.registrarTurno(sc, turnos, huespedes, reservas, pagos, emp);
                    break;
                case 2: {
                    Turno t = seleccionarTurno(sc, turnos);
                    if (t != null) {
                        t.actualizarTurno(sc, turnos);
                    }
                    break;
                }
                case 3: {
                    Turno t = seleccionarTurno(sc, turnos);
                    if (t != null) {
                        t.eliminarTurno();
                    }
                    break;
                }
                case 4: {
                    System.out.print("Servicio / Tipo: ");
                    String tipo = sc.nextLine().trim();
                    LocalDate dia = leerFecha(sc, "Fecha (AAAA-MM-DD): ");
                    System.out.print("Horario (ej: 15:00): ");
                    String horario = sc.nextLine().trim();
                    boolean libre = Turno.buscarDisponibilidad(turnos, dia, horario, tipo);
                    System.out.println(libre ? "Horario DISPONIBLE." : "Horario OCUPADO.");
                    break;
                }
                case 5:
                    Mantenimiento.registrarReporte(sc, mantenimientos, habitaciones, emp);
                    break;
                case 0:
                    System.out.println("Cerrando sesion de SPA...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (op != 0);
    }

    private static Turno seleccionarTurno(Scanner sc, ArrayList<Turno> turnos) {
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos registrados.");
            return null;
        }
        System.out.println("\nTurnos registrados:");
        for (int i = 0; i < turnos.size(); i++) {
            System.out.println((i + 1) + ". " + turnos.get(i));
        }
        System.out.print("Seleccione numero de turno (0 para cancelar): ");
        int idx = leerEntero(sc) - 1;
        if (idx >= 0 && idx < turnos.size()) {
            return turnos.get(idx);
        }
        return null;
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

    private static LocalDate leerFecha(Scanner sc, String msg) {
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