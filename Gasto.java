import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Gasto {
    private String descripcion;
    private double monto;
    private LocalDate fecha;

    // Colaboradores según CRC y Modelo de Dominio
    private Mantenimiento mantenimiento;
    private Pago pago; // Multiplicidad 0..1 ("Se abona mediante")

    public Gasto(String descripcion, double monto, LocalDate fecha, Mantenimiento mantenimiento) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.mantenimiento = mantenimiento;
        this.pago = null;
    }

    // 1. REGISTRAR GASTO (CRC: Registrar gasto)
    public static Gasto registrarGasto(Scanner sc, ArrayList<Gasto> gastos, ArrayList<Pago> pagos,
                                       Mantenimiento mantenimientoAsociado) {
        System.out.println("\n--- REGISTRAR GASTO ---");
        String descripcion;
        do {
            System.out.print("Descripción del gasto: ");
            descripcion = sc.nextLine().trim();
            if (descripcion.isEmpty()) {
                System.out.println("La descripción no puede estar vacía.");
            }
        } while (descripcion.isEmpty());

        double monto;
        while (true) {
            System.out.print("Monto ($): ");
            try {
                monto = Double.parseDouble(sc.nextLine().trim());
                if (monto > 0) break;
                System.out.println("El monto debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un monto numérico válido.");
            }
        }

        LocalDate fecha = leerFecha(sc, "Fecha del gasto (AAAA-MM-DD, Enter para hoy): ", true);

        Gasto nuevoGasto = new Gasto(descripcion, monto, fecha, mantenimientoAsociado);
        gastos.add(nuevoGasto);

        if (mantenimientoAsociado != null) {
            mantenimientoAsociado.getGastos().add(nuevoGasto);
        }

        System.out.print("¿Registrar pago asociado ahora? (S/N): ");
        String abonar = sc.nextLine().trim();
        if (abonar.equalsIgnoreCase("S")) {
            String codPago = "PAG-" + (100 + pagos.size() + 1);
            Pago pagoGasto = new Pago(codPago, monto, LocalDate.now(), "Aprobado", null);
            pagoGasto.getGastos().add(nuevoGasto);
            nuevoGasto.setPago(pagoGasto);
            pagos.add(pagoGasto);
        }

        System.out.println("Gasto registrado correctamente: " + nuevoGasto);
        return nuevoGasto;
    }

    // 2. CONSULTAR GASTO (CRC: Consultar gasto / CU-07.3)
    public static void consultarGasto(Scanner sc, ArrayList<Gasto> gastos) {
        System.out.println("\n--- CONSULTAR GASTOS ---");
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos registrados.");
            return;
        }

        System.out.println("1 - Ver todos los gastos");
        System.out.println("2 - Filtrar por fecha exacta (AAAA-MM-DD)");
        System.out.println("3 - Filtrar por palabra clave en descripción");
        System.out.print("Opción: ");
        String op = sc.nextLine().trim();

        ArrayList<Gasto> filtrados = new ArrayList<>();
        if (op.equals("2")) {
            LocalDate fechaFiltro = leerFecha(sc, "Ingrese fecha (AAAA-MM-DD): ", false);
            for (Gasto g : gastos) {
                if (g.getFecha().equals(fechaFiltro)) {
                    filtrados.add(g);
                }
            }
        } else if (op.equals("3")) {
            System.out.print("Ingrese texto a buscar: ");
            String texto = sc.nextLine().trim().toLowerCase();
            for (Gasto g : gastos) {
                if (g.getDescripcion().toLowerCase().contains(texto)) {
                    filtrados.add(g);
                }
            }
        } else {
            filtrados.addAll(gastos);
        }

        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron gastos con el criterio seleccionado.");
            return;
        }

        double sumaTotal = 0;
        System.out.println("\nResultados:");
        for (int i = 0; i < filtrados.size(); i++) {
            Gasto g = filtrados.get(i);
            System.out.println((i + 1) + ". " + g);
            sumaTotal += g.getMonto();
        }
        System.out.println("Total acumulado: $" + sumaTotal);
    }

    // 3. ACTUALIZAR GASTO (CRC: Actualizar gasto)
    public void actualizarGasto(Scanner sc) {
        System.out.println("\n--- ACTUALIZAR GASTO ---");
        System.out.println("Actual: " + this);

        System.out.print("Nueva descripción (Enter para mantener '" + this.descripcion + "'): ");
        String nuevaDesc = sc.nextLine().trim();
        if (!nuevaDesc.isEmpty()) {
            this.descripcion = nuevaDesc;
        }

        System.out.print("Nuevo monto (Enter para mantener $" + this.monto + "): ");
        String montoStr = sc.nextLine().trim();
        if (!montoStr.isEmpty()) {
            try {
                double nuevoMonto = Double.parseDouble(montoStr);
                if (nuevoMonto > 0) {
                    this.monto = nuevoMonto;
                    if (this.pago != null) {
                        this.pago.actualizarPago(nuevoMonto, "Aprobado");
                    }
                } else {
                    System.out.println("Monto inválido; se conserva el monto anterior.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Formato numérico inválido; se conserva el monto anterior.");
            }
        }

        LocalDate nuevaFecha = leerFecha(sc, "Nueva fecha (AAAA-MM-DD, Enter para mantener " + this.fecha + "): ", true);
        if (nuevaFecha != null) {
            this.fecha = nuevaFecha;
        }

        System.out.println("Gasto actualizado: " + this);
    }

    private static LocalDate leerFecha(Scanner sc, String prompt, boolean permitirVacio) {
        while (true) {
            System.out.print(prompt);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty() && permitirVacio) {
                return prompt.contains("Fecha del gasto") ? LocalDate.now() : null;
            }
            try {
                return LocalDate.parse(entrada);
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use AAAA-MM-DD (ej: 2026-11-19).");
            }
        }
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Mantenimiento getMantenimiento() { return mantenimiento; }
    public void setMantenimiento(Mantenimiento mantenimiento) { this.mantenimiento = mantenimiento; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    @Override
    public String toString() {
        String refMant = (mantenimiento != null) ? "Mant: " + mantenimiento.getCodMant() : "General";
        String estadoPago = (pago != null) ? "Abonado (" + pago.getCodPago() + ")" : "Sin pago asociado";
        return "[" + fecha + "] " + descripcion + " - $" + monto + " (" + refMant + ") | " + estadoPago;
    }
}
