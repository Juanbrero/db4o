package queryInterfaces;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;
import objetosPersistidos.Cliente;
import objetosPersistidos.Factura;

public class QueryByExample<T> extends AbstractQuery<T>{

    private static final Scanner sc = new Scanner(System.in);

    public QueryByExample (ObjectContainer db) {
        super();
        this.setDb(db);
    }


    @Override
    public void verClientes() {
        System.out.println("\n------------------ Lista de Clientes ------------------\n");
        Cliente proto = new Cliente(0,null);
        ObjectSet<Cliente> result = getDb().queryByExample(proto);

        for (Cliente c : result) {
            System.out.println("[" + c.getId() + "] - " + c.getDescr());
        }
    }

    @Override
    public void cargarFactura() {
        boolean cliExiste = false;
        int idCli;

        System.out.println("\n------------------ Carga de Factura ------------------\n");

        while (!cliExiste) {
            System.out.println("Ingrese el numero del cliente: ");
            idCli = sc.nextInt();

            // Busco si Cliente existe con QBE
            Cliente proto = new Cliente(idCli,null);
            ObjectSet<Cliente> result = getDb().queryByExample(proto);

            if (!result.isEmpty()) {

                // Busco el ultimo nro de factura
                Factura prof = new Factura(0,null);
                ObjectSet<Factura> factu = getDb().queryByExample(prof);
                int nroUltimaFact;
                if (factu.isEmpty()) {
                    nroUltimaFact = 1;
                }
                else {
                    nroUltimaFact = factu.getLast().getNro() + 1;
                }

                Factura f = new Factura(nroUltimaFact,result.getFirst());

                System.out.println("\nIngrese el Importe: $");
                f.setImporte(sc.nextDouble());
                getDb().store(f);

                System.out.println("\nFactura [" + f.getNro() + "] creada y guardada con exito.\n");

                cliExiste = true;
                getDb().commit();
            }
            else {
                System.err.println("\nID de cliente [" + idCli + "] no existe. Intente de nuevo.\n");
            }
        }
    }

    @Override
    public void modificarFactura() {
        boolean factExiste = false;
        int nroFact;
        System.out.println("\n------------------ Modificar Factura ------------------\n");

        while (!factExiste) {
            System.out.println("Ingrese el numero de factura: ");
            nroFact = sc.nextInt();
            sc.nextLine();
            // Busco si factura existe con QBE
            Factura proto = new Factura(nroFact,null);
            ObjectSet<Factura> result = getDb().queryByExample(proto);

            if (!result.isEmpty()) {
                Factura f = result.getFirst();

                System.out.println("\nIngrese el nuevo Importe: $");
                f.setImporte(sc.nextDouble());
                getDb().store(f);

                System.out.println("\nFactura [" + f.getNro() + "] actualizada con exito.\n");

                factExiste = true;
                getDb().commit();
            }
            else {
                System.err.println("\nNro de factura [" + nroFact + "] no existe. Intente de nuevo.\n");
            }
        }
    }

    @Override
    public void verFacturas() {
        System.out.println("\n------------------ Lista de Facturas ------------------\n");
        Factura proto = new Factura(0,null);
        ObjectSet<Factura> result = getDb().queryByExample(proto);

        for (Factura f : result) {
            Cliente protoCli = new Cliente(f.getId(),null);
            ObjectSet<Cliente> cli = getDb().queryByExample(protoCli);
            System.out.println("Factura [" + f.getNro() + "] [ (" + f.getId() + ") " + cli.getFirst().getDescr() + " ] --> $" + f.getImporte());

        }

    }

    @Override
    public List<T> buscar(T e) {
        ObjectSet<T> obj = getDb().queryByExample(e);
        return obj;
    }

    @Override
    public int verCantidadObj(Class<T> e) {

        return getDb().queryByExample(e).size();
    }
}
