import java.util.ArrayList;

public class Huesped extends Persona {
    private String codHuesped;
    private ArrayList<Reserva> reservas;
    private ArrayList<Pedido> pedidos;
    private ArrayList<Turno> turnos;
    private ArrayList<Pago> pagos;

    public Huesped(String dni, String nombre, String apellido, String telefono, String codHuesped) {
        super(dni, nombre, apellido, telefono);
        this.codHuesped = codHuesped;
        this.reservas = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.turnos = new ArrayList<>();
        this.pagos = new ArrayList<>();
    }

    public static Huesped crearHuesped(ArrayList<Huesped> huespedes, String dni, String nombre,
                                       String apellido, String telefono, String codHuesped) {
        if (huespedes != null) {
            for (Huesped h : huespedes) {
                if (h.getDni() != null && h.getDni().equalsIgnoreCase(dni)) {
                    return h;
                }
            }
        }
        Huesped nuevo = new Huesped(dni, nombre, apellido, telefono, codHuesped);
        if (huespedes != null) {
            huespedes.add(nuevo);
        }
        return nuevo;
    }

    public String getCodHuesped() {
        return codHuesped;
    }

    public void setCodHuesped(String codHuesped) {
        this.codHuesped = codHuesped;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public ArrayList<Turno> getTurnos() {
        return turnos;
    }

    public ArrayList<Pago> getPagos() {
        return pagos;
    }

    @Override
    public String toString() {
        return "[" + codHuesped + "] " + getNombre() + " " + getApellido()
                + " (DNI: " + getDni() + " | Tel: " + getTelefono() + ")";
    }
}