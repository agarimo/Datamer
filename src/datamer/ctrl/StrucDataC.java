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
import javafx.scene.control.Alert;
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
        tfEstructura.setText("");
        tfSancionado.setText("");
        tfNif.setText("");
        tfLocalidad.setText("");
        tfFecha.setText("");
        tfMatricula.setText("");
        tfCuantia.setText("");
        tfArticuloA.setText("");
        tfArticuloB.setText("");
        tfArticuloC.setText("");
        tfArticuloD.setText("");
        tfPreceptoA.setText("");
        tfPreceptoB.setText("");
        tfPreceptoC.setText("");
        tfPuntos.setText("");
        tfReqObs.setText("");
    }

    public void setParentController(ExtC controller) {
        this.controller = controller;
    }

    public void loadData(int id) {
        this.sd = Query.getStrucData(StrucData.SQLBuscar(id));

        if (sd == null) {
            sd = new StrucData(id);
        }

        tfEstructura.setText(Integer.toString(sd.getId()));
        tfExpediente.setText(Integer.toString(sd.getExpediente()));
        tfSancionado.setText(Integer.toString(sd.getSancionado()));
        tfNif.setText(Integer.toString(sd.getNif()));
        tfLocalidad.setText(Integer.toString(sd.getLocalidad()));
        tfFecha.setText(Integer.toString(sd.getFechaMulta()));
        tfMatricula.setText(Integer.toString(sd.getMatricula()));
        tfCuantia.setText(Integer.toString(sd.getCuantia()));
        tfArticuloA.setText(Integer.toString(sd.getArticuloA()));
        tfArticuloB.setText(Integer.toString(sd.getArticuloB()));
        tfArticuloC.setText(Integer.toString(sd.getArticuloC()));
        tfArticuloD.setText(Integer.toString(sd.getArticuloD()));
        tfPreceptoA.setText(Integer.toString(sd.getPreceptoA()));
        tfPreceptoB.setText(Integer.toString(sd.getPreceptoB()));
        tfPreceptoC.setText(Integer.toString(sd.getPreceptoC()));
        tfPuntos.setText(Integer.toString(sd.getPuntos()));
        tfReqObs.setText(Integer.toString(sd.getReqObs()));
    }

    public void saveData() {
        try {
            sd.setExpediente(Integer.parseInt(tfExpediente.getText()));
            sd.setSancionado(Integer.parseInt(tfSancionado.getText()));
            sd.setNif(Integer.parseInt(tfNif.getText()));
            sd.setLocalidad(Integer.parseInt(tfLocalidad.getText()));
            sd.setFechaMulta(Integer.parseInt(tfFecha.getText()));
            sd.setMatricula(Integer.parseInt(tfMatricula.getText()));
            sd.setCuantia(Integer.parseInt(tfCuantia.getText()));
            sd.setArticuloA(Integer.parseInt(tfArticuloA.getText()));
            sd.setArticuloB(Integer.parseInt(tfArticuloB.getText()));
            sd.setArticuloC(Integer.parseInt(tfArticuloC.getText()));
            sd.setArticuloD(Integer.parseInt(tfArticuloD.getText()));
            sd.setPreceptoA(Integer.parseInt(tfPreceptoA.getText()));
            sd.setPreceptoB(Integer.parseInt(tfPreceptoB.getText()));
            sd.setPreceptoC(Integer.parseInt(tfPreceptoC.getText()));
            sd.setPuntos(Integer.parseInt(tfPuntos.getText()));
            sd.setReqObs(Integer.parseInt(tfReqObs.getText()));

            Query.ejecutar(sd.SQLCrear());
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR EN FORMATO");
            alert.setContentText("Ha introducido caracteres no válidos.");
            alert.showAndWait();
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        saveData();
        controller.cerrarStrucData();
    }

}
