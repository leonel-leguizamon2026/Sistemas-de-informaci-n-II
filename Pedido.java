import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Pedido {
    private String codPedido;
    private double precio;
    private String estado; // "Activo", "Cancelado"

    // Colaboradores según CRC y Modelo de Dominio
    private Huesped huesped;
    private Empleado empleado;
    private Pago pago; // Multiplicidad 0..1 (null si se carga a la habitación)

    public Pedido(String codPedido, double precio, Huesped huesped, Empleado empleado) {
        this.codPedido = codPedido;
        this.precio = precio;
        this.estado = "Activo";
        this.huesped = huesped;
        this.empleado = empleado;
        this.pago = null;
    }

    // 1. TOMAR PEDIDO (CU-01.1 + CU-09 Forma de pago + CU-10 Buscar Huésped/Cliente)
    public static Pedido tomarPedido(Scanner sc, ArrayList<Pedido> pedidos, ArrayList<Huesped> huespedes,
                                     ArrayList<Reserva> reservas, ArrayList<Pago> pagos, Empleado empleado) {
        System.out.println("\n--- TOMAR PEDIDO ---");
        System.out.print("Ingrese DNI del huésped/cliente: ");
        String dni = sc.nextLine().trim();

        Huesped huesped = null;
        for (Huesped h : huespedes) {
            if (h.getDni().equalsIgnoreCase(dni)) {
                huesped = h;
                break;
            }
        }
        if (huesped == null) {
            System.out.println("Huésped no encontrado. Debe estar registrado en el sistema.");
            return null;
        }

        System.out.print("Ingrese el precio total de la comanda: $");
        double precio = sc.nextDouble();
        sc.nextLine();

        String codPed = "PED-" + (100 + pedidos.size() + 1);
        Pedido nuevoPedido = new Pedido(codPed, precio, huesped, empleado);

        System.out.print("¿Cargar a habitación / pagar al Check-Out? (S/N): ");
        String cargarHab = sc.nextLine().trim();

        if (cargarHab.equalsIgnoreCase("S")) {
            // Validar estadía activa (Check-In)
            boolean estadiaActiva = false;
            for (Reserva r : reservas) {
                if (r.getHuesped().getDni().equalsIgnoreCase(huesped.getDni())
                        && r.getEstado().equalsIgnoreCase("Check-In")) {
                    estadiaActiva = true;
                    break;
                }
            }

            if (!estadiaActiva) {
                System.out.println("El DNI ingresado no corresponde a un huésped con estadía activa (ya se retiró o no tiene Check-In).");
                System.out.println("Se requiere pago inmediato para registrar el pedido.");
                System.out.print("¿Abona de contado/inmediato ahora? (S/N): ");
                if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                    System.out.println("Pedido no registrado (sin cobro confirmado; no impacta en ganancias).");
                    return null;
                }
                String codPago = "PAG-" + (100 + pagos.size() + 1);
                Pago pagoInmediato = new Pago(codPago, precio, LocalDate.now(), "Aprobado", huesped);
                pagoInmediato.getPedidos().add(nuevoPedido);
                nuevoPedido.setPago(pagoInmediato);
                huesped.getPagos().add(pagoInmediato);
                pagos.add(pagoInmediato);
            } else {
                System.out.println("Consumo cargado a la cuenta de la habitación para el Check-Out.");
            }
        } else {
            System.out.print("¿Confirma cobro inmediato? (S/N): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                System.out.println("Pago no confirmado; pedido no registrado.");
                return null;
            }
            String codPago = "PAG-" + (100 + pagos.size() + 1);
            Pago pagoInmediato = new Pago(codPago, precio, LocalDate.now(), "Aprobado", huesped);
            pagoInmediato.getPedidos().add(nuevoPedido);
            nuevoPedido.setPago(pagoInmediato);
            huesped.getPagos().add(pagoInmediato);
            pagos.add(pagoInmediato);
        }

        pedidos.add(nuevoPedido);
        huesped.getPedidos().add(nuevoPedido);
        System.out.println("Pedido registrado: " + nuevoPedido);
        return nuevoPedido;
    }

    // 2. MODIFICAR PEDIDO (CU-01.2)
    public void modificarPedido(Scanner sc) {
        if (!this.estado.equalsIgnoreCase("Activo")) {
            System.out.println("No se puede modificar un pedido cancelado.");
            return;
        }
        System.out.print("Ingrese el nuevo precio total del pedido (anterior $" + this.precio + "): $");
        double nuevoPrecio = sc.nextDouble();
        sc.nextLine();

        if (nuevoPrecio <= 0) {
            System.out.println("Error: El importe debe ser mayor a 0.");
            return;
        }

        if (this.pago != null && nuevoPrecio > this.precio) {
            double diferencia = nuevoPrecio - this.precio;
            System.out.println("Diferencia a abonar: $" + diferencia + ". Actualizando pago asociado...");
            this.pago.actualizarPago(nuevoPrecio, "Aprobado");
        }

        this.precio = nuevoPrecio;
        System.out.println("Pedido actualizado: " + this);
    }

    // 3. CANCELAR PEDIDO (CU-01.3)
    public void cancelarPedido() {
        this.estado = "Cancelado";
        if (this.pago != null) {
            System.out.println("Gestionando anulación/devolución del pago " + this.pago.getCodPago() + " por $" + this.precio);
            this.pago.actualizarPago(0, "Anulado");
            this.pago = null;
        }
        System.out.println("Pedido " + this.codPedido + " cancelado.");
    }

    public String getCodPedido() { return codPedido; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getEstado() { return estado; }
    public Huesped getHuesped() { return huesped; }
    public Empleado getEmpleado() { return empleado; }
    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    @Override
    public String toString() {
        String estadoPago = (pago != null) ? "Pagado (" + pago.getCodPago() + ")" : "Pendiente (Cargo Hab.)";
        return "[" + codPedido + "] Huésped: " + huesped.getNombre() + " | Precio: $" + precio + " | " + estadoPago + " | Estado: " + estado;
    }
}