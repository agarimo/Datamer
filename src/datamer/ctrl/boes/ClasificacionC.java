package datamer.ctrl.boes;

import datamer.Var;
import datamer.ctrl.boes.boe.Insercion;
import datamer.model.boes.ModeloBoes;
import datamer.model.boes.Status;
import datamer.model.boes.enty.Publicacion;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import tools.LoadFile;
import java.awt.Desktop;
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
import sql.Sql;
import tools.Util;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class ClasificacionC implements Initializable {

    private static final Logger LOG = LogManager.getLogger(ClasificacionC.class);

    boolean autoScroll;

    private int selectedCount = 0;
    private int discartedCount = 0;

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
    private Button btVerBoletinC;
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
        GlyphsDude.setIcon(btVerBoletinC, MaterialIcon.FIND_IN_PAGE, "32");
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

        tvBoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        codigoCL.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 50% width
        origenCL.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 30% width
        descripcionCL.setMaxWidth(1f * Integer.MAX_VALUE * 70); // 20% width

//        codigoCL.prefWidthProperty().bind(tvBoes.widthProperty().multiply(0.10));
//        origenCL.prefWidthProperty().bind(tvBoes.widthProperty().multiply(0.20));
//        descripcionCL.prefWidthProperty().bind(tvBoes.widthProperty().multiply(0.67));

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
            discartedCount++;
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
            discartedCount--;
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
            aux.setSelected(false);
            publicacion.add(0, aux);
            selectedList.remove(aux);
            selectedCount--;
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
            aux.setSelected(true);
            selectedList.add(0, aux);
            publicacion.remove(aux);
            selectedCount++;
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
                selectedCount++;
            }
            publicacion.clear();
            setContadores();
        }
    }

    @FXML
    void pdfShow(ActionEvent event) {
        ModeloBoes aux = tvBoes.getSelectionModel().getSelectedItem();
        LoadFile.writeFile(Var.temporal, Query.getPublicacionData(aux.getCodigo()));
        try {
            Desktop.getDesktop().browse(Var.temporal.toURI());
        } catch (IOException ex) {
            LOG.error("[pdfShowOnWeb]" + ex);
        }
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
            Alert alert = new Alert(AlertType.INFORMATION);
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

            Publicacion aux;
            LocalDate fecha = dpFechaC.getValue();
            String query = "SELECT * FROM " + Var.dbNameServer + ".publicacion WHERE fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)) + " and selected=true;";

            Platform.runLater(() -> {
                setProcesandoC(true);
                pbClasificacion.setProgress(-1);
                lbClasificacion.setText("INICIANDO");
            });

            Insercion in = new Insercion();

            Platform.runLater(() -> {
                lbClasificacion.setText("GUARDANDO SELECCIÓN");
            });

            in.guardaStatsS(selectedList);
            in.guardaStatsD(discartedList);

            Platform.runLater(() -> {
                selectedList.clear();
                discartedList.clear();
                pbClasificacion.setProgress(-1);
                lbClasificacion.setText("INICIANDO CARGA");
            });

            procesarTaskPreClean(fecha);
            List list = Query.listaPublicacion(query);

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

                aux = (Publicacion) list.get(i);
                in.insertaBoletin(aux);
            }

            Platform.runLater(() -> {
                lbClasificacion.setText("INSERCIÓN FINALIZADA");
                pbClasificacion.setProgress(-1);
            });

            in.clean();

            Platform.runLater(() -> {
                setProcesandoC(false);
                lbClasificacion.setText("INSERCIÓN FINALIZADA");
                pbClasificacion.setProgress(-1);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("COMPLETADO");
                alert.setHeaderText("PROCESO FINALIZADO");
                alert.setContentText("SE HA FINALIZADO LA INSERCIÓN, DESCARGA Y LIMPIEZA");
                alert.showAndWait();

                initializeClear();
            });
        });
        Var.executor.execute(a);
    }

    private void procesarTaskPreClean(LocalDate fecha) {
        try {
            String query = "DELETE FROM " + Var.dbNameBoes + ".boletin WHERE idBoe=(SELECT id FROM " + Var.dbNameBoes + ".boe WHERE fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)) + ")";
            Sql bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ClasificacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setContadores() {
        Platform.runLater(() -> {
            lbContadorT.setText(Integer.toString(publicacion.size()));
            tpDescartados.setText("Boletines Descartados - " + discartedCount);
            tpSeleccionados.setText("Boletines Seleccionados - " + selectedCount);
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
        ModeloBoes aux;
        Iterator it = lista.iterator();

        while (it.hasNext()) {
            aux = (ModeloBoes) it.next();

            if (aux.getSelected()) {
                selectedCount++;
                switch (aux.getStatus()) {
                    case USER:
                        selectedList.add(aux);
                        break;
                }

            } else {
                discartedCount++;
                switch (aux.getStatus()) {
                    case PENDING:
                        publicacion.add(aux);

                        break;
                    case USER:
                        discartedList.add(aux);
                        break;
                }
            }
        }
        setContadores();
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

        LocalDate aux = dpFechaC.getValue();

        if (aux != null) {
            Thread a = new Thread(() -> {
                String query = "SELECT fecha,codigo,entidad,origen,descripcion,link,selected,status FROM " + Var.dbNameServer + ".publicacion WHERE fecha=" + Util.comillas(aux.format(DateTimeFormatter.ISO_DATE));

                Platform.runLater(() -> {
                    rootPane.getScene().setCursor(Cursor.WAIT);
                });

                List list = Query.listaModeloBoes(query);
                Var.boesIsClasificando = true;

                Platform.runLater(() -> {
                    tableLoadData(list);
                    rootPane.getScene().setCursor(Cursor.DEFAULT);
                    tableFocus();
                });
            });
            Var.executor.execute(a);
        }

    }
}
