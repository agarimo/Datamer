/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamer.ctrl;

import datamer.Var;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author agari
 */
public class NotasC implements Initializable {
    
    private int estructura;
    
    @FXML
    private TextArea textArea;
    
    @FXML
    private Button btCerrar;
    
    @FXML
    private Button btBorrar;
    
    @FXML
    private Label lbEstructura;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estructura = 0;
        textArea.setText("");
        lbEstructura.setText("");
    }

    public void setEstructura(int a){
        lbEstructura.setText("Estructura - "+a);
        textArea.setText(getData(a));
    }
    
    private String getData(int a){
        return "";
    }
    
    private void setData(String data){
        if(data==null){
            
        }else{
            
        }
    }
    
    @FXML
    void cerrar(ActionEvent event){
        String data = textArea.getText();
        setData(data);
    }
    
    @FXML
    void borrar(ActionEvent event){
        setData(null);
    }
}
