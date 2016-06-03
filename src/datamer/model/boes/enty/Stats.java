package datamer.model.boes.enty;

import datamer.model.boes.Status;
import datamer.Var;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Stats {

    private int id;
    private String fecha;
    private String codigo;
    private boolean isSelected;
    private Status status;
    private String entidad;
    private String origen;
    private String descripcion;
    private String link;
    private String cve;

    public Stats() {
    }

    public Stats(String fecha, String codigo, boolean isSelected, Status status, String entidad, String origen, String descripcion, String link, String cve) {
        this.fecha = fecha;
        this.codigo = codigo;
        this.isSelected = isSelected;
        this.status = status;
        this.entidad = entidad;
        this.origen = origen;
        this.descripcion = descripcion;
        this.link = link;
        this.cve = cve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

//    public String SQLCrear() {
//        return "INSERT into " + Var.dbNameBoesStats + ".boletines (fecha, codigo, isSelected, status, cve, entidad, origen, descripcion, link) values("
//                + Varios.entrecomillar(this.fecha) + ","
//                + Varios.entrecomillar(this.codigo) + ","
//                + this.isSelected + ","
//                + Varios.entrecomillar(this.status.toString()) + ","
//                + Varios.entrecomillar(this.cve) + ","
//                + Varios.entrecomillar(this.entidad) + ","
//                + Varios.entrecomillar(this.origen) + ","
//                + Varios.entrecomillar(this.descripcion) + ","
//                + Varios.entrecomillar(this.link)
//                + ");";
//    }
}
