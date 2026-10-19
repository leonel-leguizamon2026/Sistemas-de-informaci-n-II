import java.util.Scanner;
 class gestionG {
 
    static Scanner sc = new Scanner(System.in);
    public static void gestionarGastos() {
    int op=0;
    //MENU DE GESTION DE GASTOS
        do{
        System.out.println("MENU DE GESTION DE GASTOS");
        System.out.println("1 - Chek-in");
        System.out.println("2 - Chek-out");
        System.out.println("0 - Volver al menu principal");
        op = sc.nextInt();
        switch(op){
             case 1:
                 System.out.println("CHEK-IN");
                 CheckIn();
                 break;
             case 2:
                 System.out.println("CHEK-OUT");
                 CheckOut();
                 break;
             case 0:
                 System.out.println("VOLVIENDO AL MENU PRINCIPAL");
                 break;
             default:
                 System.out.println("OPCION INVALIDA");
         }
        }while(op!=0);
    }

    public static void CheckIn() {
        //FALTA FUNCION PARA CHEK-IN
    }
    public static void CheckOut() {
        //FALTA FUNCION PARA CHEK-OUT
    }
}