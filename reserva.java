import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
public class reserva {
   //FORMATO AAAA/MM/DD
int id;
persona usuario;
habitacion habitacion;
LocalDate fechaEntrada;
LocalDate fechaSalida;


//CONSTRUCTOR
public  reserva(int id, persona usuario, habitacion habitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
    this.id = id;
    this.usuario = usuario;
    this.habitacion = habitacion;
    this.fechaEntrada = fechaEntrada;
    this.fechaSalida = fechaSalida;
}

//METODOS
public  void CrearReserva(persona usuario,ArrayList<reserva> reservas, habitacion[] habitaciones,
        Scanner sc) {
    System.out.println("CREAR RESERVA");
    //FALTA FUNCION PARA CREAR RESERVA
     // SOLICITAR FECHAS
    System.out.println("Ingrese la fecha de entrada (AAAA-MM-DD):");
    LocalDate fechaEntrada = LocalDate.parse(sc.nextLine());

    System.out.println("Ingrese la fecha de salida (AAAA-MM-DD):");
    LocalDate fechaSalida = LocalDate.parse(sc.nextLine());

    // VALIDAR FECHAS
    while (!fechaSalida.isAfter(fechaEntrada)) {
        System.out.println(
            "Error: la fecha de salida debe ser posterior a la entrada.");

        System.out.println("Ingrese la fecha de salida (AAAA-MM-DD):");
        fechaSalida = LocalDate.parse(sc.nextLine());
    }

}

public  void ActualizarReserva() {
    System.out.println("ACTUALIZAR RESERVA");
    
}

public  void EliminarReserva() {
    System.out.println("ELIMINAR RESERVA");
    //FALTA FUNCION PARA ELIMINAR RESERVA
}

    
   public  static void buscarReserva(Scanner sc) { 
    int op=0;
    do{
        System.out.println("MENU DE BUSCAR RESERVA");
        System.out.println("1 - Buscar por ID");
        System.out.println("2 - Buscar por nombre/DNI");
        System.out.println("0 - Volver al menu principal");
        op = gestionR.sc.nextInt();
        switch(op){
             case 1:
                 System.out.println("BUSCAR POR ID");
                 break;
             case 2:
                 System.out.println("BUSCAR POR NOMBRE/DNI");
                 break;
             case 0:
                 System.out.println("VOLVIENDO AL MENU PRINCIPAL");
                 break;
             default:
                 System.out.println("OPCION INVALIDA");
         }

    }while(op!=0);
}
}
