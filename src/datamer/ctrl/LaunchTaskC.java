/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamer.ctrl;

import datamer.Var;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import socket.enty.ModeloTarea;
import socket.enty.ServerTask;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class LaunchTaskC implements Initializable {

    @FXML
    private Accordion acordeon;

    @FXML
    private TitledPane panelTipo;

    @FXML
    private ListView<ModeloTarea> lista;

    @FXML
    private TitledPane panelParametros;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button btEjecutar;

    ObservableList<ModeloTarea> listado;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        acordeon.setExpandedPane(acordeon.getPanes().get(0));
        initializeList();

    }

    private void initializeList() {
        listado = FXCollections.observableArrayList();
        lista.setItems(listado);

        String host = "";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(LaunchTaskC.class.getName()).log(Level.SEVERE, null, ex);
        }

        ModeloTarea mt = new ModeloTarea();
        mt.setPropietario(host);
        mt.setTipoTarea(ServerTask.BOE);
        mt.setFechaInicio(LocalDateTime.now());
        listado.add(mt);

        mt = new ModeloTarea();
        mt.setPropietario(host);
        mt.setTipoTarea(ServerTask.BOE_CLASIFICACION);
        mt.setFechaInicio(LocalDateTime.now());
        listado.add(mt);

        mt = new ModeloTarea();
        mt.setPropietario(host);
        mt.setTipoTarea(ServerTask.ESTRUCTURAS);
        mt.setFechaInicio(LocalDateTime.now());
        listado.add(mt);

        mt = new ModeloTarea();
        mt.setPropietario(host);
        mt.setTipoTarea(ServerTask.ESTRUCTURAS_PENDIENTES);
        mt.setFechaInicio(LocalDateTime.now());
        listado.add(mt);

        mt = new ModeloTarea();
        mt.setPropietario(host);
        mt.setTipoTarea(ServerTask.FASES);
        mt.setFechaInicio(LocalDateTime.now());
        listado.add(mt);
    }

    private ModeloTarea getData() {
        ModeloTarea mt = lista.getSelectionModel().getSelectedItem();

        if (mt != null) {
            LocalDate date = datePicker.getValue();

            if (date != null) {
                mt.setParametros(date.format(DateTimeFormatter.ISO_DATE));
            } else {
                mt.setParametros(null);
            }

            return mt;
        } else {
            return null;
        }
    }

    @FXML
    void ejecutar(ActionEvent event) {

        ModeloTarea mt = getData();

        if (mt != null) {
            Var.mainController.launchTask(mt);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Debes seleccionar un elemento.");
            alert.showAndWait();
        }
    }
}
