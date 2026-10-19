import java.util.Scanner;
import java.util.ArrayList;
public class persona {
    //ATRIBUTOS
    String dni;
    String nombre;
    String apellido;
    String telefono;
     //CONSTRUCTOR
    public persona(String dni, String nombre, String apellido, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }
    //METODOS


    //CARGAR DATOS
    public void CrearUsuario(Scanner sc, ArrayList<persona> personas){
        System.out.println("Ingrese el NOMBRE del usuario:");
        this.nombre = sc.nextLine();
        System.out.println("Ingrese el APELLIDO del usuario:");
        this.apellido = sc.nextLine();

        //FALTA FUNCION PARA VERIFICAR QUE EL NUMERO SEA VALIDO
          do {
        System.out.println("Ingrese el DNI del usuario:");
        this.dni = sc.nextLine();
        if (!this.dni.matches("[0-9]{8}")) {
            System.out.println("Error. El DNI debe contener exactamente 8 números.");
        } else if (existeDNI(personas, this.dni)) {
            System.out.println("Error. Ya existe un usuario con ese DNI.");
        } else {
            break;
        } } while (true);
        //FALTA FUNCION PARA VERIFICAR SI EL DNI YA ESTA


        System.out.println("Ingrese el TELEFONO del usuario:");
        this.telefono = sc.nextLine();
        while (!this.telefono.matches("[0-9]{12}")) {
    System.out.println("Error. El teléfono debe tener exactamente 12 números.");
    System.out.println("Ejemplo: 542657217613");
    this.telefono = sc.nextLine();
        }


        System.out.println("DATOS CARGADOS CORRECTAMENTE");
      }


    //BUSCAR USUARIO
   public static persona BuscarUsuario(Scanner sc, ArrayList<persona> personas) {
    System.out.println("Ingrese el DNI del usuario a buscar:");
    String dniBuscar = sc.nextLine();
    for (persona p : personas) {
        if (p.dni.equals(dniBuscar)) {
            System.out.println("Usuario encontrado: "+ p.nombre + " " + p.apellido);
            return p;
        }}
    System.out.println("Usuario no encontrado.");
    return null;
}  
    

    //EXTRASSS!!!!
    // COMPROBAR SI EL DNI YA EXISTE
public boolean existeDNI(ArrayList<persona> personas, String dni) {

    for (persona p : personas) {
        if (p.dni.equals(dni)) {
            return true;
        }
    } return false;
}

}