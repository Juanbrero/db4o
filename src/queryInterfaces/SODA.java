package queryInterfaces;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import com.db4o.query.Query;

import java.util.List;

public class SODA<T> extends AbstractQuery<T>{

    public SODA(ObjectContainer db) {
        super();
        setDb(db);
    }

    @Override
    public void ver(Class<T> e) {

        Query query = getDb().query();
        query.constrain(e);

        ObjectSet<T> result = query.execute();

        for (T ee : result) {
            System.out.println(ee.toString());
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
    public int verCantidadObj(Class<T> e) {
        Query query = getDb().query();
        query.constrain(e);
        ObjectSet<T> obj = query.execute();

        return obj.size();
    }
}
