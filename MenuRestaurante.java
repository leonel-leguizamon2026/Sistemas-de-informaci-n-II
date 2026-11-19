import java.util.ArrayList;
import java.util.Scanner;

public class MenuRestaurante {

    public static void mostrar(Scanner sc, Empleado emp, Habitacion[] habitaciones,
                               ArrayList<Pedido> pedidos, ArrayList<Huesped> huespedes,
                               ArrayList<Reserva> reservas, ArrayList<Pago> pagos,
                               ArrayList<Mantenimiento> mantenimientos) {
        int op;
        do {
            System.out.println("\n--- MENU RESTAURANTE / COMANDO (" + emp.getNombre() + " " + emp.getApellido() + ") ---");
            System.out.println("1 - Tomar Pedido (CU-01.1 / CU-09 / CU-10)");
            System.out.println("2 - Modificar Pedido (CU-01.2)");
            System.out.println("3 - Cancelar Pedido (CU-01.3)");
            System.out.println("4 - Registrar / Buscar Cliente o Huesped (CU-10 / CU-11)");
            System.out.println("5 - Reportar Mantenimiento (CU-08)");
            System.out.println("0 - Cerrar Sesion");
            System.out.print("Opcion: ");

            op = leerEntero(sc);

            switch (op) {
                case 1:
                    Pedido.tomarPedido(sc, pedidos, huespedes, reservas, pagos, emp);
                    break;
                case 2: {
                    Pedido p = seleccionarPedido(sc, pedidos);
                    if (p != null) {
                        p.modificarPedido(sc);
                    }
                    break;
                }
                case 3: {
                    Pedido p = seleccionarPedido(sc, pedidos);
                    if (p != null) {
                        p.cancelarPedido();
                    }
                    break;
                }
                case 4:
                    gestionarClienteRapido(sc, huespedes);
                    break;
                case 5:
                    Mantenimiento.registrarReporte(sc, mantenimientos, habitaciones, emp);
                    break;
                case 0:
                    System.out.println("Cerrando sesion de Restaurante...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (op != 0);
    }

    private static Pedido seleccionarPedido(Scanner sc, ArrayList<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return null;
        }
        System.out.println("\nPedidos registrados:");
        for (int i = 0; i < pedidos.size(); i++) {
            System.out.println((i + 1) + ". " + pedidos.get(i));
        }
        System.out.print("Seleccione numero de pedido (0 para cancelar): ");
        int idx = leerEntero(sc) - 1;
        if (idx >= 0 && idx < pedidos.size()) {
            return pedidos.get(idx);
        }
        return null;
    }

    private static void gestionarClienteRapido(Scanner sc, ArrayList<Huesped> huespedes) {
        System.out.print("Ingrese DNI del cliente/huesped: ");
        String dni = sc.nextLine().trim();
        for (Huesped h : huespedes) {
            if (h.getDni().equalsIgnoreCase(dni)) {
                System.out.println("Registrado: " + h);
                return;
            }
        }
        System.out.println("Cliente no encontrado.");
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
}