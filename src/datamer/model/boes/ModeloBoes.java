package datamer.model.boes;

import datamer.Var;
import javafx.beans.property.SimpleStringProperty;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class ModeloBoes {

    public SimpleStringProperty fecha = new SimpleStringProperty();
    public SimpleStringProperty codigo = new SimpleStringProperty();
    public SimpleStringProperty entidad = new SimpleStringProperty();
    public SimpleStringProperty origen = new SimpleStringProperty();
    public SimpleStringProperty descripcion = new SimpleStringProperty();
    public SimpleStringProperty link = new SimpleStringProperty();
    public Status status = Status.PENDING;
    public Boolean selected = null;

    public String getOrigen() {
        return origen.get();
    }

    public String getCodigo() {
        return codigo.get();
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public String getEntidad() {
        return entidad.get();
    }

    public String getLink() {
        return link.get();
    }

    public String getFecha() {
        return fecha.get();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        switch (status) {
            case "PENDING":
                this.status = Status.PENDING;
                break;
            case "APP":
                this.status = Status.APP;
                break;
            case "SOURCE":
                this.status = Status.SOURCE;
                break;
            case "USER":
                this.status = Status.USER;
                break;
            case "DELETED":
                this.status = Status.DELETED;
                break;
            case "DUPLICATED":
                this.status = Status.DUPLICATED;
                break;
            default:
                this.status = Status.PENDING;
        }
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return codigo.get() + " [" + this.status.toString() + "]";
    }
    
    public String SQLUpdate(){
        return "UPDATE "+Var.dbNameServer+".publicacion SET "
                + "selected="+this.selected+","
                + "status="+Util.comillas(this.status.toString())+" "
                + "WHERE codigo="+Util.comillas(this.codigo.get())+";";
    }
}
