package datamer.ctrl.boes;

import datamer.Var;
import datamer.ctrl.boes.boletines.Archivos;
import datamer.ctrl.boes.boletines.Estructuras;
import datamer.ctrl.boes.boletines.Fases;
import datamer.ctrl.boes.boletines.Union;
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
import java.util.Date;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import util.Dates;
import files.Util;
import sql.Sql;
import util.Varios;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class BoletinesC implements Initializable {

    @FXML
    Label lbContador;

    @FXML
    Label lbEstado;

    @FXML
    ProgressBar pbEstado;

    @FXML
    Button btEstructuras;

    @FXML
    Button btUnion;

    @FXML
    Button btEliminarDSC;

    @FXML
    SplitMenuButton btProcesar;

    @FXML
    MenuItem miEstructuras;

    @FXML
    MenuItem miEstructurasP;

    @FXML
    MenuItem miFases;

    @FXML
    Button btAbrirCarpetaBoletines;

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

        codigoCLB.prefWidthProperty().bind(tvBoletines.widthProperty().multiply(0.13));
        origenCLB.prefWidthProperty().bind(tvBoletines.widthProperty().multiply(0.56));
        fechaCLB.prefWidthProperty().bind(tvBoletines.widthProperty().multiply(0.1));
        tipoCLB.prefWidthProperty().bind(tvBoletines.widthProperty().multiply(0.07));
        faseCLB.prefWidthProperty().bind(tvBoletines.widthProperty().multiply(0.04));
        estructuraCLB.prefWidthProperty().bind(tvBoletines.widthProperty().multiply(0.116));

        boletinesList = FXCollections.observableArrayList();
        tvBoletines.setItems(boletinesList);
    }

    void cargaDatosTablaBoletines(Date fecha) {
        ModeloBoletines aux;
        String query = "SELECT * FROM " + Var.dbNameBoes + ".vista_boletines where fecha=" + Varios.comillas(Dates.imprimeFecha(fecha)) + " order by codigo";
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
        Date aux = Dates.asDate(dpFechaB.getValue());
        cargaDatosTablaBoletines(aux);
    }

    void trasvaseEx(Date fecha) {
        Sql bd;
        Procesar aux;
        Iterator it;

        try {
            bd = new Sql(Var.con);
            bd.ejecutar("DELETE FROM boes.procesar where fecha=" + Varios.comillas(Dates.imprimeFecha(fecha)));

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
        Date fecha = Dates.asDate(dpFechaB.getValue());

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    btProcesar.setDisable(true);
                    miEstructuras.setDisable(true);
                    miEstructurasP.setDisable(true);
                    miFases.setDisable(true);
                    btUnion.setDisable(true);
                    pbEstado.setVisible(true);
                    pbEstado.setProgress(0);
                    lbEstado.setText("INICIANDO ESTRUCTURAS");
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
                        lbEstado.setText("COMPROBANDO ESTRUCTURAS " + contadour + " de " + total);
                        pbEstado.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    es.run(aux);
                }

                Fases fs = new Fases(fecha);
                list = fs.getBoletines();

                for (int i = 0; i < list.size(); i++) {
                    final int contador = i;
                    final int total = list.size();
                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador + 1;
                        double toutal = total;
                        lbEstado.setText("COMPROBANDO FASE " + contadour + " de " + total);
                        pbEstado.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    fs.run(aux);
                }

                trasvaseEx(fecha);

                Platform.runLater(() -> {
                    lbEstado.setText("COMPROBACIÓN FINALIZADA");
                    btProcesar.setDisable(false);
                    miEstructuras.setDisable(false);
                    miEstructurasP.setDisable(false);
                    miFases.setDisable(false);
                    btUnion.setDisable(false);
                    pbEstado.setProgress(1);
                    pbEstado.setVisible(false);
                    lbEstado.setText("");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("COMPLETADO");
                    alert.setHeaderText("PROCESO FINALIZADO");
                    alert.setContentText("SE HA FINALIZADO LA COMPROBACIÓN DE ESTRUCTURAS Y FASES");
                    alert.showAndWait();

                    recargarBoletines(new ActionEvent());
                });
            });
            a.start();
        }
    }

    @FXML
    void comprobarEstructuras(ActionEvent event) {
        Date fecha = Dates.asDate(dpFechaB.getValue());

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    pbEstado.setVisible(true);
                    pbEstado.setProgress(0);
                    lbEstado.setText("INICIANDO ESTRUCTURAS");
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
                        lbEstado.setText("COMPROBANDO ESTRUCTURAS " + contadour + " de " + total);
                        pbEstado.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    es.run(aux);
                }

                trasvaseEx(fecha);

                Platform.runLater(() -> {
                    lbEstado.setText("COMPROBACIÓN FINALIZADA");
                    pbEstado.setProgress(1);
                    pbEstado.setVisible(false);
                    lbEstado.setText("");

                    recargarBoletines(new ActionEvent());
                });
            });
            a.start();
        }
    }

    @FXML
    void comprobarEstructurasP(ActionEvent event) {
        Date fecha = Dates.asDate(dpFechaB.getValue());

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    pbEstado.setVisible(true);
                    pbEstado.setProgress(0);
                    lbEstado.setText("INICIANDO ESTRUCTURAS");
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
                        lbEstado.setText("COMPROBANDO ESTRUCTURAS " + contadour + " de " + total);
                        pbEstado.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    es.run(aux);
                }

                trasvaseEx(fecha);

                Platform.runLater(() -> {
                    lbEstado.setText("COMPROBACIÓN FINALIZADA");
                    pbEstado.setProgress(1);
                    pbEstado.setVisible(false);
                    lbEstado.setText("");

                    recargarBoletines(new ActionEvent());
                });
            });
            a.start();
        }
    }

    @FXML
    void comprobarFases(ActionEvent event) {
        Date fecha = Dates.asDate(dpFechaB.getValue());

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    btEliminarDSC.setDisable(true);
                    pbEstado.setVisible(true);
                    pbEstado.setProgress(0);
                    lbEstado.setText("DESCARGANDO BOLETINES");
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
                        lbEstado.setText("COMPROBANDO FASE " + contadour + " de " + total);
                        pbEstado.setProgress(counter / toutal);
                    });
                    aux = (Boletin) list.get(i);
                    fs.run(aux);
                }

                Platform.runLater(() -> {
                    lbEstado.setText("COMPROBACIÓN FINALIZADA");
                    btEliminarDSC.setDisable(false);
                    pbEstado.setProgress(1);
                    pbEstado.setVisible(false);
                    lbEstado.setText("");

                    recargarBoletines(new ActionEvent());
                });
            });
            a.start();
        }
    }

    @FXML
    void generarArchivosUnion(ActionEvent event) {
        Date fecha = Dates.asDate(dpFechaB.getValue());

        File dir = new File(Var.ficheroUnion, Dates.imprimeFecha(fecha));
        Util.borraDirectorio(dir);
        dir.mkdirs();

        Thread a = new Thread(() -> {

            Platform.runLater(() -> {
                pbEstado.setVisible(true);
                pbEstado.setProgress(0);
                lbEstado.setText("GENERANDO ARCHIVOS .un");
            });

            String codigoUn, struc;
            Iterator it;
            Union un = new Union(fecha);
            Archivos ar = new Archivos();
            List list = un.getEstructuras();

            for (int i = 0; i < list.size(); i++) {
                final int contador = i;
                final int total = list.size();
                Platform.runLater(() -> {
                    int contadour = contador + 1;
                    double counter = contador + 1;
                    double toutal = total;
                    lbEstado.setText("GENERANDO ESTRUCTURA " + contadour + " de " + total);
                    pbEstado.setProgress(counter / toutal);
                });
                struc = (String) list.get(i);
                un.setMap(un.cargaMap(struc));
                it = un.getKeySet().iterator();

                while (it.hasNext()) {
                    codigoUn = (String) it.next();
                    ar.creaArchivos(un.getBoletines(codigoUn), fecha, struc, codigoUn);
                }
            }

            Platform.runLater(() -> {
                lbEstado.setText("PROCESO FINALIZADO");
                pbEstado.setProgress(1);
                pbEstado.setVisible(false);
                lbEstado.setText("");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("COMPLETADO");
                alert.setHeaderText("PROCESO FINALIZADO");
                alert.setContentText("SE HAN GENERADO LOS ARCHIVOS .un");
                alert.showAndWait();
            });
        });
        a.start();
    }

    @FXML
    void eliminarDSC(ActionEvent event) {
        Date fecha = Dates.asDate(dpFechaB.getValue());

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    btEliminarDSC.setDisable(true);
                    pbEstado.setVisible(true);
                    pbEstado.setProgress(-1);
                    lbEstado.setText("ELIMINANDO DESCARTADOS");
                });

                ModeloBoletines aux;
                String query = "SELECT * FROM " + Var.dbNameBoes + ".vista_boletines where "
                        + "fecha=" + Varios.comillas(Dates.imprimeFecha(fecha)) + " "
                        + "AND tipo='*DSC*'";
                Iterator<ModeloBoletines> it = Query.listaModeloBoletines(query).iterator();

                while (it.hasNext()) {
                    aux = it.next();
                    Query.eliminaBoletinFase(aux.getCodigo());
                }

                Platform.runLater(() -> {
                    lbEstado.setText("PROCESO FINALIZADO");
                    btEliminarDSC.setDisable(false);
                    pbEstado.setProgress(1);
                    pbEstado.setVisible(false);
                    lbEstado.setText("");

                    recargarBoletines(new ActionEvent());
                });
            });
            a.start();
        }
    }

    @FXML
    void recargarBoletines(ActionEvent event) {
        boletinesList.clear();
        Date aux = Dates.asDate(dpFechaB.getValue());

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

                Thread a = new Thread(() -> {
                    boletinesList.remove(aux);
                    Query.eliminaBoletin(aux.getCodigo());
                });
                a.start();

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
                datos = bd.getString("SELECT datos FROM " + Var.dbNameServer + ".publicacion WHERE codigo=" + Varios.comillas(aux.getCodigo()));
                bd.close();
            } catch (SQLException ex) {
                Logger.getLogger(BoletinesC.class.getName()).log(Level.SEVERE, null, ex);
            }

            Util.escribeArchivo(file, datos);

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
}
