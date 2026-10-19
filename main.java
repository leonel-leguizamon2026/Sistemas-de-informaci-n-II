import java.util.Scanner;
import java.util.ArrayList;


//CREAR CLASE RESERVA?????
//INICIAR SESION


public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<persona> personas = new ArrayList<>();
        ArrayList<reserva> reservas = new ArrayList<>();
         int op=0;
         
        habitacion[] habitaciones = new habitacion[60];

int numero = 0;
//INICIALIZA LA HABITACIONES
// 12 habitaciones de 1 persona
for (int i = 0; i < 12; i++) {
    habitaciones[numero] = new habitacion(numero + 1, 1, "Libre");
    numero++;
}

// 14 habitaciones de 2 personas
for (int i = 0; i < 14; i++) {
    habitaciones[numero] = new habitacion(numero + 1, 2, "Libre");
    numero++;
}

// 15 habitaciones de 3 personas
for (int i = 0; i < 15; i++) {
    habitaciones[numero] = new habitacion(numero + 1, 3, "Libre");
    numero++;
}

// 12 habitaciones de 4 personas
for (int i = 0; i < 12; i++) {
    habitaciones[numero] = new habitacion(numero + 1, 4, "Libre");
    numero++;
}

// 5 habitaciones de 5 personas
for (int i = 0; i < 5; i++) {
    habitaciones[numero] = new habitacion(numero + 1, 5, "Libre");
    numero++;
}

// 2 habitaciones de 6 personas
for (int i = 0; i < 2; i++) {
    habitaciones[numero] = new habitacion(numero + 1, 6, "Libre");
    numero++;
}


//INICIO
do{
    System.out.println("INGRESE UNA OPCION: ");
    System.out.println("1-MESERO/ENCARGADO DE COMANDOS");
    System.out.println("2-ENCARGADO DE SPA/ERVICIO");
    System.out.println("3-PERSONAL DE LIMPIEZA");
    System.out.println("4-RECEPCIONISTA");
    System.out.println("5-GERENTE");
    System.out.println("0-SALIR");
    op = sc.nextInt();

    switch(op){
        //MESERO/ENCARGADO DE COMANDOS
        case 1:
            System.out.println("BIENVENIDO MESERO/ENCARGADO DE COMANDOS");
            break;
        //ENCARGADO DE SPA/SERVICIO
        case 2:
            System.out.println("BIENVENIDO ENCARGADO DE SPA/SERVICIO");
            break;
        //PERSONAL DE LIMPIEZA    
        case 3:
            System.out.println("BIENVENIDO PERSONAL DE LIMPIEZA");
            break;
            //RECEPCIONISTA
        case 4:
            System.out.println("BIENVENIDO RECEPCIONISTA");

          //MENU PRINCIPAL  RECEPCIONISTA
         do{
        System.out.println("INGRESE UNA OPCION: ");
        System.out.println("1-GESTIONAR RESERVAS");
         System.out.println("2-GESTION DE GASTOS");
          System.out.println("0-SALIR");
          op = sc.nextInt();

         switch(op){
             case 1:
                    gestionR.gestionarReservas(personas, reservas, habitaciones);
                 break;
             case 2:
                    gestionG.gestionarGastos();
                 break;
             case 0:
                 System.out.println("SALIENDO");
                 break;
             default:
                 System.out.println("OPCION INVALIDA");
         }

        }while(op!=0);
        break;
        //GERENTE


        case 5:
            System.out.println("BIENVENIDO GERENTE");
            break;
        case 0:
            System.out.println("SALIENDO");
            break;
        default:
            System.out.println("OPCION INVALIDA");
    }









       
    }while(op!=0);











    }
}