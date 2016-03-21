package datamer.model.testra;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Agarimo
 */
public class ModeloCaptura {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty edicto = new SimpleStringProperty();
    SimpleStringProperty csv = new SimpleStringProperty();
    SimpleStringProperty parametros = new SimpleStringProperty();
    SimpleIntegerProperty estado = new SimpleIntegerProperty();
    
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getEdicto() {
        return edicto.get();
    }

    public void setEdicto(String edicto) {
        this.edicto.set(edicto);
    }
    
    public String getCsv() {
        return csv.get();
    }

    public void setCsv(String csv) {
        this.csv.set(csv);
    }

    public String getParametros() {
        return parametros.get();
    }

    public void setParametros(String parametros) {
        this.parametros.set(parametros);
    }

    public int getEstado() {
        return estado.get();
    }

    public void setEstado(int estado) {
        this.estado.set(estado);
    }
}
