package datamer.model.boes.enty;

import datamer.Var;
import tools.Util;

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
                + Util.comillas(this.nombre) + ","
                + Util.comillas(this.codigo)
                + ");";
    }

    public String SQLEditar() {
        String query = "";
        return query;
    }

    public String SQLBuscar() {
        return "SELECT * from " + Var.dbNameBoes + ".entidad where nombre=" + Util.comillas(this.nombre);
    }

}
