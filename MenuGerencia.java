import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuGerencia {

    public static void mostrar(Scanner sc, Empleado emp, Habitacion[] habitaciones,
                               ArrayList<Reserva> reservas, ArrayList<Pedido> pedidos,
                               ArrayList<Turno> turnos, ArrayList<Pago> pagos,
                               ArrayList<Gasto> gastos, ArrayList<Mantenimiento> mantenimientos,
                               ArrayList<RegistroFichaje> fichajes) {
        int op;
        do {
            System.out.println("\n--- MENU GERENCIA (" + emp.getNombre() + " " + emp.getApellido() + ") ---");
            System.out.println("1 - Reporte de Entrada y Salida del Personal");
            System.out.println("2 - Reporte de Habitaciones");
            System.out.println("3 - Reporte de Mantenimiento");
            System.out.println("4 - Ingresos y Ganancias");
            System.out.println("5 - Gastos Generales y Balance");
            System.out.println("6 - Registrar / Pagar Gasto de Mantenimiento");
            System.out.println("0 - Cerrar Sesion");
            System.out.print("Opcion: ");

            op = leerEntero(sc);

            switch (op) {
                case 1: {
                    System.out.println("\n-- Fichajes de Entrada y Salida --");
                    if (fichajes == null || fichajes.isEmpty()) {
                        System.out.println("Sin registros de fichaje.");
                    } else {
                        for (RegistroFichaje f : fichajes) {
                            System.out.println("  " + f);
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.println("\n-- Reporte de Habitaciones --");
                    for (Habitacion h : habitaciones) {
                        System.out.println("  " + h);
                    }
                    System.out.println("Total reservas registradas: " + reservas.size());
                    break;
                }
                case 3: {
                    System.out.println("\n-- Reporte de Mantenimiento --");
                    if (mantenimientos.isEmpty()) {
                        System.out.println("Sin reportes de mantenimiento.");
                    } else {
                        for (Mantenimiento m : mantenimientos) {
                            System.out.println("  " + m + " (Gastos asociados: " + m.getGastos().size() + ")");
                        }
                    }
                    break;
                }
                case 4: {
                    System.out.println("\n-- Ingresos y Ganancias --");
                    LocalDate desde = leerFechaOpcional(sc, "Fecha inicio (AAAA-MM-DD, Enter para todo): ");
                    LocalDate hasta = leerFechaOpcional(sc, "Fecha fin (AAAA-MM-DD, Enter para todo): ");
                    double totalIngresos = 0;
                    for (Pago p : pagos) {
                        if (p.getEstado().equalsIgnoreCase("Aprobado")) {
                            boolean enRango = (desde == null || !p.getFecha().isBefore(desde))
                                    && (hasta == null || !p.getFecha().isAfter(hasta));
                            if (enRango) {
                                totalIngresos += p.getMontoTotal();
                            }
                        }
                    }
                    System.out.println("Ingresos aprobados en periodo: $" + totalIngresos);
                    System.out.println("Pedidos registrados: " + pedidos.size() + " | Turnos SPA: " + turnos.size());
                    break;
                }
                case 5: {
                    Gasto.consultarGasto(sc, gastos);
                    double ingresos = 0;
                    for (Pago p : pagos) {
                        if (p.getEstado().equalsIgnoreCase("Aprobado")) {
                            ingresos += p.getMontoTotal();
                        }
                    }
                    double egresos = 0;
                    for (Gasto g : gastos) {
                        egresos += g.getMonto();
                    }
                    System.out.println("Balance (Ingresos - Gastos): $" + (ingresos - egresos));
                    break;
                }
                case 6: {
                    if (mantenimientos.isEmpty()) {
                        System.out.println("No hay desperfectos/reportes de mantenimiento registrados.");
                    } else {
                        System.out.println("\n-- Reportes de Mantenimiento --");
                        for (int i = 0; i < mantenimientos.size(); i++) {
                            System.out.println((i + 1) + ". " + mantenimientos.get(i));
                        }
                        System.out.print("Seleccione el reporte a abonar/registrar gasto (0 para cancelar): ");
                        int idx = leerEntero(sc) - 1;
                        if (idx >= 0 && idx < mantenimientos.size()) {
                            Gasto.registrarGasto(sc, gastos, pagos, mantenimientos.get(idx));
                        }
                    }
                    break;
                }
                case 0:
                    System.out.println("Cerrando sesion de Gerencia...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (op != 0);
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

    private static LocalDate leerFechaOpcional(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return null;
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use AAAA-MM-DD.");
            }
        }
    }
}