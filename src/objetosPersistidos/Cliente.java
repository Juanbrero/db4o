package objetosPersistidos;

import java.util.Objects;

public class Cliente {
    private int id;
    private String descr;

    public Cliente() {

    }

    public Cliente(int id, String descr) {
        this.id = id;
        this.descr = descr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public boolean equals(Object o) {
        Cliente c = (Cliente) o;

        return this.getId() == c.getId();
    }

}
