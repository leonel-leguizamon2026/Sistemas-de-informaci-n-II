import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Empleado> empleados = Empleado.crearEmpleadosIniciales();
        ArrayList<Huesped> huespedes = new ArrayList<>();
        Habitacion[] habitaciones = inicializarHabitaciones();
        ArrayList<Reserva> reservas = new ArrayList<>();
        ArrayList<Pedido> pedidos = new ArrayList<>();
        ArrayList<Turno> turnos = new ArrayList<>();
        ArrayList<Pago> pagos = new ArrayList<>();
        ArrayList<Gasto> gastos = new ArrayList<>();
        ArrayList<Mantenimiento> mantenimientos = new ArrayList<>();
        ArrayList<RegistroFichaje> fichajes = new ArrayList<>();

        int legajoIngresado;

        do {
            System.out.println("\n==========================================");
            System.out.println("        SISTEMA DE GESTION HOTELERA       ");
            System.out.println("==========================================");
            System.out.println("Rangos de Legajo:");
            System.out.println("  100-199: Recepcion  | 200-299: Restaurante");
            System.out.println("  300-399: SPA        | 400-499: Limpieza");
            System.out.println("  500-599: Gerencia   | 0: Salir");
            System.out.print("Ingrese su numero de legajo: ");

            while (!sc.hasNextInt()) {
                System.out.print("Ingrese un numero valido: ");
                sc.next();
            }
            legajoIngresado = sc.nextInt();
            sc.nextLine();

            if (legajoIngresado == 0) {
                System.out.println("Cerrando sistema...");
                break;
            }

            Empleado emp = buscarEmpleado(empleados, legajoIngresado);
            if (emp == null) {
                System.out.println("Error: Legajo no encontrado o no autorizado.");
                continue;
            }

            fichajes.add(new RegistroFichaje(emp.getLegajo(), emp.getNombre() + " " + emp.getApellido(),
                    emp.getRol(), "Entrada", LocalDateTime.now()));

            System.out.println("\nBienvenido/a " + emp.getNombre() + " " + emp.getApellido()
                    + " | Rol: " + emp.getRol() + " (Legajo: " + emp.getLegajo() + ")");

            if (legajoIngresado >= 100 && legajoIngresado <= 199) {
                MenuRecepcion.mostrar(sc, emp, habitaciones, reservas, huespedes, pedidos, turnos, pagos, gastos, mantenimientos);
            } else if (legajoIngresado >= 200 && legajoIngresado <= 299) {
                MenuRestaurante.mostrar(sc, emp, habitaciones, pedidos, huespedes, reservas, pagos, mantenimientos);
            } else if (legajoIngresado >= 300 && legajoIngresado <= 399) {
                MenuSpa.mostrar(sc, emp, habitaciones, turnos, huespedes, reservas, pagos, mantenimientos);
            } else if (legajoIngresado >= 400 && legajoIngresado <= 499) {
                MenuLimpieza.mostrar(sc, emp, habitaciones, mantenimientos);
            } else if (legajoIngresado >= 500 && legajoIngresado <= 599) {
                MenuGerencia.mostrar(sc, emp, habitaciones, reservas, pedidos, turnos, pagos, gastos, mantenimientos, fichajes);
            }

            fichajes.add(new RegistroFichaje(emp.getLegajo(), emp.getNombre() + " " + emp.getApellido(),
                    emp.getRol(), "Salida", LocalDateTime.now()));

        } while (legajoIngresado != 0);

        sc.close();
    }

    private static Habitacion[] inicializarHabitaciones() {
        // 15 habitaciones: 10 Singles (101-110), 3 Dobles (111-113), 2 Suites (114-115)
        Habitacion[] habs = new Habitacion[15];
        for (int i = 0; i < 15; i++) {
            int numero = 101 + i;
            int capacidad = (i < 10) ? 1 : (i < 13) ? 2 : 4;
            habs[i] = new Habitacion(numero, capacidad, "Disponible");
        }
        return habs;
    }

    private static Empleado buscarEmpleado(ArrayList<Empleado> empleados, int legajo) {
        for (Empleado e : empleados) {
            if (e.getLegajo() == legajo) {
                return e;
            }
        }
        return null;
    }
}