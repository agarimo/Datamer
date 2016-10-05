/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamer.ctrl;

import datamer.ctrl.boes.ExtC;
import datamer.ctrl.boes.Query;
import datamer.model.Nota;
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

    private ExtC controller;

    private Nota nota;

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
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nota = null;
        textArea.setText("");
        lbEstructura.setText("");
    }

    public void setController(ExtC controller) {
        this.controller = controller;
    }

    public boolean setNota(int a) {
        nota = Query.getNota(a);

        if (nota != null) {
            lbEstructura.setText("Estructura - " + nota.getId());
            textArea.setText(nota.getDatos());
            return true;
        } else {
            nota = new Nota(a);
            lbEstructura.setText("Estructura - " + a);
            textArea.setText("");
            return false;
        }
    }

    private void setData(String data) {
        String query;

        if (data == null) {
            query = nota.SQLEliminar();
        } else {
            nota.setDatos(data);
            query = nota.SQLCrear();
        }

        Query.ejecutar(query);
    }

    @FXML
    void cerrar(ActionEvent event) {
        String data = textArea.getText();
        setData(data);
        controller.cerrarPopOver();
    }

    @FXML
    void borrar(ActionEvent event) {
        setData(null);
        controller.cerrarPopOver();
    }
}
