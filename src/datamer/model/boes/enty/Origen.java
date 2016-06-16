package datamer.model.boes.enty;

import datamer.Var;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class Origen {

    private int id;
    private int idEntidad;
    private String nombre;
    private String codigo;
    private String codigoAy;
    private String codigoUn;
    private String codigoTes;
    private String nombreMostrar;

    public Origen() {
    }

    public Origen(String nombre) {
        this.nombre = nombre;
    }

    public Origen(int id, int idEntidad, String nombre, String codigo, String codigoAy, String codigoUn, String codigoTes, String nombreMostrar) {
        this.id = id;
        this.idEntidad = idEntidad;
        this.nombre = nombre;
        this.codigo = codigo;
        this.codigoAy = codigoAy;
        this.codigoUn = codigoUn;
        this.codigoTes = codigoTes;
        this.nombreMostrar = nombreMostrar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(int idEntidad) {
        this.idEntidad = idEntidad;
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

    public String getCodigoAy() {
        return codigoAy;
    }

    public void setCodigoAy(String codigoAy) {
        this.codigoAy = codigoAy;
    }

    public String getCodigoUn() {
        return codigoUn;
    }

    public void setCodigoUn(String codigoUn) {
        this.codigoUn = codigoUn;
    }

    public String getCodigoTes() {
        return codigoTes;
    }

    public void setCodigoTes(String codigoTes) {
        this.codigoTes = codigoTes;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    public void setNombreMostrar(String nombreMostrar) {
        this.nombreMostrar = nombreMostrar;
    }
    
    @Override
    public String toString() {
        return nombre;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".origen (idEntidad ,nombre, nombreMostrar) values("
                + this.idEntidad + ","
                + Util.comillas(this.nombre) + ","
                + Util.comillas(this.nombreMostrar)
                + ");";
    }

    public String SQLBuscar() {
        return "SELECT * FROM " + Var.dbNameBoes + ".origen WHERE nombre=" + Util.comillas(this.nombre) + " "
                + "and idEntidad=" + this.idEntidad;
    }

    public String SQLBuscarNombre() {
        return "SELECT * FROM " + Var.dbNameBoes + ".origen WHERE nombre=" + tools.Util.comillas(this.nombre);
    }

    public String SQLBuscarCodigo() {
        return "SELECT * FROM " + Var.dbNameBoes + ".origen WHERE codigo=" + tools.Util.comillas(this.codigo);
    }
}
