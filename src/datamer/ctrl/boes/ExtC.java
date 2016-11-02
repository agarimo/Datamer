package datamer.ctrl.boes;

import datamer.Var;
import datamer.ctrl.NotasC;
import datamer.ctrl.StrucDataC;
import datamer.ctrl.boes.ext.BB0;
import datamer.ctrl.boes.ext.INS;
import datamer.ctrl.boes.ext.Extraccion;
import datamer.ctrl.boes.ext.TXT;
import datamer.ctrl.boes.ext.script.ScriptArticulo;
import datamer.ctrl.boes.ext.script.ScriptExp;
import datamer.ctrl.boes.ext.script.ScriptFase;
import datamer.ctrl.boes.ext.script.ScriptOrigen;
import datamer.ctrl.boes.ext.script.ScriptReq;
import datamer.ctrl.boes.ext.XLSXProcess;
import datamer.model.boes.Estado;
import datamer.model.boes.ModeloPreview;
import datamer.model.boes.ModeloProcesar;
import datamer.model.boes.enty.Multa;
import datamer.model.boes.enty.Procesar;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import tools.Download;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.controlsfx.control.PopOver;
import tools.Dates;
import sql.Sql;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class ExtC implements Initializable {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ExtC.class);

    private LocalDate fecha;
    private Node notas;
    private NotasC notasC;
    private PopOver popOverNotas;
    private Node strucData;
    private StrucDataC strucDataC;
    private PopOver popoverStrucData;

    private List<Integer> listaEstructurasCreadas;
    private List<Integer> listaEstructurasManual;

    private boolean isPreview;
    private final int procesar_to_preview = 1;
    private final int preview_to_procesar = 2;
    private final int procesar_to_wait = 3;
    private final int wait_to_preview = 4;
    private final int wait_to_procesar = 5;
    private final int preview_to_wait = 6;

    private final int ARCHIVOS_ALL = 0;
    private final int ARCHIVOS_REQ = 1;
    private final int ARCHIVOS_FILES = 2;

    private Text icono_new;
    private Text icono_view;

    private List<ModeloProcesar> boletines;
    private ObservableList<ModeloProcesar> procesarList;
    private ObservableList<ModeloPreview> previewList;

    //<editor-fold defaultstate="collapsed" desc="FXML VAR">
    @FXML
    private VBox rootPane;
    @FXML
    private VBox panelProcesar;
    @FXML
    private VBox panelPreview;
    @FXML
    private VBox panelEspera;
    @FXML
    private VBox panelFunciones;
    @FXML
    private VBox panelExtraccion;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private Button btPreview;
    @FXML
    private Button btForzarProcesar;
    @FXML
    private SplitMenuButton btArchivos;
    @FXML
    private SplitMenuButton btProcesar;
    @FXML
    private TableView tvProcesar;
    @FXML
    private TableColumn clCodigo;
    @FXML
    private TableColumn clEstructura;
    @FXML
    private TableColumn clEstado;
    @FXML
    private TableView tvPreview;
    @FXML
    private TableColumn clExpediente;
    @FXML
    private TableColumn clSancionado;
    @FXML
    private TableColumn clNif;
    @FXML
    private TableColumn clLocalidad;
    @FXML
    private TableColumn clFecha;
    @FXML
    private TableColumn clMatricula;
    @FXML
    private TableColumn clCuantia;
    @FXML
    private TableColumn clArticulo;
    @FXML
    private TableColumn clPuntos;
    @FXML
    private TableColumn clReqObs;
    @FXML
    private ProgressIndicator piProgreso;
    @FXML
    private Label lbProceso;
    @FXML
    private Label lbProgreso;
    @FXML
    private Label lbMultasPreview;
    @FXML
    private Label lbNotas;
    @FXML
    private CheckBox cbHide;
    @FXML
    private Label lbRefresh;
    @FXML
    private Button btStrucData;
//</editor-fold>

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isPreview = false;
        panelPreview.setOpacity(0.0);
        panelPreview.setVisible(false);
        panelProcesar.setOpacity(1.0);
        panelProcesar.setVisible(true);
        panelEspera.setOpacity(0.0);
        panelEspera.setVisible(false);
        lbNotas.setVisible(false);
        btStrucData.setVisible(false);
        cbHide.setSelected(true);
        lbRefresh.setVisible(false);
        btForzarProcesar.setVisible(false);
        boletines = new ArrayList();
        initializeIcons();
        iniciarTablaProcesar();
        iniciarTablaPreview();
        iniciarNotas();
        iniciarStrucData();

        final ObservableList<ModeloProcesar> ls1 = tvProcesar.getSelectionModel().getSelectedItems();
        ls1.addListener(selectorTablaProcesar);

        final ObservableList<ModeloPreview> ls2 = tvPreview.getSelectionModel().getSelectedItems();
        ls2.addListener(selectorTablaPreview);
    }

    private void initializeIcons() {
        Text text;

        text = GlyphsDude.createIcon(MaterialIcon.REFRESH, "20");
        text.setFill(Color.DARKORANGE);
        lbRefresh.setGraphic(text);

        icono_new = GlyphsDude.createIcon(MaterialIcon.ADD, "20");
        icono_new.setFill(Color.GREEN);

        icono_view = GlyphsDude.createIcon(MaterialIcon.ATTACH_FILE, "20");
        icono_view.setFill(Color.ORANGERED);
        lbNotas.setGraphic(null);

//        GlyphsDude.setIcon(btSelectAll, MaterialIcon.PLAYLIST_ADD, "32");
//        GlyphsDude.setIcon(btRecoverS, MaterialIcon.INPUT, "32");
//        GlyphsDude.setIcon(btRecoverD, MaterialIcon.INPUT, "32");
//        GlyphsDude.setIcon(btVerBoletinC, MaterialIcon.FIND_IN_PAGE, "32");
    }

    private void showPanel(int panel) {
        FadeTransition fade;

        switch (panel) {
            case procesar_to_preview:
                fade = new FadeTransition(Duration.millis(1000), panelProcesar);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setAutoReverse(false);
                fade.play();
                panelProcesar.setVisible(false);

                panelPreview.setVisible(true);
                fade = new FadeTransition(Duration.millis(1000), panelPreview);
                fade.setFromValue(0.0);
                fade.setToValue(1.0);
                fade.setAutoReverse(false);
                fade.play();

                break;

            case preview_to_procesar:
                fade = new FadeTransition(Duration.millis(1000), panelPreview);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setAutoReverse(false);
                fade.play();
                panelPreview.setVisible(false);

                panelProcesar.setVisible(true);
                fade = new FadeTransition(Duration.millis(1000), panelProcesar);
                fade.setFromValue(0.0);
                fade.setToValue(1.0);
                fade.setAutoReverse(false);
                fade.play();

                break;
            case procesar_to_wait:
                fade = new FadeTransition(Duration.millis(1000), panelProcesar);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setAutoReverse(false);
                fade.play();
                panelProcesar.setVisible(false);

                panelEspera.setVisible(true);
                fade = new FadeTransition(Duration.millis(1000), panelEspera);
                fade.setFromValue(0.0);
                fade.setToValue(1.0);
                fade.setAutoReverse(false);
                fade.play();

                break;

            case wait_to_procesar:
                fade = new FadeTransition(Duration.millis(1000), panelEspera);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setAutoReverse(false);
                fade.play();
                panelEspera.setVisible(false);

                panelProcesar.setVisible(true);
                fade = new FadeTransition(Duration.millis(1000), panelProcesar);
                fade.setFromValue(0.0);
                fade.setToValue(1.0);
                fade.setAutoReverse(false);
                fade.play();

                break;

            case wait_to_preview:
                fade = new FadeTransition(Duration.millis(1000), panelEspera);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setAutoReverse(false);
                fade.play();
                panelEspera.setVisible(false);

                panelPreview.setVisible(true);
                fade = new FadeTransition(Duration.millis(1000), panelPreview);
                fade.setFromValue(0.0);
                fade.setToValue(1.0);
                fade.setAutoReverse(false);
                fade.play();

                break;

            case preview_to_wait:
                fade = new FadeTransition(Duration.millis(1000), panelPreview);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setAutoReverse(false);
                fade.play();
                panelEspera.setVisible(false);

                panelPreview.setVisible(true);
                fade = new FadeTransition(Duration.millis(1000), panelEspera);
                fade.setFromValue(0.0);
                fade.setToValue(1.0);
                fade.setAutoReverse(false);
                fade.play();

                break;
        }
    }

    void switchControls(boolean aux) {
        dpFecha.setDisable(aux);
        btProcesar.setDisable(aux);
        btArchivos.setDisable(aux);
    }

    //<editor-fold defaultstate="collapsed" desc="PROCESO DATEPICKER">
    @FXML
    void cambioEnDatePicker(ActionEvent event) {
        try {
            fecha = dpFecha.getValue();

            if (fecha != null) {
                String query = "SELECT * FROM boes.procesar where fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE));
                lbRefresh.setVisible(true);
                cargarDatos(query);
            }
        } catch (NullPointerException ex) {
            //
        }
    }

    @FXML
    void refresh(MouseEvent event) {
        cambioEnDatePicker(new ActionEvent());
    }

    void cargarDatos(String query) {
        listaEstructurasCreadas = Query.listaEstructurasCreadas();
        listaEstructurasManual = Query.listaEstructurasManual();
        boletines.clear();

        Procesar procesar;
        ModeloProcesar modelo;
        List<Procesar> list = Query.listaProcesar(query);
        Iterator<Procesar> it = list.iterator();

        while (it.hasNext()) {
            procesar = it.next();

            modelo = new ModeloProcesar();
            modelo.id.set(procesar.getId());
            modelo.codigo.set(procesar.getCodigo());
            modelo.estructura.set(procesar.getEstructura());
            if (listaEstructurasManual.contains(procesar.getEstructura()) && procesar.getEstado() != Estado.PROCESADO_XLSX) {
                modelo.estado.set(Estado.PROCESAR_MANUAL.getValue());
            } else {
                modelo.estado.set(procesar.getEstado().getValue());
            }
            modelo.link.set(procesar.getLink());
            modelo.fecha.set(procesar.getFecha().format(DateTimeFormatter.ISO_DATE));

            boletines.add(modelo);
        }

        cargarDatosProcesar();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROCESO TABLA PROCESAR">
    private void iniciarTablaProcesar() {
        clCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        clCodigo.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, String>() {
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
                    }
                }
            };
        });
        clEstructura.setCellValueFactory(new PropertyValueFactory<>("estructura"));
        clEstructura.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setAlignment(Pos.CENTER);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText("");

                        switch (item) {
                            case -1:
                                setText("Sin estructura");
                                setTextFill(Color.RED);
                                break;
                            default:
                                if (listaEstructurasCreadas.contains(item)) {
                                    setText(Integer.toString(item));
                                    setTextFill(Color.GREEN);
                                } else {
                                    setText("STRUCDATA no creado : " + Integer.toString(item));
                                    setTextFill(Color.ORANGE);
                                }
                                break;
                        }
                    }
                }
            };
        });
        clEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        clEstado.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setAlignment(Pos.CENTER);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {

                        switch (item) {
                            case 0:
                                setText(Estado.SIN_PROCESAR.toString());
                                setTextFill(Color.BLACK);
                                break;

                            case 1:
                                setText(Estado.LISTO_PROCESAR.toString());
                                setTextFill(Color.ORCHID);
                                break;

                            case 2:
                                setText(Estado.PROCESADO_XLSX.toString());
                                setTextFill(Color.GREEN);
                                break;

                            case 3:
                                setText(Estado.ERROR_PROCESAR.toString());
                                setTextFill(Color.RED);
                                break;

                            case 4:
                                setText(Estado.PDF_NO_GENERADO.toString());
                                setTextFill(Color.GRAY);
                                break;

                            case 5:
                                setText(Estado.XLSX_NO_GENERADO.toString());
                                setTextFill(Color.GRAY);
                                break;
                            case 6:
                                setText(Estado.PROCESAR_MANUAL.toString());
                                setTextFill(Color.BLUE);
                        }
                    }
                }
            };
        });

        tvProcesar.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clCodigo.setMaxWidth(1f * Integer.MAX_VALUE * 40);
        clEstructura.setMaxWidth(1f * Integer.MAX_VALUE * 40);
        clEstado.setMaxWidth(1f * Integer.MAX_VALUE * 20);

        procesarList = FXCollections.observableArrayList();
        tvProcesar.setItems(procesarList);
    }

    void cargarDatosProcesar() {
        procesarList.clear();

        if (cbHide.isSelected()) {
            procesarList.addAll(boletines.stream()
                    .filter(bol -> bol.getEstado() != Estado.PROCESADO_XLSX.getValue())
                    .collect(Collectors.toList()));
        } else {
            procesarList.addAll(boletines);
        }

    }

    private List<ModeloProcesar> getBoletinesProcesar() {
        ModeloProcesar modelo;
        List<ModeloProcesar> list = new ArrayList();
        Iterator<ModeloProcesar> it = procesarList.iterator();

        while (it.hasNext()) {
            modelo = it.next();

            if (modelo.getEstado() == 1 && listaEstructurasCreadas.contains(modelo.getEstructura())) {
                list.add(modelo);
            }
        }
        return list;
    }

    @FXML
    void hideProcessed(ActionEvent event) {
        cargarDatosProcesar();
    }

    @FXML
    void generarPDF(ActionEvent event) {
        if (fecha != null) {
            String query = "SELECT * FROM " + Var.dbNameBoes + ".procesar where fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE));
            File fichero = new File(Var.fileRemote, fecha.format(DateTimeFormatter.ISO_DATE));
            fichero.mkdirs();

            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    showPanel(this.procesar_to_wait);
                    piProgreso.setProgress(0);
                    lbProgreso.setText("");
                    lbProceso.setText("GENERANDO PDFs");
                });

                File destino;
                Procesar aux;
                List list = Query.listaProcesar(query);

                for (int i = 0; i < list.size(); i++) {
                    final int contador = i;
                    final int total = list.size();
                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador;
                        double toutal = total;
                        lbProgreso.setText("DESCARGANDO " + contadour + " de " + total);
                        piProgreso.setProgress(counter / toutal);
                    });
                    aux = (Procesar) list.get(i);
                    destino = new File(fichero, aux.getCodigo() + ".pdf");

                    try {
                        Download.downloadFILE(aux.getLink(), destino);

                    } catch (IOException ex) {
                        Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    aux.SQLSetEstado(Estado.LISTO_PROCESAR.getValue());
                }

                Platform.runLater(() -> {
                    piProgreso.setProgress(1);
                    lbProgreso.setText("");
                    lbProceso.setText("");
                    showPanel(this.wait_to_procesar);

                    cambioEnDatePicker(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    @FXML
    void generarSinglePDF(ActionEvent event) {
        ModeloProcesar pr = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();

        if (fecha != null && pr != null) {
            File fichero = new File(Var.fileRemote, fecha.format(DateTimeFormatter.ISO_DATE));
            fichero.mkdirs();

            Thread a = new Thread(() -> {
                File destino = new File(fichero, pr.getCodigo() + ".pdf");

                try {
                    Download.downloadFILE(pr.getLink(), destino);
                } catch (IOException ex) {
                    Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
                }

                Platform.runLater(() -> {
                    cambioEnDatePicker(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    @FXML
    void openDataFolder(ActionEvent event) {
        if (fecha != null) {
            File fichero = new File(Var.fileRemote, fecha.format(DateTimeFormatter.ISO_DATE));
            try {
                Desktop.getDesktop().browse(fichero.toURI());
            } catch (IOException ex) {
                LOG.error("[openDataFolder]" + ex);
            }
        }
    }

    @FXML
    void openXLSX(ActionEvent event) {
        ModeloProcesar pr = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();

        if (pr != null && fecha != null) {
            try {
                Desktop.getDesktop().browse(pr.getXLSX(Var.fileRemote).toURI());
            } catch (IOException ex) {
                LOG.error("[verXLSX]" + ex);
            }
        }
    }

    @FXML
    void openPDF(ActionEvent event) {
        ModeloProcesar pr = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();

        if (pr != null) {
            try {
                Desktop.getDesktop().browse(new URI(pr.link.get()));
            } catch (IOException | URISyntaxException ex) {
                LOG.error("[verPdf]" + ex);
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROCESO TABLA PREVIEW">
    private void iniciarTablaPreview() {
        clExpediente.setCellValueFactory(new PropertyValueFactory<>("expediente"));
        clSancionado.setCellValueFactory(new PropertyValueFactory<>("sancionado"));
        clNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        clNif.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, String>() {
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
                    }
                }
            };
        });
        clLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        clLocalidad.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, String>() {
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
                    }
                }
            };
        });
        clFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        clFecha.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, String>() {
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
                    }
                }
            };
        });
        clMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        clMatricula.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, String>() {
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
                    }
                }
            };
        });
        clCuantia.setCellValueFactory(new PropertyValueFactory<>("cuantia"));
        clCuantia.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, String>() {
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
                    }
                }
            };
        });
        clArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        clPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        clPuntos.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, String>() {
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
                    }
                }
            };
        });
        clReqObs.setCellValueFactory(new PropertyValueFactory<>("reqObs"));
        clReqObs.setCellFactory(column -> {
            return new TableCell<ModeloProcesar, String>() {
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
                    }
                }
            };
        });

        tvPreview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clExpediente.setMaxWidth(1f * Integer.MAX_VALUE * 9);
        clSancionado.setMaxWidth(1f * Integer.MAX_VALUE * 27);
        clNif.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        clLocalidad.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        clFecha.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        clMatricula.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        clCuantia.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        clArticulo.setMaxWidth(1f * Integer.MAX_VALUE * 8);
        clPuntos.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        clReqObs.setMaxWidth(1f * Integer.MAX_VALUE * 5);

        previewList = FXCollections.observableArrayList();
        tvPreview.setItems(previewList);
    }

    void cargarDatosPreview(List<Multa> list) {
        previewList.clear();
        ModeloPreview modelo;
        Multa multa;
        Iterator<Multa> it = list.iterator();

        while (it.hasNext()) {
            multa = it.next();

            if (!multa.equals(new Multa())) {
                modelo = new ModeloPreview();
                modelo.setMulta(multa);
                modelo.expediente.set(multa.getExpediente());
                modelo.sancionado.set(multa.getSancionado());
                modelo.nif.set(multa.getNif());
                modelo.localidad.set(multa.getLocalidad());
                modelo.fecha.set(Dates.imprimeFecha(multa.getFechaMulta()));
                modelo.matricula.set(multa.getMatricula());
                modelo.cuantia.set(multa.getCuantia());
                modelo.articulo.set(multa.getArticulo());
                modelo.puntos.set(multa.getPuntos());
                modelo.reqObs.set(multa.getReqObs());

                previewList.add(modelo);
            }
        }
        lbMultasPreview.setText(Integer.toString(previewList.size()));
    }

    @FXML
    void eliminarLineaPreview(ActionEvent event) {
        ModeloPreview aux = (ModeloPreview) tvPreview.getSelectionModel().getSelectedItem();

        if (aux != null) {
            previewList.remove(aux);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROCESO NOTAS">
    private void iniciarNotas() {
        try {
            FXMLLoader loader = new FXMLLoader();
            notas = loader.load(getClass().getResourceAsStream("/datamer/view/Notas.fxml"));
            notasC = loader.getController();
            notasC.setParentController(this);
        } catch (IOException ex) {
            Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void verNotas(MouseEvent event) {
        popOverNotas = new PopOver();
        popOverNotas.setDetachable(false);
        popOverNotas.setDetached(false);
        popOverNotas.arrowSizeProperty().setValue(12);
        popOverNotas.arrowIndentProperty().setValue(13);
        popOverNotas.arrowLocationProperty().setValue(PopOver.ArrowLocation.BOTTOM_CENTER);
        popOverNotas.cornerRadiusProperty().setValue(7);
        popOverNotas.headerAlwaysVisibleProperty().setValue(false);
        popOverNotas.setAnimated(true);

        popOverNotas.setContentNode(notas);
        popOverNotas.show(lbNotas);
    }

    public void cerrarNotas() {
        popOverNotas.hide();
        tvProcesar.getSelectionModel().clearSelection();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROCESO STRUCDATA">
    private void iniciarStrucData() {
        try {
            FXMLLoader loader = new FXMLLoader();
            strucData = loader.load(getClass().getResourceAsStream("/datamer/view/StrucData.fxml"));
            strucDataC = loader.getController();
            strucDataC.setParentController(this);
        } catch (IOException ex) {
            Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void verStrucData(ActionEvent event) {
        popoverStrucData = new PopOver();
        popoverStrucData.setDetachable(false);
        popoverStrucData.setDetached(false);
        popoverStrucData.arrowSizeProperty().setValue(12);
        popoverStrucData.arrowIndentProperty().setValue(13);
        popoverStrucData.arrowLocationProperty().setValue(PopOver.ArrowLocation.BOTTOM_CENTER);
        popoverStrucData.cornerRadiusProperty().setValue(7);
        popoverStrucData.headerAlwaysVisibleProperty().setValue(false);
        popoverStrucData.setAnimated(true);

        popoverStrucData.setContentNode(strucData);
        popoverStrucData.show(btStrucData);
    }

    public void cerrarStrucData() {
        popoverStrucData.hide();
        tvProcesar.getSelectionModel().clearSelection();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROCESO EXTRACCION">
    @FXML
    void previsualizar(ActionEvent event) {
        if (isPreview) {
            btPreview.setText("Previsualizar Extracción");
            btForzarProcesar.setVisible(false);
            panelFunciones.setVisible(true);
            panelFunciones.setManaged(true);
            showPanel(this.preview_to_procesar);
            isPreview = !isPreview;
            switchControls(false);
        } else {
            ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();
            Extraccion ex;

            if (fecha != null || aux != null) {
                ex = new Extraccion(fecha);
                if (ex.fileExist(aux.getCodigo())) {
                    if (listaEstructurasCreadas.contains(aux.getEstructura())) {

                        Thread a = new Thread(() -> {
                            List<Multa> procesados;

                            Platform.runLater(() -> {
                                switchControls(true);
                                piProgreso.setProgress(-1);
                                lbProgreso.setText("");
                                lbProceso.setText("PROCESANDO PREVISUALIZACIÓN");
                                btPreview.setText("Volver");
                                btForzarProcesar.setVisible(true);
                                panelFunciones.setVisible(false);
                                panelFunciones.setManaged(false);
                                showPanel(this.procesar_to_wait);
                                isPreview = !isPreview;
                            });

                            try {
                                procesados = ex.previewXLSX(Query.getProcesar(aux.getCodigo()));

                                Platform.runLater(() -> {
                                    cargarDatosPreview(procesados);
                                    piProgreso.setProgress(1);
                                    lbProgreso.setText("");
                                    lbProceso.setText("");
                                    showPanel(this.wait_to_preview);
                                });

                            } catch (Exception e) {
                                Platform.runLater(() -> {
                                    piProgreso.setProgress(1);
                                    lbProgreso.setText("");
                                    lbProceso.setText("");

                                    btPreview.setText("Previsualizar Extracción");
                                    btForzarProcesar.setVisible(false);
                                    panelFunciones.setVisible(true);
                                    panelFunciones.setManaged(true);
                                    showPanel(this.preview_to_procesar);
                                    isPreview = !isPreview;
                                    switchControls(false);

                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("ERROR");
                                    alert.setHeaderText("XLSX CON ERRORES");
                                    alert.setContentText("El XLSX seleccionado contiene errores de estructura \n"
                                            + e.getMessage());
                                    alert.showAndWait();
                                });
                            }
                        });
                        Var.executor.execute(a);

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("STRUCDATA NO CREADO");
                        alert.setContentText("Debe crear el STRUCDATA para previsualizar el boletín");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("FICHERO NO ENCONTRADO");
                    alert.setContentText("Debe generar el fichero .xlsx para previsualizar el contenido.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR");
                alert.setHeaderText("ERROR EN SELECCIÓN");
                alert.setContentText("Debe seleccionar un boletín para su visualización");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void forzarProcesado(ActionEvent event) {
        List<Multa> list = new ArrayList();
        ModeloPreview mp;
        Iterator<ModeloPreview> it = previewList.iterator();

        while (it.hasNext()) {
            mp = it.next();

            if (!mp.getMulta().equals(new Multa())) {
                list.add(mp.getMulta());
            }
        }

        Thread a = new Thread(() -> {

            Platform.runLater(() -> {
                rootPane.getScene().setCursor(Cursor.WAIT);
                btForzarProcesar.setDisable(true);
                btForzarProcesar.setText("PROCESANDO");
                btPreview.setDisable(true);
            });

            Procesar pr;
            ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();
            pr = Query.getProcesar(aux.getCodigo());

            try {
                Query.eliminarMultasBoletin(aux.getCodigo());
                XLSXProcess.insertMultas(list);
                pr.SQLSetEstado(Estado.PROCESADO_XLSX.getValue());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(aux.getCodigo());
                pr.SQLSetEstado(Estado.ERROR_PROCESAR.getValue());
            }

            Platform.runLater(() -> {
                rootPane.getScene().setCursor(Cursor.DEFAULT);
                btForzarProcesar.setDisable(false);
                btForzarProcesar.setText("Procesar");
                btPreview.setDisable(false);
                btPreview.setText("Previsualizar Extracción");
                btForzarProcesar.setVisible(false);
                panelFunciones.setVisible(true);
                panelFunciones.setManaged(true);
                showPanel(this.preview_to_procesar);
                isPreview = !isPreview;
                switchControls(false);
                cambioEnDatePicker(new ActionEvent());
            });
        });
        Var.executor.execute(a);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROCESO FUNCIONES">
    @FXML
    void procesar(ActionEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("PROCESAR EXTRACCIÓN");
        alert.setHeaderText("Se va a procesar el día completo.");
        alert.setContentText("¿Desea CONTINUAR?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            procesarRun();
        }
    }

    private void procesarRun() {
        cambioEnDatePicker(new ActionEvent());
        List<ModeloProcesar> list = getBoletinesProcesar();

        if (!list.isEmpty() && fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    showPanel(this.procesar_to_wait);
                    piProgreso.setProgress(0);
                    lbProgreso.setText("");
                    lbProceso.setText("PREPARANDO BBDD");
                });

                procesarRunPreClean();

                Platform.runLater(() -> {
                    lbProgreso.setText("");
                    lbProceso.setText("PROCESANDO BOLETINES");
                });

                Extraccion ex = new Extraccion(fecha);
                List<Multa> procesado;
                ModeloProcesar aux;
                Procesar pr;

                for (int i = 0; i < list.size(); i++) {
                    final int contador = i;
                    final int total = list.size();

                    Platform.runLater(() -> {
                        int contadour = contador + 1;
                        double counter = contador;
                        double toutal = total;
                        lbProgreso.setText("PROCESANDO " + contadour + " de " + total);
                        piProgreso.setProgress(counter / toutal);
                    });

                    aux = (ModeloProcesar) list.get(i);
                    pr = Query.getProcesar(aux.getCodigo());

                    try {
                        procesado = ex.previewXLSX(pr);

                        if (procesado.contains(new Multa())) {
                            pr.SQLSetEstado(Estado.ERROR_PROCESAR.getValue());
                        } else {
                            Platform.runLater(() -> {
                                lbProgreso.setText("INSERTANDO MULTAS");
                            });
                            XLSXProcess.insertMultas(procesado);
                            pr.SQLSetEstado(Estado.PROCESADO_XLSX.getValue());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getClass());
                        LOG.error("[procesarRun] (" + e.getClass() + ") -" + e.getMessage());
                        pr.SQLSetEstado(Estado.ERROR_PROCESAR.getValue());
                    }
                }

                Platform.runLater(() -> {
                    piProgreso.setProgress(1);
                    lbProgreso.setText("");
                    lbProceso.setText("");
                    showPanel(this.wait_to_procesar);

                    cambioEnDatePicker(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    private void procesarRunPreClean() {
        String queryMultas = "DELETE FROM " + Var.dbNameBoes + ".multa WHERE fechaPublicacion=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE));
        String queryProcesar = "UPDATE " + Var.dbNameBoes + ".procesar SET estado=1 WHERE fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE));

        try {
            Sql bd = new Sql(Var.con);
            bd.ejecutar(queryMultas);
            bd.ejecutar(queryProcesar);
            bd.close();
            cambioEnDatePicker(new ActionEvent());

        } catch (SQLException ex) {
            Logger.getLogger(ExtC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void eliminarEstructura(ActionEvent event) {
        ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();
        Procesar pr = Query.getProcesar(aux.getCodigo());
        Query.eliminarMultasBoletin(pr.getCodigo());
        pr.SQLEliminarEstructura();
        cambioEnDatePicker(new ActionEvent());
    }

    @FXML
    void eliminarBoletin(ActionEvent event) {
        ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();
        Query.eliminaBoletin(aux.getCodigo());
        cambioEnDatePicker(new ActionEvent());
    }

    @FXML
    void resetearEstado(ActionEvent event) {
        ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();
        Procesar pr = Query.getProcesar(aux.getCodigo());
        Query.eliminarMultasBoletin(pr.getCodigo());
        pr.SQLSetEstado(Estado.LISTO_PROCESAR.getValue());
        cambioEnDatePicker(new ActionEvent());
    }

    @FXML
    void resetData(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("RESETEAR DATOS");
        alert.setHeaderText("Se reinicializaran los datos de " + fecha.format(DateTimeFormatter.ISO_DATE));
        alert.setContentText("¿Desea CONTINUAR?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Query.resetData(fecha);
            cambioEnDatePicker(new ActionEvent());
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROCESO ARCHIVOS">
    @FXML
    void runArchivosAll(ActionEvent event) {
        File fichero = fileChooser();
        if (fichero != null && fecha != null) {
            fichero = new File(fichero, fecha.format(DateTimeFormatter.ISO_DATE));
            runArchivos(this.ARCHIVOS_ALL, fichero);
        }
    }

    @FXML
    void runArchivosReq(ActionEvent event) {
        runArchivos(this.ARCHIVOS_REQ, null);
    }

    @FXML
    void runArchivosFiles(ActionEvent event) {
        File fichero = fileChooser();
        if (fichero != null && fecha != null) {
            fichero = new File(fichero, fecha.format(DateTimeFormatter.ISO_DATE));
            runArchivos(this.ARCHIVOS_FILES, fichero);
        }
    }

    private File fileChooser() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(new File(System.getProperty("user.home")), "Desktop"));
        return chooser.showDialog(Var.stage);
    }

    private void runArchivos(int mode, File fichero) {
        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    showPanel(this.procesar_to_wait);
                    piProgreso.setProgress(-1);
                    lbProgreso.setText("");
                    lbProceso.setText("...INICIANDO...");
                });

                fichero.mkdirs();

                switch (mode) {
                    case 0:
                        runScript();
                        runFiles(fichero);
                        break;
                    case 1:
                        runScript();
                        break;
                    case 2:
                        runFiles(fichero);
                        break;
                }

                Platform.runLater(() -> {
                    piProgreso.setProgress(1);
                    lbProgreso.setText("");
                    lbProceso.setText("");
                    showPanel(this.wait_to_procesar);
                });

                try {
                    tools.Util.openFile(fichero);
                } catch (IOException ex) {
                    Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
            Var.executor.execute(a);
        }
    }

    private void runScript() {
        Platform.runLater(() -> {
            lbProceso.setText("PRE-PROCESANDO DATOS");
            lbProgreso.setText("EJECUTANDO SCRIPT REQUERIMIENTOS");
        });
        ScriptReq sr = new ScriptReq(fecha);
        sr.run();

        Platform.runLater(() -> {
            lbProgreso.setText("EJECUTANDO SCRIPT EXPEDIENTE");
        });

        ScriptExp se = new ScriptExp(fecha);
        se.run();

        Platform.runLater(() -> {
            lbProgreso.setText("EJECUTANDO SCRIPT FASE");
        });
        ScriptFase sf = new ScriptFase();
        sf.run();

        Platform.runLater(() -> {
            lbProgreso.setText("EJECUTANDO SCRIPT ORIGEN");
        });
        ScriptOrigen so = new ScriptOrigen(fecha);
        so.run();

        Platform.runLater(() -> {
            lbProgreso.setText("EJECUTANDO SCRIPT ARTICULO");
        });
        ScriptArticulo sa = new ScriptArticulo();
        sa.run();
    }

    private void runFiles(File fichero) {
        Platform.runLater(() -> {
            lbProceso.setText("GENERANDO ARCHIVOS");
            lbProgreso.setText("Generando BB0");
        });
        BB0 bb = new BB0(fecha, fichero);
        bb.run();

        Platform.runLater(() -> {
            lbProgreso.setText("Generando INS");
        });
        INS ins = new INS(fecha, fichero);
        ins.run();

        Platform.runLater(() -> {
            lbProgreso.setText("Generando TXT");
        });
        TXT txt = new TXT(fecha, fichero);
        txt.run();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LISTENER">
    /**
     * Listener de la Tabla Procesar
     */
    private final ListChangeListener<ModeloProcesar> selectorTablaProcesar
            = (ListChangeListener.Change<? extends ModeloProcesar> c) -> {
                ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();

                if (aux != null) {
                    if (aux.getEstructura() == -1) {
                        btStrucData.setVisible(false);
                        lbNotas.setVisible(false);
                    } else {
                        btStrucData.setVisible(true);
                        lbNotas.setVisible(true);

                        strucDataC.loadData(aux.getEstructura());

                        if (notasC.setNota(aux.getEstructura())) {
                            lbNotas.setGraphic(icono_view);
                        } else {
                            lbNotas.setGraphic(icono_new);
                        }
                    }
                } else {
                    lbNotas.setVisible(false);
                }

            };

    /**
     * Listener de la Tabla Preview
     */
    private final ListChangeListener<ModeloPreview> selectorTablaPreview
            = (ListChangeListener.Change<? extends ModeloPreview> c) -> {
//                cargaDatosFase();
            };
//</editor-fold>
}
