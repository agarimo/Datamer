package datamer.ctrl.boes;

import datamer.Var;
import datamer.ctrl.boes.boletines.Estructuras;
import datamer.ctrl.boes.boletines.Fases;
import datamer.model.boes.ModeloBoletines;
import datamer.model.boes.enty.Boletin;
import datamer.model.boes.enty.Procesar;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import org.controlsfx.control.MaskerPane;
import tools.LoadFile;
import sql.Sql;
import tools.Util;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class BoletinesC implements Initializable {

    @FXML
    Label lbContador;

    @FXML
    Button btEstructuras;

    @FXML
    SplitMenuButton btProcesar;

    @FXML
    MenuItem miEstructuras;

    @FXML
    MenuItem miEstructurasP;

    @FXML
    MenuItem miFases;

    @FXML
    Button btRecargarBoletines;

    @FXML
    TableView<ModeloBoletines> tvBoletines;

    @FXML
    TableColumn<ModeloBoletines, String> codigoCLB;

    @FXML
    TableColumn<ModeloBoletines, String> origenCLB;

    @FXML
    TableColumn<ModeloBoletines, String> fechaCLB;

    @FXML
    TableColumn<ModeloBoletines, String> tipoCLB;

    @FXML
    TableColumn<ModeloBoletines, Integer> faseCLB;

    @FXML
    TableColumn<ModeloBoletines, String> estructuraCLB;

    @FXML
    DatePicker dpFechaB;

    @FXML
    MaskerPane masker;

    ObservableList<ModeloBoletines> boletinesList;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTable();
    }

    void initializeTable() {
        codigoCLB.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        codigoCLB.setCellFactory(column -> {
            return new TableCell<ModeloBoletines, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

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
        origenCLB.setCellValueFactory(new PropertyValueFactory<>("origen"));
        fechaCLB.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tipoCLB.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        faseCLB.setCellValueFactory(new PropertyValueFactory<>("isFase"));
        faseCLB.setCellFactory(column -> {
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
                            case 3:
                                setTextFill(Color.BLACK);
                                setStyle("-fx-background-color: blue");
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
        estructuraCLB.setCellValueFactory(new PropertyValueFactory<>("isEstructura"));
        estructuraCLB.setCellFactory(column -> {
            return new TableCell<ModeloBoletines, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setTextFill(Color.BLACK);
                        setStyle("-fx-background-color: green");
                    }
                }
            };
        });

        tvBoletines.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        codigoCLB.setMaxWidth(1f * Integer.MAX_VALUE * 13);
        origenCLB.setMaxWidth(1f * Integer.MAX_VALUE * 56);
        fechaCLB.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        tipoCLB.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        faseCLB.setMaxWidth(1f * Integer.MAX_VALUE * 4);
        estructuraCLB.setMaxWidth(1f * Integer.MAX_VALUE * 10);

        boletinesList = FXCollections.observableArrayList();
        tvBoletines.setItems(boletinesList);
    }

    void cargaDatosTablaBoletines(LocalDate fecha) {
        ModeloBoletines aux;
        String query = "SELECT * FROM " + Var.dbNameBoes + ".vista_boletines where fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)) + " order by codigo";
        Iterator it = Query.listaModeloBoletines(query).iterator();

        while (it.hasNext()) {
            aux = (ModeloBoletines) it.next();
            boletinesList.add(aux);
        }

        lbContador.setText(Integer.toString(boletinesList.size()));
    }

    @FXML
    void cambioEnDatePickerBoletines(ActionEvent event) {
        boletinesList.clear();
        LocalDate aux = dpFechaB.getValue();
        cargaDatosTablaBoletines(aux);
        convertExcel(aux);
    }

    void trasvaseEx(LocalDate fecha) {
        Sql bd;
        Procesar aux;
        Iterator it;

        try {
            bd = new Sql(Var.con);
            bd.ejecutar("DELETE FROM boes.procesar where fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)));

            it = Query.listaProcesarPendiente(fecha).iterator();

            while (it.hasNext()) {
                aux = (Procesar) it.next();
                bd.ejecutar(aux.SQLCrear());
            }
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(BoletinesC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void procesarBoletines(ActionEvent event) {
        LocalDate fecha = dpFechaB.getValue();

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    masker.setVisible(true);
                    masker.setProgress(-1);
                    masker.setText("INICIANDO ESTRUCTURAS");
                });

                Boletin aux;
                Estructuras es = new Estructuras(fecha, false);
                List list = es.getBoletines();

                for (int i = 0; i < list.size(); i++) {
                    final int contador = i;
                    final int total = list.size();
                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador + 1;
                        double toutal = total;
                        masker.setText("ESTRUCTURA " + contadour + " de " + total);
                        masker.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    es.run(aux);
                }

                Platform.runLater(() -> {
                    masker.setText("INICIANDO FASES");
                    masker.setProgress(-1);
                });

                Fases fs = new Fases(fecha);
                list = fs.getBoletines();

                for (int i = 0; i < list.size(); i++) {
                    final int contador = i;
                    final int total = list.size();
                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador + 1;
                        double toutal = total;
                        masker.setText("FASE " + contadour + " de " + total);
                        masker.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    fs.run(aux);
                }

                Platform.runLater(() -> {
                    masker.setText("EJECUTANDO TRASVASE");
                    masker.setProgress(-1);
                });

                trasvaseEx(fecha);

                Platform.runLater(() -> {
                    masker.setText("PROCESO FINALIZADO");
                    masker.setProgress(-1);
                    masker.setVisible(false);
                    masker.setText("");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("COMPLETADO");
                    alert.setHeaderText("PROCESO FINALIZADO");
                    alert.setContentText("SE HA FINALIZADO LA COMPROBACIÓN DE ESTRUCTURAS Y FASES");
                    alert.showAndWait();

                    recargarBoletines(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    @FXML
    void comprobarEstructuras(ActionEvent event) {
        LocalDate fecha = dpFechaB.getValue();

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    masker.setVisible(true);
                    masker.setProgress(-1);
                    masker.setText("INICIANDO ESTRUCTURAS");
                });

                Boletin aux;
                Estructuras es = new Estructuras(fecha, false);
                List list = es.getBoletines();

                for (int i = 0; i < list.size(); i++) {
                    final int contador = i;
                    final int total = list.size();
                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador + 1;
                        double toutal = total;
                        masker.setText("ESTRUCTURA " + contadour + " de " + total);
                        masker.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    es.run(aux);
                }

                Platform.runLater(() -> {
                    masker.setText("FINALIZANDO PROCESO");
                    masker.setProgress(-1);
                });

                trasvaseEx(fecha);

                Platform.runLater(() -> {
                    masker.setText("PROCESO FINALIZADO");
                    masker.setProgress(-1);
                    masker.setVisible(false);
                    masker.setText("");

                    recargarBoletines(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    @FXML
    void comprobarEstructurasP(ActionEvent event) {
        LocalDate fecha = dpFechaB.getValue();

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    masker.setVisible(true);
                    masker.setProgress(-1);
                    masker.setText("INICIANDO ESTRUCTURAS");
                });

                Boletin aux;
                Estructuras es = new Estructuras(fecha, true);
                List list = es.getBoletines();

                for (int i = 0; i < list.size(); i++) {
                    final int contador = i;
                    final int total = list.size();
                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador + 1;
                        double toutal = total;
                        masker.setText("ESTRUCTURA " + contadour + " de " + total);
                        masker.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    es.run(aux);
                }

                Platform.runLater(() -> {
                    masker.setText("FINALIZANDO PROCESO");
                    masker.setProgress(-1);
                });

                trasvaseEx(fecha);

                Platform.runLater(() -> {
                    masker.setText("PROCESO FINALIZADO");
                    masker.setProgress(-1);
                    masker.setVisible(false);
                    masker.setText("");

                    recargarBoletines(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    @FXML
    void comprobarFases(ActionEvent event) {
        LocalDate fecha = dpFechaB.getValue();

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    masker.setVisible(true);
                    masker.setProgress(-1);
                    masker.setText("INICIANDO FASES");
                });

                Boletin aux;
                Fases fs = new Fases(fecha);
                List list = fs.getBoletines();

                for (int i = 0; i < list.size(); i++) {
                    final int contador = i;
                    final int total = list.size();
                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador + 1;
                        double toutal = total;
                        masker.setText("FASE " + contadour + " de " + total);
                        masker.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    fs.run(aux);
                }

                Platform.runLater(() -> {
                    masker.setText("PROCESO FINALIZADO");
                    masker.setProgress(-1);
                    masker.setVisible(false);
                    masker.setText("");

                    recargarBoletines(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    @FXML
    void recargarBoletines(ActionEvent event) {
        boletinesList.clear();
        tvBoletines.refresh();
        LocalDate aux = dpFechaB.getValue();

        if (aux != null) {
            cargaDatosTablaBoletines(aux);
        }
    }

    @FXML
    void abrirCarpetaBoletines(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new File("data").toURI());
        } catch (IOException ex) {
            Logger.getLogger(BoletinesC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void eliminarBoletin(ActionEvent event) {
        ModeloBoletines aux = tvBoletines.getSelectionModel().getSelectedItem();

        if (aux != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("BORRAR BOLETÍN");
            alert.setHeaderText(aux.getCodigo());
            alert.setContentText("¿Desea BORRAR el boletín seleccionado?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                boletinesList.remove(aux);
                tvBoletines.refresh();

                Thread a = new Thread(() -> {
                    Query.eliminaBoletin(aux.getCodigo());
                });
                Var.executor.execute(a);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR de selección.");
            alert.setContentText("Debes seleccionar un boletín.");

            alert.showAndWait();
        }
    }

    @FXML
    void verBoletin(ActionEvent event) {
        Sql bd;
        File file = new File("boletinTemp.txt");
        ModeloBoletines aux = tvBoletines.getSelectionModel().getSelectedItem();
        String datos = "";

        if (aux != null) {
            try {
                bd = new Sql(Var.con);
                datos = bd.getString("SELECT datos FROM " + Var.dbNameServer + ".publicacion WHERE codigo=" + Util.comillas(aux.getCodigo()));
                bd.close();
            } catch (SQLException ex) {
                Logger.getLogger(BoletinesC.class.getName()).log(Level.SEVERE, null, ex);
            }

            LoadFile.writeFile(file, datos);

            try {
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException ex) {
                Logger.getLogger(BoletinesC.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR de selección.");
            alert.setContentText("Debes seleccionar un boletín.");

            alert.showAndWait();
        }
    }

    @FXML
    void verBoletinWeb(ActionEvent event) {
        ModeloBoletines aux = tvBoletines.getSelectionModel().getSelectedItem();

        if (aux != null) {
            try {
                Desktop.getDesktop().browse(new URI(aux.getLink()));
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(BoletinesC.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR de selección.");
            alert.setContentText("Debes seleccionar un boletín.");

            alert.showAndWait();
        }
    }

    private void convertExcel(LocalDate fecha) {
        File fichero = new File(Var.fileRemote, fecha.format(DateTimeFormatter.ISO_DATE));

        if (fichero.exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONVERSIÓN");
            alert.setHeaderText("");
            alert.setContentText("¿Desea CONVERTIR los PDF a EXCEL?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                if (Var.nitro != null) {
                    tools.Files.openFile(Var.nitro);
                } else {
                    tools.Files.openFile(fichero);
                }
            }
        }
    }
}
