import java.time.LocalDate;
import java.util.ArrayList;

public class Pago {
    private String codPago;
    private double montoTotal;
    private LocalDate fecha;
    private String estado; // "Pendiente", "Aprobado", "Rechazado", "Anulado"

    // Colaboradores según CRC y Modelo de Dominio
    private Huesped huesped;
    private ArrayList<Pedido> pedidos;   // 0..*
    private ArrayList<Reserva> reservas; // 1..*
    private ArrayList<Gasto> gastos;     // 1..*
    private ArrayList<Turno> turnos;     // 0..*

    public Pago(String codPago, double montoTotal, LocalDate fecha, String estado, Huesped huesped) {
        this.codPago = codPago;
        this.montoTotal = montoTotal;
        this.fecha = fecha;
        this.estado = estado;
        this.huesped = huesped;
        this.pedidos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.gastos = new ArrayList<>();
        this.turnos = new ArrayList<>();
    }

    // 1. REGISTRAR PAGO (Soporta pago consolidado en Check-Out CU-05.2)
    public static Pago registrarPagoConsolidado(ArrayList<Pago> pagos, Huesped huesped, Reserva reserva,
                                                ArrayList<Pedido> pedidosPendientes, ArrayList<Turno> turnosPendientes,
                                                double costoEstadia) {
        double totalPedidos = 0;
        for (Pedido p : pedidosPendientes) {
            totalPedidos += p.getPrecio();
        }
        double totalTurnos = 0;
        for (Turno t : turnosPendientes) {
            // Valor base de turno si no se almacenó aparte
            totalTurnos += 5000;
        }

        double totalGeneral = costoEstadia + totalPedidos + totalTurnos;
        String codPago = "PAG-" + (100 + pagos.size() + 1);
        Pago nuevoPago = new Pago(codPago, totalGeneral, LocalDate.now(), "Aprobado", huesped);

        if (reserva != null) {
            nuevoPago.getReservas().add(reserva);
            reserva.setPago(nuevoPago);
        }
        for (Pedido p : pedidosPendientes) {
            nuevoPago.getPedidos().add(p);
            p.setPago(nuevoPago);
        }
        for (Turno t : turnosPendientes) {
            nuevoPago.getTurnos().add(t);
            t.setPago(nuevoPago);
        }

        pagos.add(nuevoPago);
        if (huesped != null) {
            huesped.getPagos().add(nuevoPago);
        }

        System.out.println("Pago consolidado registrado: " + nuevoPago);
        return nuevoPago;
    }

    // 2. VERIFICAR PAGO (CU-05.4)
    public boolean verificarPago() {
        System.out.println("Verificando pago [" + codPago + "] - Monto: $" + montoTotal + " - Estado actual: " + estado);
        return this.estado.equalsIgnoreCase("Aprobado");
    }

    // 3. ACTUALIZAR PAGO
    public void actualizarPago(double nuevoMonto, String nuevoEstado) {
        this.montoTotal = nuevoMonto;
        this.estado = nuevoEstado;
        this.fecha = LocalDate.now();
        System.out.println("Pago [" + codPago + "] actualizado -> Monto: $" + montoTotal + " | Estado: " + estado);
    }

    public String getCodPago() { return codPago; }
    public double getMontoTotal() { return montoTotal; }
    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public Huesped getHuesped() { return huesped; }
    public ArrayList<Pedido> getPedidos() { return pedidos; }
    public ArrayList<Reserva> getReservas() { return reservas; }
    public ArrayList<Gasto> getGastos() { return gastos; }
    public ArrayList<Turno> getTurnos() { return turnos; }

    @Override
    public String toString() {
        String titular = (huesped != null) ? huesped.getNombre() + " " + huesped.getApellido() : "Operación Interna";
        return "[" + codPago + "] Titular: " + titular + " | Total: $" + montoTotal + " | Fecha: " + fecha + " | Estado: " + estado;
    }
}