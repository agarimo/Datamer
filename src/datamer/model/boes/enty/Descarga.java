package datamer.model.boes.enty;

import datamer.Var;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Descarga {

    private int id;
    private String codigo;
    private String link;
    private String datos;

    public Descarga() {
    }

    public Descarga(String codigo, String link) {
        this.codigo = codigo;
        this.link = link;
        this.datos = "null";
    }

    public Descarga(int id, String codigo, String link, String datos) {
        this.id = id;
        this.codigo = codigo;
        this.link = link;
        this.datos = datos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".descarga (codigo,link,datos) values("
                + Varios.comillas(this.codigo) + ","
                + Varios.comillas(this.link) + ","
                + Varios.comillas(this.datos)
                + ");";
    }

    public String SQLSetDatos() {
        return "UPDATE " + Var.dbNameBoes + ".descarga SET "
                + "datos=" + Varios.comillas(this.datos) + " "
                + "WHERE id=" + this.id;
    }

    public String SQLBuscar() {
        return "SELECT * from " + Var.dbNameBoes + ".descarga where codigo=" + Varios.comillas(this.codigo);
    }
}
