
import com.db4o.*;
import objetosPersistidos.Cliente;
import objetosPersistidos.Factura;
import queryInterfaces.AbstractQuery;
import queryInterfaces.NativeQuery;
import queryInterfaces.QueryByExample;
import queryInterfaces.SODA;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static ObjectContainer db;
    private static int opInterfaz;
    private static AbstractQuery query;
    private static int idUltCli;
    private static int nroUltFact;

    public static void main(String[] args) {

        System.out.println("\nCrea/Abre db TP2Ej1.yap\n");
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "D:/Users/Juaan/UNLu/Bases de Datos II/DBs TP2/TP2Ej1.yap");

        try {
            int op = -1;

            while (op != 0) {
                opInterfaz = menuInterfazConsulta();
                switch (opInterfaz) {
                    case 1:
                        query = new QueryByExample(db);
                        break;
                    case 2:
                        query = new NativeQuery(db);
                        break;
                    case 3:
                        query = new SODA(db);
                        break;
                    default:
                        op = 0;
                        break;
                }
                if (op != 0) {

                    idUltCli = query.verCantidadObj(new Cliente());
                    nroUltFact = query.verCantidadObj(new Factura());
                    menu();
                }
            }
        } finally {
            db.close();
        }
    }

    public static int menuInterfazConsulta(){
        clean();
        int op;

        System.out.println("\n------------------ Seleccionar Interfaz de Consultas ------------------\n");
        System.out.println("1 - Query By Example (QBE)");
        System.out.println("2 - Native Query (NQ)");
        System.out.println("3 - SODA");
        System.out.println("0 - Salir.");
        System.out.println("Ingrese opcion: ");

        op = sc.nextInt();
        sc.nextLine();

        return op;
    }

    public static void menu() {

        clean();
        int op = -1;

        while (op != 0) {

            System.out.println("\n------------------ Menu Principal ------------------\n");
            System.out.println("1 - Cargar Cliente");
            System.out.println("2 - Modificar Cliente");
            System.out.println("3 - Borrar Cliente");
            System.out.println("4 - Ver Clientes");
            System.out.println("5 - Cargar Factura");
            System.out.println("6 - Modificar Factura");
            System.out.println("7 - Eliminar Factura");
            System.out.println("8 - Ver Facturas");
            System.out.println("9 - Volver.\n");

            System.out.println("Ingrese opcion: ");

            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    clean();
                    System.out.println("\n------------------ Carga de Cliente ------------------\n");
                    System.out.println("Ingrese el nombre del cliente: ");
                    String nomCli = sc.nextLine();
                    Cliente cli = new Cliente(++idUltCli,nomCli);
                    query.cargar(cli);
                    break;
                case 2:
                    clean();
                    System.out.println("\n------------------ Modificar Cliente ------------------\n");
                    boolean cliExiste = false;
                    Cliente temp = new Cliente();
                    List<Cliente> lcli = List.of();
                    while (!cliExiste) {
                        System.out.println("Ingrese el ID del cliente: ");
                        int idCli = sc.nextInt();
                        sc.nextLine();
                        temp.setId(idCli);
                        lcli = query.buscar(temp);
                        if (lcli.isEmpty()){

                            System.err.println("\nID de cliente [" + idCli + "] no existe. Intente de nuevo.\n");
                        }
                        else {
                            cliExiste = true;
                        }

                    }
                    query.modificar(lcli.getFirst());
                    break;
                case 3:
                    clean();
                    query.borrarCliente();
                    break;
                case 4:
                    clean();
                    query.verClientes();
                    break;
                case 5:
                    clean();
                    query.cargarFactura();
                    break;
                case 6:
                    clean();
                    query.modificarFactura();
                    break;
                case 7:
                    clean();
                    query.borrarFactura();
                    break;
                case 8:
                    clean();
                    query.verFacturas();
                    break;
                default:
                    op = 0;
                    break;
            }
        }

    }

    public static void clean() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }


}