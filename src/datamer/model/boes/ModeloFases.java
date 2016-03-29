package datamer.model.boes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Agarimo
 */
public class ModeloFases {

    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    public SimpleIntegerProperty idOrigen = new SimpleIntegerProperty();
    public SimpleStringProperty codigo = new SimpleStringProperty();
    public SimpleIntegerProperty tipo = new SimpleIntegerProperty();
    public SimpleStringProperty texto1 = new SimpleStringProperty();
    public SimpleStringProperty texto2 = new SimpleStringProperty();
    public SimpleStringProperty texto3 = new SimpleStringProperty();
    public SimpleStringProperty plazo = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public int getIdOrigen() {
        return idOrigen.get();
    }

    public String getCodigo() {
        return codigo.get();
    }
    
    public int getIdTipo(){
        return tipo.get();
    }

    public String getTipo() {
        switch (tipo.get()) {
            case 1:
                return "Notificación de Denuncias";
            case 2:
                return "Resolución de Sanciones";
            case 3:
                return "Resolución de Recursos";
            default:
                return "Desconocido";
        }
    }

    public String getTexto1() {
        return texto1.get();
    }

    public String getTexto2() {
        return texto2.get();
    }

    public String getTexto3() {
        return texto3.get();
    }

    public String getPlazo() {
        return plazo.get();
    }

    public String getFase() {
        return "(" + codigo.get() + ")" + plazo.get() + tipoToString();
    }
    
    private String tipoToString() {
        switch (tipo.get()) {
            case 1:
                return "ND";
            case 2:
                return "RS";
            case 3:
                return "RR";
            default:
                return "Desconocido";
        }
    }
    
}
