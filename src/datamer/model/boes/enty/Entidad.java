package datamer.model.boes.enty;

import datamer.Var;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Entidad {

    private int id;
    private String nombre;
    private String codigo;

    public Entidad() {

    }

    public Entidad(int id, String nombre, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".entidad (nombre,codigo) values("
                + Varios.entrecomillar(this.nombre) + ","
                + Varios.entrecomillar(this.codigo)
                + ");";
    }

    public String SQLEditar() {
        String query = "";
        return query;
    }

    public String SQLBuscar() {
        return "SELECT * from " + Var.dbNameBoes + ".entidad where nombre=" + Varios.entrecomillar(this.nombre);
    }

}
