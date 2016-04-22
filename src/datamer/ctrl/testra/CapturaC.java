package datamer.ctrl.testra;

import datamer.Nav;
import datamer.Var;
import static datamer.Var.popup;
import datamer.ctrl.WinC;
import datamer.ctrl.testra.captura.Download;
import datamer.model.boes.ModeloBoletines;
import datamer.model.testra.ModeloCaptura;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    private VBox rootPane;

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
    private Label lbProgreso;

    @FXML
    private ProgressIndicator pgProgreso;

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
        parametrosCL.prefWidthProperty().bind(tabla.widthProperty().multiply(0.605));
        estadoCL.prefWidthProperty().bind(tabla.widthProperty().multiply(0.08));

        listaCaptura = FXCollections.observableArrayList();
        tabla.setItems(listaCaptura);
    }

    @FXML
    void initCapturador(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());

        if (fecha != null) {
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
    }

    @FXML
    void initDescarga(ActionEvent event) {

        Thread a = new Thread(() -> {

            ModeloCaptura aux;
            List<ModeloCaptura> list;

            Platform.runLater(() -> {
                lbProgreso.setVisible(true);
                pgProgreso.setVisible(true);
                lbProgreso.setText("INICIANDO DESCARGA");
                pgProgreso.setProgress(-1);
                dpFecha.setDisable(true);
                btDescargar.setDisable(true);
                btReload.setDisable(true);
            });

            Download dw = new Download();
            list = listaCaptura.stream().filter(c -> c.getEstado() == 0).collect(Collectors.toList());
            final int total = list.size();

            Platform.runLater(() -> {
                lbProgreso.setText("DESCARGANDO 1 DE " + total);
            });

            for (int i = 0; i < list.size(); i++) {
                final int cont = i + 2;
                aux = list.get(i);
                dw.descargar(aux);

                loadData();

                Platform.runLater(() -> {
                    lbProgreso.setText("DESCARGANDO " + cont + " DE " + total);
                });
            }

            Platform.runLater(() -> {
                lbProgreso.setVisible(false);
                pgProgreso.setVisible(false);
                lbProgreso.setText("");
                pgProgreso.setProgress(0);
                dpFecha.setDisable(false);
                btDescargar.setDisable(false);
                btReload.setDisable(false);
            });

        });
        a.start();
    }

    @FXML
    void reloadData(ActionEvent event) {
        loadData();
    }

    @FXML
    void cambioEnDatePicker(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());

        if (fecha != null) {
            fechaCaptura = fecha;
            btDescargar.setDisable(false);
            btCapturador.setDisable(false);
            btReload.setDisable(false);
            loadData();
        }
    }

    void loadData() {
        Thread a = new Thread(() -> {

            Platform.runLater(() -> {
                rootPane.setCursor(Cursor.WAIT);
                btReload.setDisable(true);
            });

            List<ModeloCaptura> list = Query.listaModeloCaptura(fechaCaptura);
            listaCaptura.clear();
            listaCaptura.addAll(list);

            Platform.runLater(() -> {
                btReload.setDisable(false);
                lbTotal.setText(Integer.toString(listaCaptura.size()));
                lbPendientes.setText(Integer.toString((int) list.stream().filter(c -> c.getEstado() == 0).count()));
                rootPane.setCursor(Cursor.DEFAULT);
            });

        });
        a.start();
    }
}
