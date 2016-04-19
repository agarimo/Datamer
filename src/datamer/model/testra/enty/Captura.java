package datamer.model.testra.enty;

import datamer.Var;
import java.util.Date;
import java.util.Objects;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Captura {

    private int id;
    private String codigo;
    private String parametros;
    private String csv;
    private Date fecha;
    private int estado;
    private String datos;
    private int estadoCruce;
    
    public Captura() {
        this.id = 0;
        this.codigo = null;
        this.parametros = null;
        this.csv = null;
        this.fecha = null;
        this.estado = 0;
        this.datos = null;
        this.estadoCruce = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String idEdicto) {
        this.codigo = idEdicto;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public int getEstadoCruce() {
        return estadoCruce;
    }

    public void setEstadoCruce(int estadoCruce) {
        this.estadoCruce = estadoCruce;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.parametros);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Captura other = (Captura) obj;
        return Objects.equals(this.parametros, other.parametros);
    }

    @Override
    public String toString() {
        return "Edicto{" + "idEdicto=" + codigo + '}';
    }

    public String SQLBuscar() {
        return "";
    }
    
    public static String SQLBuscar(Date fecha) {
        return "SELECT id,codigo,fecha,csv,datos,estado_cruce FROM " + Var.dbNameTestra + ".captura "
                + "where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(fecha)) + " "
                + "and estado_cruce <5";
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameTestra + ".captura (id_edicto,parametros,csv,fecha,estado,datos,estado_cruce) values("
                + Varios.entrecomillar(this.codigo) + ","
                + Varios.entrecomillar(this.parametros) + ","
                + Varios.entrecomillar(this.csv) + ","
                + Varios.entrecomillar(Dates.imprimeFecha(fecha)) + ","
                + this.estado + ","
                + Varios.entrecomillar(this.datos) + ","
                + this.estadoCruce
                + ");";
    }

    public String SQLsetEstado() {
        return "UPDATE " + Var.dbNameTestra + ".captura SET "
                + "estado=" + this.estado + " "
                + "WHERE id=" + this.id;
    }

    public String SQLsetEstadoCruce() {
        return "UPDATE " + Var.dbNameTestra + ".captura SET "
                + "estado_cruce=" + this.estadoCruce + " "
                + "WHERE id=" + this.id;
    }

    public String SQLsetDatos() {
        return "UPDATE " + Var.dbNameTestra + ".captura SET "
                + "id_edicto=" + Varios.entrecomillar(this.codigo) + ","
                + "csv=" + Varios.entrecomillar(this.csv) + ","
                + "estado=" + this.estado + ","
                + "estado_cruce=" + this.estadoCruce + ","
                + "datos=" + Varios.entrecomillar(this.datos) + " "
                + "WHERE id=" + this.id;
    }
}
