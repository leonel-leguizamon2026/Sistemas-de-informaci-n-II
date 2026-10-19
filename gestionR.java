import java.util.ArrayList;
import java.util.Scanner;
public class gestionR {
      static Scanner sc = new Scanner(System.in);
    public static void gestionarReservas(  ArrayList<persona> personas,ArrayList<reserva> reservas,
    habitacion[] habitaciones) {
     int op=0;
     reserva reserva;
     //MENU DE GESTION DE RESERVAS
        do{
        System.out.println("MENU DE GESTION DE RESERVAS");
        System.out.println("1 - Crear reserva");
        System.out.println("2 - Actualizar reserva");
        System.out.println("3 - Eliminar reserva");
        System.out.println("0 - Volver al menu principal");
        op = sc.nextInt();
        switch(op){

             case 1: {
        System.out.println("CREAR RESERVA");
        System.out.println("1 - Crear usuario");
        System.out.println("2 - Buscar usuario");
        System.out.println("0 - Volver al menu anterior");

        op = sc.nextInt();
        sc.nextLine();
        persona usuario = null;

        switch (op) {
        case 1:
            System.out.println("CREAR USUARIO");

            usuario = new persona("", "", "", "");
            usuario.CrearUsuario(sc, personas);
            personas.add(usuario);
            break;

        case 2:
            System.out.println("BUSCAR USUARIO");
            usuario = persona.BuscarUsuario(sc, personas);
            break;

        case 0:
            System.out.println("VOLVIENDO AL MENU ANTERIOR");
            break;

        default:
            System.out.println("OPCION INVALIDA");
            break;
    }

    if (usuario != null) {
        System.out.println("Huesped seleccionado: "
                + usuario.nombre + " " + usuario.apellido);
            reserva= new reserva(0, usuario, null, null, null);
            reserva.CrearReserva(usuario, reservas, habitaciones, sc);
        // FALTA INVOCAR CREARRESERVA
    }

    break;
}
             case 2:
                 System.out.println("ACTUALIZAR RESERVA");
                  reserva = new reserva(0, null, null, null, null);
                 reserva.ActualizarReserva();
                 break;
             case 3:
                 System.out.println("ELIMINAR RESERVA");
                  reserva = new reserva(0, null, null, null, null);
                 reserva.EliminarReserva();
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