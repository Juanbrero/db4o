
import com.db4o.*;
import objetosPersistidos.Cliente;
import objetosPersistidos.Factura;
import queryInterfaces.AbstractQuery;
import queryInterfaces.NativeQuery;
import queryInterfaces.QueryByExample;
import queryInterfaces.SODA;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static ObjectContainer db;
    private static int opInterfaz;
    private static AbstractQuery query;
    private static int idUltCli;
    private static int nroUltFact;

    public static void main(String[] args) {

        System.out.println("\nCrea/Abre db\n");

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dbPath = props.getProperty("db.path");
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbPath);

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

                    idUltCli = query.verCantidadObj(Cliente.class);
                    nroUltFact = query.verCantidadObj(Factura.class);

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
                    query.cargar(new Cliente(++idUltCli,nomCli));
                    break;
                case 2:
                    clean();
                    System.out.println("\n------------------ Modificar Cliente ------------------\n");
                    Cliente c = inputCliente();
                    System.out.println("\nIngrese nuevo nombre: ");
                    c.setDescr(sc.nextLine());
                    query.modificar(c);
                    System.out.println("\nCliente modificado con exito.\n");
                    break;
                case 3:
                    clean();
                    System.out.println("\n------------------ Borrar Cliente ------------------\n");
                    Cliente temp = inputCliente();
                    List<Factura> lf = query.buscar(new Factura(0,temp));
                    for (Factura f : lf) {
                        query.borrar(f);
                    }
                    query.borrar(temp);
                    break;
                case 4:
                    clean();
                    System.out.println("\n------------------ Lista de Clientes ------------------\n");
                    query.ver(Cliente.class);
                    break;
                case 5:
                    clean();
                    System.out.println("\n------------------ Carga de Factura ------------------\n");
                    temp = inputCliente();
                    Factura f = new Factura(++nroUltFact,temp);
                    System.out.println("\nIngrese el Importe: $");
                    f.setImporte(sc.nextDouble());
                    query.cargar(f);
                    break;
                case 6:
                    clean();
                    System.out.println("\n------------------ Modificar Factura ------------------\n");
                    f = inputFactura();
                    System.out.println("\nIngrese el nuevo Importe: $");
                    f.setImporte(sc.nextDouble());
                    query.modificar(f);
                    System.out.println("\nFactura modificada con exito.\n");
                    break;
                case 7:
                    clean();
                    System.out.println("\n------------------ Borrar Factura ------------------\n");
                    query.borrar(inputFactura());
                    break;
                case 8:
                    clean();
                    System.out.println("\n------------------ Lista de Facturas ------------------\n");
                    query.ver(Factura.class);
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

    public static Cliente inputCliente() {
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
        return lcli.getFirst();
    }

    public static Factura inputFactura() {
        boolean factExiste = false;
        int nroFact;
        List<Factura> lfact = List.of();
        Factura temp = new Factura();
        while (!factExiste) {
            System.out.println("Ingrese el numero de factura: ");
            nroFact = sc.nextInt();
            sc.nextLine();
            temp.setNro(nroFact);
            lfact = query.buscar(temp);

            if (lfact.isEmpty()) {

                System.err.println("\nNro de factura [" + nroFact + "] no existe. Intente de nuevo.\n");
            }
            else {
                factExiste = true;
            }
        }
        return lfact.getFirst();
    }

}