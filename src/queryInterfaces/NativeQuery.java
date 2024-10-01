package queryInterfaces;

import com.db4o.ObjectContainer;

import com.db4o.query.Predicate;

import java.util.List;

public class NativeQuery<T> extends AbstractQuery<T>{

    public NativeQuery(ObjectContainer db) {
        super();
        setDb(db);
    }

    @Override
    public void ver(Class<T> e) {

        List<T> le = getDb().query(new Predicate<T>() {
            @Override
            public boolean match(T ee) {
                return ee.getClass().equals(e);
            }
        });

        for (T ee : le) {
            System.out.println(ee.toString());
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
