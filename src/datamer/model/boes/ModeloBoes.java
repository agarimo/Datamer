package datamer.model.boes;



import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Agarimo
 */
public class ModeloBoes {

    public SimpleStringProperty entidad = new SimpleStringProperty();
    public SimpleStringProperty origen = new SimpleStringProperty();
    public SimpleStringProperty codigo = new SimpleStringProperty();
    public SimpleStringProperty descripcion = new SimpleStringProperty();
    public SimpleStringProperty link = new SimpleStringProperty();
    public SimpleStringProperty fecha = new SimpleStringProperty();
    public Status status = Status.PENDING;

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

    @Override
    public String toString() {
        return codigo.get()+" ["+this.status.toString()+"]";
    }
}
