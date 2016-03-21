/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamer.ctrl.testra;

import datamer.Nav;
import datamer.Var;
import static datamer.Var.popup;
import datamer.ctrl.WinC;
import datamer.model.boes.ModeloBoletines;
import datamer.model.testra.ModeloCaptura;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Dates;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class CapturaC implements Initializable {

    @FXML
    private DatePicker dpFecha;

    @FXML
    private Button btDescargar;

    @FXML
    private Button btCapturador;

    @FXML
    private Button btReload;

    @FXML
    private Label lbTotal;

    @FXML
    private Label lbPendientes;

    @FXML
    private Label lbDescargados;

    @FXML
    private Label lbProgreso;

    @FXML
    private ProgressBar pgProgreso;

    @FXML
    private TableView tabla;

    @FXML
    private TableColumn edictoCL;

    @FXML
    private TableColumn csvCL;

    @FXML
    private TableColumn parametrosCL;

    @FXML
    private TableColumn estadoCL;

    private ObservableList<ModeloCaptura> listaCaptura;
    private Date fechaCaptura;
    

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTable();
        btDescargar.setDisable(true);
        btCapturador.setDisable(true);
        btReload.setDisable(true);
        lbTotal.setText("-");
        lbPendientes.setText("-");
        lbDescargados.setText("-");
        lbProgreso.setVisible(false);
        pgProgreso.setVisible(false);
    }

    public void initializeTable() {
        edictoCL.setCellValueFactory(new PropertyValueFactory<>("edicto"));
        edictoCL.setCellFactory(column -> {
            return new TableCell<ModeloBoletines, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setAlignment(Pos.CENTER);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setTextFill(Color.BLACK);
                        setStyle("");
                    }
                }
            };
        });
        csvCL.setCellValueFactory(new PropertyValueFactory<>("csv"));
        csvCL.setCellFactory(column -> {
            return new TableCell<ModeloBoletines, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setAlignment(Pos.CENTER);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setTextFill(Color.BLACK);
                        setStyle("");
                    }
                }
            };
        });
        parametrosCL.setCellValueFactory(new PropertyValueFactory<>("parametros"));
        parametrosCL.setCellFactory(column -> {
            return new TableCell<ModeloBoletines, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setAlignment(Pos.CENTER);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setTextFill(Color.BLACK);
                        setStyle("");
                    }
                }
            };
        });

        estadoCL.setCellValueFactory(new PropertyValueFactory<>("estado"));
        estadoCL.setCellFactory(column -> {
            return new TableCell<ModeloBoletines, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText("");

                        switch (item) {
                            case 0:
                                setTextFill(Color.BLACK);
                                setStyle("-fx-background-color: red");
                                break;
                            case 1:
                                setTextFill(Color.BLACK);
                                setStyle("-fx-background-color: orange");
                                break;
                            case 2:
                                setTextFill(Color.BLACK);
                                setStyle("-fx-background-color: green");
                                break;
                            default:
                                setTextFill(Color.BLACK);
                                setStyle("");
                                break;
                        }
                    }
                }
            };
        });

        edictoCL.prefWidthProperty().bind(tabla.widthProperty().multiply(0.12));
        csvCL.prefWidthProperty().bind(tabla.widthProperty().multiply(0.18));
        parametrosCL.prefWidthProperty().bind(tabla.widthProperty().multiply(0.60));
        estadoCL.prefWidthProperty().bind(tabla.widthProperty().multiply(0.08));

        listaCaptura = FXCollections.observableArrayList();
        tabla.setItems(listaCaptura);
    }

    @FXML
    void initCapturador(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Nav.TESTRA_CAPTURADOR));
            Pane nodo = (Pane) fxmlLoader.load();
            CapturadorC control = fxmlLoader.getController();
            control.setFecha(fecha);
            popup = new Stage();
            popup.initOwner(Var.stage);
            popup.setResizable(false);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initStyle(StageStyle.UTILITY);
            popup.setTitle("Capturador");
            popup.setScene(new Scene(nodo));
            popup.setAlwaysOnTop(true);
            Var.stage.hide();
            popup.show();
        } catch (IOException ex) {
            Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void initDescarga(ActionEvent event) {

    }

    @FXML
    void reloadData(ActionEvent event) {
        loadData();
    }

    @FXML
    void cambioEnDatePicker(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());

        if (fecha != null) {
            fechaCaptura=fecha;
            btDescargar.setDisable(false);
            btCapturador.setDisable(false);
            btReload.setDisable(false);
            loadData();
        }
    }

    void loadData() {

    }
}
