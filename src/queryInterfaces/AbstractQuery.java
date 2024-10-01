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
            System.out.println("\nObjeto " + e.getClass().getSimpleName() + " creado y guardado con exito.\n");
            getDb().commit();
        }
        else {
            System.out.println("\nObjeto " + e.getClass().getSimpleName() + " ya existe.\n");
        }
    }

    public void modificar(T e) {

        if (e != null) {

            getDb().store(e);
            getDb().commit();
        }

    }

    public void borrar(T e) {

        if (e != null) {

            getDb().delete(e);
            getDb().commit();
        }

    }

    public abstract void ver(Class<T> e);

    public abstract List<T> buscar(T e);

    public abstract int verCantidadObj(Class<T> e);

}
