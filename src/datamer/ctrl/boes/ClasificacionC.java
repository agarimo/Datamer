package datamer.ctrl.boes;

import datamer.Var;
import datamer.ctrl.boes.boe.Boe;
import datamer.ctrl.boes.boe.Download;
import datamer.ctrl.boes.boe.Insercion;
import datamer.ctrl.boes.boe.Pdf;
import datamer.ctrl.boes.boe.Publicacion;
import datamer.ctrl.boes.boletines.Limpieza;
import datamer.model.boes.ModeloBoes;
import datamer.model.boes.enty.Descarga;
import datamer.model.boes.enty.Boletin;
import datamer.model.boes.Status;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Dates;
import sql.Sql;
import util.Varios;





/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class ClasificacionC implements Initializable {

    private static final Logger LOG = LogManager.getLogger(ClasificacionC.class);

    Sql bd;
    boolean autoScroll;

    ObservableList<ModeloBoes> publicacion;
    ObservableList<ModeloBoes> selectedList;
    ObservableList<ModeloBoes> discartedList;

    //<editor-fold defaultstate="collapsed" desc="FXML VAR">
    @FXML
    private VBox rootPane;
    @FXML
    private TableView<ModeloBoes> tvBoes;
    @FXML
    private TableColumn<ModeloBoes, String> codigoCL;
    @FXML
    private TableColumn<ModeloBoes, String> origenCL;
    @FXML
    private TableColumn<ModeloBoes, String> descripcionCL;
    @FXML
    private ListView<ModeloBoes> lvSelect;
    @FXML
    private ListView<ModeloBoes> lvDiscard;
    @FXML
    private Label lbClasificacion;
    @FXML
    private Label lbContadorT;
    @FXML
    private CheckBox cbAutoScroll;
    @FXML
    private ProgressBar pbClasificacion;
    @FXML
    private DatePicker dpFechaC;
    @FXML
    private Button btSelect;
    @FXML
    private Button btRecargarClasificacion;
    @FXML
    private Button btRecoverD;
    @FXML
    private Button btSelectAll;
    @FXML
    private Button btRecoverS;
    @FXML
    private Button btVerWebC;
    @FXML
    private Button btDiscard;
    @FXML
    private Button btFinClas;
    @FXML
    private TitledPane tpDescartados;
    @FXML
    private TitledPane tpSeleccionados;
    //</editor-fold>

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeIcons();
        Tooltip a = new Tooltip();
        a.setText("Seleccionar todos");
        btSelectAll.setTooltip(a);
        initializeTable();
        initializeClear();
        autoScroll = true;
        cbAutoScroll.setSelected(autoScroll);
        setProcesandoC(false);

    }

    private void initializeIcons() {
        String green = "#008000";
        String red = "#FF0000";
        String orange = "#FFA500";
        Text text;
        
        text = GlyphsDude.createIcon(MaterialIcon.ADD_CIRCLE, "32");
        text.setFill(Paint.valueOf(green));
        btSelect.setGraphic(text);
        
        text = GlyphsDude.createIcon(MaterialIcon.REMOVE_CIRCLE, "32");
        text.setFill(Paint.valueOf(red));
        btDiscard.setGraphic(text);
        
        
        text = GlyphsDude.createIcon(MaterialIcon.CACHED, "32");
        text.setFill(Paint.valueOf(orange));
        btRecargarClasificacion.setGraphic(text);
        
        
        GlyphsDude.setIcon(btSelectAll, MaterialIcon.PLAYLIST_ADD, "32");
        GlyphsDude.setIcon(btRecoverS, MaterialIcon.INPUT, "32");
        GlyphsDude.setIcon(btRecoverD, MaterialIcon.INPUT, "32");
    }

    void initializeClear() {
        publicacion.clear();
        discartedList.clear();
        selectedList.clear();
        dpFechaC.setValue(null);
    }

    void initializeTable() {
        origenCL.setCellValueFactory(new PropertyValueFactory<>("origen"));
        origenCL.setCellFactory((TableColumn<ModeloBoes, String> arg0) -> new TableCell<ModeloBoes, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    text = new Text(item);
                    text.setWrappingWidth(origenCL.getWidth() - 10);
                    this.setWrapText(true);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });
        codigoCL.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descripcionCL.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        descripcionCL.setCellFactory((TableColumn<ModeloBoes, String> arg0) -> new TableCell<ModeloBoes, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    text = new Text(item);
                    text.setWrappingWidth(descripcionCL.getWidth() - 30);
                    this.setWrapText(true);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });

        codigoCL.prefWidthProperty().bind(tvBoes.widthProperty().multiply(0.10));
        origenCL.prefWidthProperty().bind(tvBoes.widthProperty().multiply(0.20));
        descripcionCL.prefWidthProperty().bind(tvBoes.widthProperty().multiply(0.67));

        publicacion = FXCollections.observableArrayList();
        tvBoes.setItems(publicacion);
        selectedList = FXCollections.observableArrayList();
        lvSelect.setItems(selectedList);
        discartedList = FXCollections.observableArrayList();
        lvDiscard.setItems(discartedList);
    }

    @FXML
    void keyPressed(KeyEvent event) {
        System.out.println(event.getCode());

        if (event.isControlDown() && event.getCode() == KeyCode.S) {
            event.consume();
//            System.out.println("Se ha pulsado CRTL + S");
            pdfSelect(new ActionEvent());
        } else if (event.isControlDown() && event.getCode() == KeyCode.D) {
            event.consume();
//            System.out.println("Se ha pulsado CRTL + D");
            pdfDiscard(new ActionEvent());

        }
    }

    @FXML
    void pdfDiscard(ActionEvent event) {
        ModeloBoes aux = tvBoes.getSelectionModel().getSelectedItem();

        if (aux != null) {
            aux.setStatus(Status.USER);
            discartedList.add(0, aux);
            publicacion.remove(aux);
            tableFocus();
            setContadores();
        }
    }

    @FXML
    void pdfRecoverD(ActionEvent event) {
        ModeloBoes aux = lvDiscard.getSelectionModel().getSelectedItem();

        if (aux != null) {
            aux.setStatus(Status.PENDING);
            publicacion.add(0, aux);
            discartedList.remove(aux);
            tableFocus();
            setContadores();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Debes seleccionar un elemento.");
            alert.showAndWait();
        }
    }

    @FXML
    void pdfRecoverS(ActionEvent event) {
        ModeloBoes aux = lvSelect.getSelectionModel().getSelectedItem();

        if (aux != null) {
            aux.setStatus(Status.PENDING);
            publicacion.add(0, aux);
            selectedList.remove(aux);
            tableFocus();
            setContadores();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Debes seleccionar un elemento.");
            alert.showAndWait();
        }
    }

    @FXML
    void pdfSelect(ActionEvent event) {
        ModeloBoes aux = tvBoes.getSelectionModel().getSelectedItem();

        if (aux != null) {
            aux.setStatus(Status.USER);
            selectedList.add(0, aux);
            publicacion.remove(aux);
            tableFocus();
            setContadores();
        }
    }

    @FXML
    void pdfSelectAll(ActionEvent event) {
        ModeloBoes aux;
        Iterator it;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("SELECCIONAR TODOS");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea SELECCIONAR TODOS?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            it = publicacion.iterator();

            while (it.hasNext()) {
                aux = (ModeloBoes) it.next();
                aux.setStatus(Status.USER);
                selectedList.add(0, aux);
            }
            publicacion.clear();
            setContadores();
        }
    }
    
    @FXML
    void pdfShow(ActionEvent event){
        
    }

    @FXML
    void pdfShowOnWeb(ActionEvent event) {
        ModeloBoes aux = tvBoes.getSelectionModel().getSelectedItem();
        try {
            Desktop.getDesktop().browse(new URI(aux.getLink()));
        } catch (IOException | URISyntaxException ex) {
            LOG.error("[pdfShowOnWeb]" + ex);
        }
    }

    @FXML
    void procesar(ActionEvent event) {
        if (publicacion.isEmpty()) {
            Var.boesIsClasificando = false;
            procesarTask();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ACEPTAR BOLETINES");
            alert.setHeaderText("Todavía quedan Boletines sin clasificar");
            alert.setContentText("¿Desea CONTINUAR?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Var.boesIsClasificando = false;
                procesarTask();
            }
        }
    }

    void procesarTask() {
        Thread a = new Thread(() -> {

            Platform.runLater(() -> {
                setProcesandoC(true);
                pbClasificacion.setProgress(-1);
                lbClasificacion.setText("INICIANDO");
            });

            ModeloBoes aux;
            Insercion in = new Insercion();

            Platform.runLater(() -> {
                lbClasificacion.setText("LIMPIANDO DUPLICADOS (Selected)");
            });
            List list = in.cleanDuplicateS(this.selectedList);

            Platform.runLater(() -> {
                lbClasificacion.setText("LIMPIANDO DUPLICADOS (Discarted)");
            });
            List listD = in.cleanDuplicateD(this.discartedList);

            Platform.runLater(() -> {
                lbClasificacion.setText("GUARDANDO ESTADÍSTICAS (Selected)");
            });

            in.guardaStatsS(list);

            Platform.runLater(() -> {
                lbClasificacion.setText("GUARDANDO ESTADÍSTICAS (Discarted)");
            });

            in.guardaStatsD(listD);

            Platform.runLater(() -> {
                pbClasificacion.setProgress(0);
                lbClasificacion.setText("INICIANDO CARGA DE BOLETINES");
            });

            for (int i = 0; i < list.size(); i++) {
                final int contador = i;
                final int total = list.size();

                Platform.runLater(() -> {
                    int contadour = contador + 1;
                    double counter = contador + 1;
                    double toutal = total;
                    lbClasificacion.setText("INSERTANDO BOLETÍN " + contadour + " de " + total);
                    pbClasificacion.setProgress(counter / toutal);
                });

                aux = (ModeloBoes) list.get(i);
                in.insertaBoletin(aux);
            }

            Platform.runLater(() -> {
                lbClasificacion.setText("INSERCIÓN FINALIZADA");
                pbClasificacion.setProgress(-1);
                lbClasificacion.setText("INICIANDO DESCARGA");
            });

            Descarga des;
            Download dw = new Download();
            List listDes = dw.getListado();

            for (int i = 0; i < listDes.size(); i++) {
                final int contador = i;
                final int total = listDes.size();
                Platform.runLater(() -> {
                    int contadour = contador + 1;
                    double counter = contador + 1;
                    double toutal = total;
                    lbClasificacion.setText("DESCARGANDO ARCHIVO " + contadour + " de " + total);
                    pbClasificacion.setProgress(counter / toutal);
                });
                des = (Descarga) listDes.get(i);
                dw.descarga(des);
            }

            Platform.runLater(() -> {
                lbClasificacion.setText("DESCARGA FINALIZADA");
                pbClasificacion.setProgress(-1);
                lbClasificacion.setText("INICIANDO");
            });

            Date fecha = Dates.asDate(dpFechaC.getValue());

            if (fecha != null) {
                Boletin bol;
                Limpieza li;
                List listLi = Query.listaBoletin("SELECT * FROM boes.boletin where idBoe="
                        + "(SELECT id FROM boes.boe where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(fecha)) + ")");

                for (int i = 0; i < listLi.size(); i++) {
                    final int contador = i;
                    final int total = listLi.size();
                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador + 1;
                        double toutal = total;
                        lbClasificacion.setText("LIMPIANDO BOLETIN " + contadour + " de " + total);
                        pbClasificacion.setProgress(counter / toutal);
                    });
                    bol = (Boletin) listLi.get(i);
                    li = new Limpieza(bol);
                    li.run();
                }
            }

            Platform.runLater(() -> {
                setProcesandoC(false);
                lbClasificacion.setText("LIMPIEZA FINALIZADA");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("COMPLETADO");
                alert.setHeaderText("PROCESO FINALIZADO");
                alert.setContentText("SE HA FINALIZADO LA INSERCIÓN, DESCARGA Y LIMPIEZA");
                alert.showAndWait();

                initializeClear();
            });
        });
        a.start();
    }

    void setContadores() {
        Platform.runLater(() -> {
            lbContadorT.setText(Integer.toString(publicacion.size()));
            tpDescartados.setText("Boletines Descartados - " + discartedList.size());
            tpSeleccionados.setText("Boletines Seleccionados - " + selectedList.size());
        });
    }

    void setProcesandoC(boolean aux) {
        lbClasificacion.setVisible(aux);
        pbClasificacion.setVisible(aux);
        btFinClas.setDisable(aux);
        dpFechaC.setDisable(aux);
        btSelect.setDisable(aux);
        btDiscard.setDisable(aux);
        btVerWebC.setDisable(aux);
        btRecargarClasificacion.setDisable(aux);
        btRecoverD.setDisable(aux);
        btRecoverS.setDisable(aux);
        btSelectAll.setDisable(aux);
    }

    void tableFocus() {
        if (autoScroll) {
            tvBoes.getSelectionModel().select(0);
            tvBoes.scrollTo(0);
            tvBoes.requestFocus();
        }
    }

    void tableLoadData(List lista) {
        publicacion.clear();
        Pdf aux;
        ModeloBoes model;
        Iterator it = lista.iterator();

        while (it.hasNext()) {
            aux = (Pdf) it.next();
            model = new ModeloBoes();
            model.origen.set(aux.getOrigen());
            model.entidad.set(aux.getEntidad());
            model.fecha.set(Dates.imprimeFecha(aux.getFecha()));
            model.codigo.set(aux.getCodigo());
            model.descripcion.set(aux.getDescripcion());
            model.link.set(aux.getLink());

            publicacion.add(model);
        }
        tableUpdate();
    }

    void tableUpdate() {
        ModeloBoes aux;
        ObservableList<ModeloBoes> dList = FXCollections.observableArrayList();
        ObservableList<ModeloBoes> sList = FXCollections.observableArrayList();
        Iterator it = publicacion.iterator();

        while (it.hasNext()) {
            aux = (ModeloBoes) it.next();

//            if (discardSource.contains(aux.getOrigen())) {
//                aux.setStatus(Status.SOURCE);
//                dList.add(aux);
//            } else if (isTextDiscarted(aux.getDescripcion())) {
//                aux.setStatus(Status.APP);
//                dList.add(aux);
//            } else if (selectAlready.contains(aux.getCodigo())) {
//                aux.setStatus(Status.DUPLICATED);
//                sList.add(aux);
//            } else if (isTextSelected(aux.getDescripcion())) {
//                aux.setStatus(Status.APP);
//                sList.add(aux);
//            }
        }

        it = dList.iterator();

        while (it.hasNext()) {
            aux = (ModeloBoes) it.next();

            if (publicacion.contains(aux)) {
                publicacion.remove(aux);
            }
        }

        it = sList.iterator();

        while (it.hasNext()) {
            aux = (ModeloBoes) it.next();

            if (publicacion.contains(aux)) {
                publicacion.remove(aux);
            }
        }

        Platform.runLater(() -> {
            tableFocus();
            discartedList.clear();
            discartedList.addAll(dList);
            selectedList.clear();
            selectedList.addAll(sList);
            lbContadorT.setText(Integer.toString(publicacion.size()));
            tpDescartados.setText("Boletines Descartados - " + discartedList.size());
            tpSeleccionados.setText("Boletines Seleccionados - " + selectedList.size());
        });
    }

    @FXML
    void xLisCheckBox(ActionEvent event) {
        autoScroll = cbAutoScroll.isSelected();
    }

    @FXML
    void xLisDatePicker(ActionEvent event) {
        lbContadorT.setText("...");
        tpDescartados.setText("Boletines Descartados");
        tpSeleccionados.setText("Boletines Seleccionados");
        publicacion.clear();
        selectedList.clear();
        discartedList.clear();

        Date aux = Dates.asDate(dpFechaC.getValue());

        if (aux != null) {
            //TODO load DATA.
        }
    }

    private void xLisDatePickerRun(Boe aux) {
        Thread a = new Thread(() -> {

            Platform.runLater(() -> {
                rootPane.getScene().setCursor(Cursor.WAIT);
            });

//            initializeBoesData(aux);
            Var.boesIsClasificando = true;

            Platform.runLater(() -> {
                rootPane.getScene().setCursor(Cursor.DEFAULT);
            });
        });
        a.start();
    }
}
