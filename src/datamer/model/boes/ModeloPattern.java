/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamer.model.boes;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Agarimo
 */
public class ModeloPattern {
    
    public SimpleStringProperty id = new SimpleStringProperty();
    public SimpleStringProperty dato = new SimpleStringProperty();
    
    public String getId(){
        return this.id.get();
    }
    
    public String getDato(){
        return this.dato.get();
    }
}
