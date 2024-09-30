package queryInterfaces;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import objetosPersistidos.Cliente;

import java.util.List;
import java.util.Scanner;

public abstract class AbstractQuery<T> {

    private ObjectContainer db;

    public AbstractQuery() {

    }

    protected ObjectContainer getDb() {
        return db;
    }

    protected void setDb(ObjectContainer db) {
        this.db = db;
    }

    public void cargar(T e) {
        if (buscar(e).isEmpty()) {
            getDb().store(e);
            System.out.println("\nObjeto " + e.getClass().getName() + " creado y guardado con exito.\n");
            getDb().commit();
        }
        else {
            System.out.println("\nObjeto " + e.getClass().getName() + " ya existe.\n");
        }
    }

    public void modificar(Cliente c) {

        Scanner sc = new Scanner(System.in);
        System.out.println("\nIngrese nuevo nombre: ");
        c.setDescr(sc.nextLine());
        getDb().store(c);
        System.out.println("\nCliente modificado con exito.\n");

        getDb().commit();

    }

    public abstract void borrarCliente();

    public abstract void verClientes();

    public abstract void cargarFactura();

    public abstract void modificarFactura();

    public abstract void borrarFactura();

    public abstract void verFacturas();

    public abstract List<T> buscar(T e);

    public abstract int verCantidadObj(T e);

}
