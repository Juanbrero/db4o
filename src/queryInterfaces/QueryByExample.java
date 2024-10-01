package queryInterfaces;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.List;


public class QueryByExample<T> extends AbstractQuery<T>{

    public QueryByExample (ObjectContainer db) {
        super();
        this.setDb(db);
    }


    @Override
    public void ver(Class<T> e) {

        ObjectSet<T> result = getDb().queryByExample(e);

        for (T ee : result) {
            System.out.println(ee.toString());
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
