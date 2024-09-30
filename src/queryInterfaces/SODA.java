package queryInterfaces;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import objetosPersistidos.Cliente;
import objetosPersistidos.Factura;


import java.util.List;
import java.util.Scanner;

public class SODA<T> extends AbstractQuery<T>{

    private static final Scanner sc = new Scanner(System.in);

    public SODA(ObjectContainer db) {
        super();
        setDb(db);
    }


    @Override
    public void borrarCliente() {

        Query query = getDb().query();
        int idCli;
        boolean cliExiste = false;

        System.out.println("\n------------------ Borrar Cliente ------------------\n");

        while (!cliExiste) {

            System.out.println("Ingrese el ID del cliente: ");
            idCli = sc.nextInt();
            query.constrain(Cliente.class);
            query.descend("id").constrain(idCli);
            ObjectSet<Cliente> result = query.execute();

            if(!result.isEmpty()) {
                //primero borrar facturas asociadas
                Cliente c = result.getFirst();

                Query query2 = getDb().query();
                query2.constrain(Factura.class);
                query2.descend("id").constrain(c);
                ObjectSet<Factura> lf = query2.execute();

                for(Factura fact : lf) {
                    getDb().delete(fact);
                }

                getDb().delete(c);
                System.out.println("\nCliente borrado con exito.\n");
                cliExiste = true;
                getDb().commit();
            }
            else {
                System.err.println("\nID de cliente [" + idCli + "] no existe. Intente de nuevo.\n");
            }
        }

    }

    @Override
    public void verClientes() {
        System.out.println("\n------------------ Lista de Clientes ------------------\n");

        Query query = getDb().query();
        query.constrain(Cliente.class);

        ObjectSet<Cliente> result = query.execute();

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
            Query query = getDb().query();
            query.constrain(Cliente.class);
            query.descend("id").constrain(idCli);
            ObjectSet<Cliente> result = query.execute();

            if (!result.isEmpty()) {
                Query query2 = getDb().query();
                query2.constrain(Factura.class);
                ObjectSet<Factura> lf = query2.execute();

                int nroUltimaFact;
                if (lf.isEmpty()) {
                    nroUltimaFact = 1;
                }
                else {
                    nroUltimaFact = lf.getLast().getNro() + 1;
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
            // Busco si factura existe
            Query query = getDb().query();
            query.constrain(Factura.class);
            query.descend("nro").constrain(nroFact);
            ObjectSet<Factura> lf = query.execute();

            if (!lf.isEmpty()) {
                Factura f = lf.getFirst();

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
    public void borrarFactura() {
        boolean factExiste = false;
        int nroFact;
        System.out.println("\n------------------ Borrar Factura ------------------\n");

        while (!factExiste) {
            System.out.println("Ingrese el numero de factura: ");
            nroFact = sc.nextInt();

            // Busco si factura existe
            Query query = getDb().query();
            query.constrain(Factura.class);
            query.descend("nro").constrain(nroFact);
            ObjectSet<Factura> lf = query.execute();

            if (!lf.isEmpty()) {
                Factura f = lf.getFirst();
                getDb().delete(f);

                System.out.println("\nFactura [" + f.getNro() + "] borrada con exito.\n");

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
        Query query = getDb().query();
        query.constrain(Factura.class);

        ObjectSet<Factura> lf = query.execute();

        for (Factura f : lf) {
            Query query2 = getDb().query();
            query2.constrain(Cliente.class);
            query2.descend("id").constrain(f.getId());
            ObjectSet<Cliente> result = query2.execute();
            System.out.println("Factura [" + f.getNro() + "] [ (" + f.getId() + ") " + result.getFirst().getDescr() + " ] --> $" + f.getImporte());
        }
    }

    @Override
    public List<T> buscar(T e) {

        Query query = getDb().query();
        query.constrain(e.getClass());
        ObjectSet<T> obj = query.execute();

        List<T> le = List.of();
        for (T o : obj) {
            if (o.equals(e)) {

                le.add(o);
            }
        }

        return le;
    }

    @Override
    public int verCantidadObj(T e) {
        Query query = getDb().query();
        query.constrain(e.getClass());
        ObjectSet<T> obj = query.execute();

        return obj.size();
    }
}
