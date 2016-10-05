package datamer.model;

import datamer.Var;
import tools.Util;

/**
 *
 * @author Agárimo
 */
public class Nota {

    private int id;
    private String datos;

    public Nota(int id) {
        this.id = id;
    }

    public Nota(int id, String datos) {
        this.id = id;
        this.datos = datos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".notas (id,datos) values("
                + this.id + ","
                + Util.comillas(this.datos) + ") "
                + "ON DUPLICATE KEY "
                + "UPDATE "
                + "datos=" + Util.comillas(this.datos) + ";";
    }

    public String SQLEliminar() {
        return "DELETE FROM " + Var.dbNameBoes + ".notas where id=" + this.id;
    }
}
