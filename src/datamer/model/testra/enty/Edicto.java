package datamer.model.testra.enty;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Agarimo
 */
public class Edicto {
    private int id;
    private String idEdicto;
    private String parametros;
    private String csv;
    private Date fecha;
    private int estado;
    private String datos;
    private int estadoCruce;
    
    public Edicto(){
        this.id=0;
        this.idEdicto=null;
        this.parametros=null;
        this.csv=null;
        this.fecha=null;
        this.estado=0;
        this.datos=null;
        this.estadoCruce=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdEdicto() {
        return idEdicto;
    }

    public void setIdEdicto(String idEdicto) {
        this.idEdicto = idEdicto;
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
        final Edicto other = (Edicto) obj;
        return Objects.equals(this.parametros, other.parametros);
    }

    @Override
    public String toString() {
        return "Edicto{" + "idEdicto=" + idEdicto + '}';
    }
    
    public String SQLBuscar(){
        return "";
    }
    
    public String SQLCrear(){
        return "";
    }
    
    public String SQLEditar(){
        return "";
    }
}
