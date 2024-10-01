package objetosPersistidos;

import java.util.Objects;

public class Factura {

    private int nro;
    private Cliente id;
    private double importe;

    public Factura(){}

    public Factura(int nro, Cliente idCli){
        this.nro = nro;
        this.id = idCli;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public int getId() {
        return this.id.getId();
    }

    public void setId(Cliente id) {
        this.id = id;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    @Override
    public String toString() {
        return "Factura [" + getNro() + "] [ (" + getId() + ") " + id.getDescr() + " ] --> $" + getImporte();
    }

    @Override
    public boolean equals(Object o) {
        Factura f = (Factura) o;

        return (this.getId() == f.getId());

    }

}
