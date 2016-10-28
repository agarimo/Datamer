/*
 * The MIT License
 *
 * Copyright 2016 agarimo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package datamer.ctrl;

import datamer.ctrl.boes.ExtC;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.StrucData;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author agarimo
 */
public class StrucDataC implements Initializable {
    
    private ExtC controller;
    private StrucData sd;

    //<editor-fold defaultstate="collapsed" desc="FXML VAR">
    @FXML
    private TextField tfEstructura;

    @FXML
    private TextField tfExpediente;

    @FXML
    private TextField tfSancionado;

    @FXML
    private TextField tfNif;

    @FXML
    private TextField tfLocalidad;

    @FXML
    private TextField tfFecha;

    @FXML
    private TextField tfMatricula;

    @FXML
    private TextField tfCuantia;

    @FXML
    private TextField tfArticuloA;

    @FXML
    private TextField tfArticuloB;

    @FXML
    private TextField tfArticuloC;

    @FXML
    private TextField tfArticuloD;

    @FXML
    private TextField tfPreceptoA;

    @FXML
    private TextField tfPreceptoB;

    @FXML
    private TextField tfPreceptoC;

    @FXML
    private TextField tfPuntos;

    @FXML
    private TextField tfReqObs;

    @FXML
    private Button btGuardar;
//</editor-fold>

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setParentController(ExtC controller) {
        this.controller = controller;
    }
    
    public void loadData(int estructura){
        this.sd=Query.getStrucData(StrucData.SQLBuscar(estructura));
        
        //volcar datos en el panel.
        
    }
    
    public void saveData(){
        
    }
    
    @FXML
    void guardar(ActionEvent event){
        
    }

}
