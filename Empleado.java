import java.util.ArrayList;

public class Empleado extends Persona {
    private int legajo;
    private String rol;

    public Empleado(String dni, String nombre, String apellido, String telefono, int legajo, String rol) {
        super(dni, nombre, apellido, telefono);
        this.legajo = legajo;
        this.rol = rol;
    }

    public int getLegajo() {
        return legajo;
    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Comportamientos de la tarjeta CRC EMPLEADO
    public void crearUsuario(ArrayList<Empleado> empleados, Empleado nuevoEmpleado) {
        if (nuevoEmpleado != null && buscarUsuario(empleados, nuevoEmpleado.getLegajo()) == null) {
            empleados.add(nuevoEmpleado);
            System.out.println("Usuario registrado: " + nuevoEmpleado.getNombre() + " (Legajo: " + nuevoEmpleado.getLegajo() + ")");
        } else {
            System.out.println("No se pudo crear el usuario: legajo duplicado o datos inválidos.");
        }
    }

    public Empleado buscarUsuario(ArrayList<Empleado> empleados, int legajoBuscado) {
        for (Empleado emp : empleados) {
            if (emp.getLegajo() == legajoBuscado) {
                return emp;
            }
        }
        return null;
    }

    public void reportar(String detalle) {
        System.out.println("Reporte emitido por " + getNombre() + " " + getApellido() + " (" + rol + "): " + detalle);
    }

    //5 EMPLEADOS
    public static ArrayList<Empleado> crearEmpleadosIniciales() {
        ArrayList<Empleado> empleados = new ArrayList<>();
        empleados.add(new Empleado("30111222", "Juan", "RE", "1122334455", 100, "Recepcionista"));
        empleados.add(new Empleado("31222333", "Carlos", "R", "1133445566", 200, "Mesero/Comando"));
        empleados.add(new Empleado("32333444", "Laura", "S", "1144556677", 300, "Encargado SPA"));
        empleados.add(new Empleado("33444555", "Marta", "L", "1155667788", 400, "Personal Limpieza"));
        empleados.add(new Empleado("28555666", "Juan", "G", "1166778899", 500, "Gerente"));
        return empleados;
    }

    @Override
    public String toString() {
        return super.toString() + " | Legajo: " + legajo + " | Rol: " + rol;
    }
}