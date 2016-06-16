package datamer.model.boes.enty;

import datamer.Var;
import tools.Util;

/**
 *
 * @author Agárimo
 */
public class Tipo {

    private String id;
    private String nombre;
    
    public Tipo(){
        
    }

    public Tipo(String idTipo) {
        this.id = idTipo;
    }

    public Tipo(String idTipo, String nombre) {
        this.id = idTipo;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return this.id;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".tipo (id, nombre) values("
                + Util.comillas(this.id) + ","
                + Util.comillas(this.nombre)
                + ")";
    }

    public String SQLEditar() {
        return "UPDATE " + Var.dbNameBoes + ".tipo SET "
                + "nombre=" + Util.comillas(this.nombre)
                + "WHERE id=" + Util.comillas(this.id);
    }

    public String SQLBorrar() {
        return "DELETE FROM " + Var.dbNameBoes + ".tipo WHERE id=" + Util.comillas(this.id);
    }

    public String SQLBuscar() {
        return "SELECT * FROM " + Var.dbNameBoes + ".tipo WHERE id=" + Util.comillas(this.id);
    }
}
