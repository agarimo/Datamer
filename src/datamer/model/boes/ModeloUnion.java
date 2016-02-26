package datamer.model.boes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Agarimo
 */
public class ModeloUnion {

    public SimpleStringProperty codigo = new SimpleStringProperty();
    public SimpleIntegerProperty idDescarga = new SimpleIntegerProperty();
    public SimpleStringProperty fecha = new SimpleStringProperty();
    public SimpleStringProperty codigoUn = new SimpleStringProperty();
    public SimpleStringProperty codigoProv = new SimpleStringProperty();
    public SimpleStringProperty estructura = new SimpleStringProperty();
    
    public String getCodigo(){
        return this.codigo.get();
    }
    
    public int getIdDescarga(){
        return this.idDescarga.get();
    }
    
    public String getFecha(){
        return this.fecha.get();
    }
    
    public String getCodigoUn(){
        return this.codigoUn.get();
    }
    
    public String getEstructura(){
        return this.estructura.get();
    }
    
    public String getCodigoProv(){
        return this.codigoProv.get();
    }
}
