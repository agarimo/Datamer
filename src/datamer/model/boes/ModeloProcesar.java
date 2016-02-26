package datamer.model.boes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Agarimo
 */
public class ModeloProcesar {

    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    public SimpleStringProperty codigo = new SimpleStringProperty();
    public SimpleIntegerProperty estructura = new SimpleIntegerProperty();
    public SimpleIntegerProperty estado = new SimpleIntegerProperty();
    public SimpleStringProperty link = new SimpleStringProperty();
    public SimpleStringProperty fecha = new SimpleStringProperty();
    

    public int getId(){
        return id.get();
    }
    
    public String getCodigo() {
        return codigo.get();
    }

    public int getEstructura() {
        return estructura.get();
    }

    public int getEstado() {
        return estado.get();
    }

    public String getLink() {
        return link.get();
    }

    public String getFecha() {
        return fecha.get();
    }
}
