import java.util.ArrayList;
import java.util.Scanner;

public class MenuLimpieza {

    public static void mostrar(Scanner sc, Empleado emp, Habitacion[] habitaciones,
                               ArrayList<Mantenimiento> mantenimientos) {
        int op;
        do {
            System.out.println("\n--- MENU LIMPIEZA Y MANTENIMIENTO (" + emp.getNombre() + " " + emp.getApellido() + ") ---");
            System.out.println("1 - Ver Estado y Disponibilidad de Habitaciones (CU-03.1)");
            System.out.println("2 - Actualizar Estado de Limpieza de Habitacion (CU-03.2)");
            System.out.println("3 - Reportar Desperfecto / Mantenimiento (CU-08)");
            System.out.println("4 - Actualizar Estado de Mantenimiento");
            System.out.println("0 - Cerrar Sesion");
            System.out.print("Opcion: ");

            op = leerEntero(sc);

            switch (op) {
                case 1:
                    System.out.println("\n-- Estado de Habitaciones --");
                    for (Habitacion h : habitaciones) {
                        System.out.println("  " + h + " | Disponible: " + (h.informarDisponibilidad() ? "Si" : "No"));
                    }
                    break;
                case 2:
                    actualizarLimpiezaHabitacion(sc, habitaciones);
                    break;
                case 3:
                    Mantenimiento.registrarReporte(sc, mantenimientos, habitaciones, emp);
                    break;
                case 4: {
                    if (mantenimientos.isEmpty()) {
                        System.out.println("No hay reportes de mantenimiento.");
                    } else {
                        for (int i = 0; i < mantenimientos.size(); i++) {
                            System.out.println((i + 1) + ". " + mantenimientos.get(i));
                        }
                        System.out.print("Seleccione numero de reporte a actualizar: ");
                        int idx = leerEntero(sc) - 1;
                        if (idx >= 0 && idx < mantenimientos.size()) {
                            mantenimientos.get(idx).actualizarEstado(sc);
                        }
                    }
                    break;
                }
                case 0:
                    System.out.println("Cerrando sesion de Limpieza...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (op != 0);
    }

    private static void actualizarLimpiezaHabitacion(Scanner sc, Habitacion[] habitaciones) {
        System.out.print("Ingrese numero de habitacion: ");
        int num = leerEntero(sc);
        for (Habitacion h : habitaciones) {
            if (h.getNumero() == num) {
                System.out.println("Actual: " + h);
                System.out.println("1 - Marcar como Limpia / Disponible");
                System.out.println("2 - Marcar como Pendiente Limpieza");
                System.out.print("Opcion: ");
                int sel = leerEntero(sc);
                if (sel == 1) {
                    h.setEstado("Disponible");
                    System.out.println("Habitacion " + num + " actualizada a Disponible.");
                } else if (sel == 2) {
                    h.setEstado("Pendiente Limpieza");
                    System.out.println("Habitacion " + num + " actualizada a Pendiente Limpieza.");
                }
                return;
            }
        }
        System.out.println("Habitacion no encontrada.");
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