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
import datamer.ctrl.boes.ext.ManualStruc;
import datamer.model.boes.ModeloProcesar;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author agarimo
 */
public class ExtManualC implements Initializable {

    private ExtC controller;
    private ManualStruc struc;

    private ModeloProcesar boletin;

    private ObservableList<String> modelo;

    //<editor-fold defaultstate="collapsed" desc="FXML VAR">
    @FXML
    private VBox panelTexto;

    @FXML
    private TextArea taTexto;

    @FXML
    private Button btTextoContinuar;

    @FXML
    private VBox panelEstructura;

    @FXML
    private ComboBox<String> cbExpediente;

    @FXML
    private ComboBox<String> cbSancionado;

    @FXML
    private ComboBox<String> cbNif;

    @FXML
    private ComboBox<String> cbLocalidad;

    @FXML
    private ComboBox<String> cbFecha;

    @FXML
    private ComboBox<String> cbMatricula;

    @FXML
    private ComboBox<String> cbCuantia;

    @FXML
    private ComboBox<String> cbArticulo;

    @FXML
    private ComboBox<String> cbPrecepto;

    @FXML
    private ComboBox<String> cbPuntos;

    @FXML
    private ComboBox<String> cbReqObs;

    @FXML
    private Label lbLineas;

    @FXML
    private Label lbColumnas;

    @FXML
    private Button btEstructuraCancelar;

    @FXML
    private Button btEstructuraAceptar;
//</editor-fold>

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        panelTexto.setVisible(true);
        panelEstructura.setVisible(false);
        modelo = FXCollections.observableArrayList();

        cbExpediente.setItems(modelo);
        cbSancionado.setItems(modelo);
        cbNif.setItems(modelo);
        cbLocalidad.setItems(modelo);
        cbFecha.setItems(modelo);
        cbMatricula.setItems(modelo);
        cbCuantia.setItems(modelo);
        cbArticulo.setItems(modelo);
        cbPrecepto.setItems(modelo);
        cbPuntos.setItems(modelo);
        cbReqObs.setItems(modelo);

    }

    public void setParentController(ExtC controller) {
        this.controller = controller;
    }

    public void setBoletin(ModeloProcesar boletin) {
        this.boletin = boletin;
    }

    private void close() {
        cancelarEstructura(new ActionEvent());
        controller.cerrarStrucData();
    }

    @FXML
    void aceptarEstructura(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONTINUAR");
        alert.setHeaderText("Se va a procesar la estructura.");
        alert.setContentText("¿Desea CONTINUAR?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            getEstructura();
            controller.previsualizarManual(struc.getMultas());
            close();
        }
    }

    void getEstructura() {
        struc.setExpediente(cbExpediente.getSelectionModel().getSelectedIndex());
        struc.setSancionado(cbSancionado.getSelectionModel().getSelectedIndex());
        struc.setNif(cbNif.getSelectionModel().getSelectedIndex());
        struc.setLocalidad(cbLocalidad.getSelectionModel().getSelectedIndex());
        struc.setFecha(cbFecha.getSelectionModel().getSelectedIndex());
        struc.setMatricula(cbMatricula.getSelectionModel().getSelectedIndex());
        struc.setCuantia(cbCuantia.getSelectionModel().getSelectedIndex());
        struc.setArticulo(cbArticulo.getSelectionModel().getSelectedIndex());
        struc.setPrecepto(cbPrecepto.getSelectionModel().getSelectedIndex());
        struc.setPuntos(cbPuntos.getSelectionModel().getSelectedIndex());
        struc.setReqObs(cbReqObs.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void cancelarEstructura(ActionEvent event) {
        panelEstructura.setVisible(false);
        panelTexto.setVisible(true);

        lbLineas.setText("");
        lbColumnas.setText("");

        modelo.clear();
    }

    @FXML
    void continuarTexto(ActionEvent event) {
        struc = new ManualStruc(boletin, taTexto.getText().trim());

        if (struc.getLineas() > 0) {
            continuarTexto();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText("SIN DATOS");
            alert.setContentText("Texto no válido, comprueba el formateo.");
            alert.showAndWait();
        }
    }

    @FXML
    void restore(ActionEvent event) {
        cbExpediente.getSelectionModel().select(-1);
        cbSancionado.getSelectionModel().select(-1);
        cbNif.getSelectionModel().select(-1);
        cbLocalidad.getSelectionModel().select(-1);
        cbFecha.getSelectionModel().select(-1);
        cbMatricula.getSelectionModel().select(-1);
        cbCuantia.getSelectionModel().select(-1);
        cbArticulo.getSelectionModel().select(-1);
        cbPrecepto.getSelectionModel().select(-1);
        cbPuntos.getSelectionModel().select(-1);
        cbReqObs.getSelectionModel().select(-1);
    }

    private void continuarTexto() {
        taTexto.setText("");
        panelTexto.setVisible(false);
        panelEstructura.setVisible(true);

        lbLineas.setText(Integer.toString(struc.getLineas()));
        lbColumnas.setText(Integer.toString(struc.getColumnas()));

        modelo.addAll(struc.getModel());
    }

}
