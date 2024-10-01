package queryInterfaces;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import objetosPersistidos.Cliente;
import objetosPersistidos.Factura;

import java.util.List;
import java.util.Scanner;

public class NativeQuery<T> extends AbstractQuery<T>{

    private static final Scanner sc = new Scanner(System.in);

    public NativeQuery(ObjectContainer db) {
        super();
        setDb(db);
    }


    @Override
    public void verClientes() {
        System.out.println("\n------------------ Lista de Clientes ------------------\n");

        List<Cliente> cli = getDb().query(new Predicate<Cliente>() {
            @Override
            public boolean match(Cliente cliente) {
                return true;
            }
        });

        for (Cliente c : cli) {
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

            int finalIdCli = idCli;
            List<Cliente> result = getDb().query(new Predicate<Cliente>() {
                @Override
                public boolean match(Cliente cli) {
                    return cli.getId() == finalIdCli;
                }
            });

            if (!result.isEmpty()) {

                List<Factura> lf = getDb().query(new Predicate<Factura>() {
                    @Override
                    public boolean match(Factura f) {
                        return true;
                    }
                });

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
            int finalNroFact = nroFact;
            List<Factura> lf = getDb().query(new Predicate<Factura>() {
                @Override
                public boolean match(Factura f) {
                    return f.getNro() == finalNroFact;
                }
            });

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
    public void verFacturas() {
        System.out.println("\n------------------ Lista de Facturas ------------------\n");
        List<Factura> lf = getDb().query(new Predicate<Factura>() {
            @Override
            public boolean match(Factura f) {
                return true;
            }
        });

        for (Factura f : lf) {
            List<Cliente> result = getDb().query(new Predicate<Cliente>() {
                @Override
                public boolean match(Cliente cli) {
                    return cli.getId() == f.getId();
                }
            });
            System.out.println("Factura [" + f.getNro() + "] [ (" + f.getId() + ") " + result.getFirst().getDescr() + " ] --> $" + f.getImporte());
        }
    }

    @Override
    public List<T> buscar(T e) {

        List<T> le = getDb().query(new Predicate<T>() {
            @Override
            public boolean match(T ee) {
                return ee.equals(e);
            }
        });

        return le;

    }

    @Override
    public int verCantidadObj(Class<T> e) {

        List<T> le = getDb().query(new Predicate<T>() {
            @Override
            public boolean match(T ee) {
                return ee.getClass().equals(e);
            }
        });

        return le.size();
    }
}
