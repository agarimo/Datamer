package datamer.ctrl.boes;

import datamer.Var;
import datamer.ctrl.NotasC;
import datamer.ctrl.boes.ext.BB0;
import datamer.ctrl.boes.ext.INS;
import datamer.ctrl.boes.ext.Extraccion;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import tools.Dates;
import sql.Sql;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class ExtC implements Initializable {

    Node notas;
    NotasC notasC;
    PopOver popOver;

    int PanelPreview = 1;
    int PanelProcesar = 2;
    boolean isPreview;
    List<Integer> listaEstructurasCreadas;
    List<Integer> listaEstructurasManual;
    private final int procesar_to_preview = 1;
    private final int preview_to_procesar = 2;
    private final int procesar_to_wait = 3;
    private final int wait_to_preview = 4;
    private final int wait_to_procesar = 5;
    private final int preview_to_wait = 6;
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
    private DatePicker dpFecha;
    @FXML
    private Button btPreview;
    @FXML
    private Button btGenerarPdf;
    @FXML
    private Button btFolderPDF;
    @FXML
    private Button btFolderFiles;
    @FXML
    private Button btProcesar;
    @FXML
    private Button btForzarProcesar;
    @FXML
    private Button btNotas;
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
    private Button btGenerarArchivos;
    @FXML
    private Label lbMultasPreview;
//</editor-fold>
    ObservableList<ModeloProcesar> procesarList;
    ObservableList<ModeloPreview> previewList;

    @FXML
    void abrirCarpeta(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());

        if (fecha != null) {
            File fichero = new File(Var.ficheroEx, Dates.imprimeFecha(fecha));
            try {
                Desktop.getDesktop().browse(fichero.toURI());
            } catch (IOException ex) {
//                Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void abrirCarpetaAr(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());

        if (fecha != null) {
            File fichero = new File(Var.ficheroTxt, Dates.imprimeFecha(fecha));
            try {
                Desktop.getDesktop().browse(fichero.toURI());
            } catch (IOException ex) {
//                Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void cambioEnDatePicker(ActionEvent event) {
        try {
            Date fecha = Dates.asDate(dpFecha.getValue());

            if (fecha != null) {
                String query = "SELECT * FROM boes.procesar where fecha=" + Util.comillas(Dates.imprimeFecha(fecha));
                cargarDatosProcesar(Query.listaProcesar(query));
            }
        } catch (NullPointerException ex) {
            //
        }
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

    void cargarDatosProcesar(List<Procesar> list) {
        listaEstructurasCreadas = Query.listaEstructurasCreadas();
        listaEstructurasManual = Query.listaEstructurasManual();
        procesarList.clear();
        ModeloProcesar modelo;
        Procesar procesar;
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
            modelo.fecha.set(Dates.imprimeFecha(procesar.getFecha()));

            procesarList.add(modelo);
        }
    }

    @FXML
    void eliminarLineaPreview(ActionEvent event) {
        System.out.println("Eliminando");
        ModeloPreview aux = (ModeloPreview) tvPreview.getSelectionModel().getSelectedItem();
        System.out.println(aux);

        if (aux != null) {
            System.out.println("Borrando");
            previewList.remove(aux);
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
                mostrarPanel(this.preview_to_procesar);
                isPreview = !isPreview;
                switchControles(false);
                cambioEnDatePicker(new ActionEvent());
            });
        });
        Var.executor.execute(a);
    }

    @FXML
    void generarArchivos(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());

        if (fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    mostrarPanel(this.procesar_to_wait);
                    piProgreso.setProgress(-1);
                    lbProgreso.setText("");
                    lbProceso.setText("EJECUTANDO SCRIPT REQ/OBS");
                });

                ScriptReq sr = new ScriptReq(fecha);
                sr.run();

                Platform.runLater(() -> {
                    lbProgreso.setText("");
                    lbProceso.setText("EJECUTANDO SCRIPT EXPEDIENTE");
                });
                ScriptExp se = new ScriptExp(fecha);
                se.run();

                Platform.runLater(() -> {
                    lbProgreso.setText("");
                    lbProceso.setText("EJECUTANDO SCRIPT FASE");
                });
                ScriptFase sf = new ScriptFase();
                sf.run();

                Platform.runLater(() -> {
                    lbProgreso.setText("");
                    lbProceso.setText("EJECUTANDO SCRIPT ORIGEN");
                });
                ScriptOrigen so = new ScriptOrigen(fecha);
                so.run();

                Platform.runLater(() -> {
                    lbProgreso.setText("");
                    lbProceso.setText("EJECUTANDO SCRIPT ARTICULO");
                });
                ScriptArticulo sa = new ScriptArticulo();
                sa.run();

                Platform.runLater(() -> {
                    piProgreso.setProgress(1);
                    lbProgreso.setText("");
                    lbProceso.setText("");
                    mostrarPanel(this.wait_to_procesar);

                    cambioEnDatePicker(new ActionEvent());
                });

                Platform.runLater(() -> {
                    mostrarPanel(this.procesar_to_wait);
                    btGenerarArchivos.setDisable(true);
                    piProgreso.setProgress(-1);
                    lbProgreso.setText("");
                    lbProceso.setText("GENERANDO ARCHIVOS");
                });

                BB0 bb = new BB0(fecha);
                bb.run();
                INS bb1 = new INS(fecha);
                bb1.run();

                Platform.runLater(() -> {
                    piProgreso.setProgress(1);
                    lbProgreso.setText("");
                    lbProceso.setText("");
                    btGenerarArchivos.setDisable(false);
                    mostrarPanel(this.wait_to_procesar);
                });
            });
            Var.executor.execute(a);
        }

    }

    @FXML
    void generarPdf(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());

        if (fecha != null) {
            String query = "SELECT * FROM " + Var.dbNameBoes + ".procesar where fecha=" + Util.comillas(Dates.imprimeFecha(fecha));
            File fichero = new File(Var.ficheroEx, Dates.imprimeFecha(fecha));
            fichero.mkdirs();

            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    mostrarPanel(this.procesar_to_wait);
                    btGenerarPdf.setDisable(true);
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
                    btGenerarPdf.setDisable(false);
                    mostrarPanel(this.wait_to_procesar);

                    cambioEnDatePicker(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    /**
     * Generar PDF individual.
     *
     * @param event
     */
    @FXML
    void generarPdfI(ActionEvent event) {
        ModeloProcesar pr = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();
        Date fecha = Dates.asDate(dpFecha.getValue());

        if (fecha != null && pr != null) {
            File fichero = new File(Var.ficheroEx, Dates.imprimeFecha(fecha));
            fichero.mkdirs();

            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    btGenerarPdf.setDisable(true);
                    piProgreso.setProgress(0);
                    lbProgreso.setText("");
                    lbProceso.setText("GENERANDO PDF");
                });

                File destino;
                ModeloProcesar aux;
                List list = new ArrayList();
                list.add(pr);

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
                    aux = (ModeloProcesar) list.get(i);
                    destino = new File(fichero, aux.getCodigo() + ".pdf");

                    try {
                        Download.downloadFILE(aux.getLink(), destino);
                    } catch (IOException ex) {
                        Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                Platform.runLater(() -> {
                    piProgreso.setProgress(1);
                    lbProgreso.setText("");
                    lbProceso.setText("");
                    btGenerarPdf.setDisable(false);

                    cambioEnDatePicker(new ActionEvent());
                });
            });
            Var.executor.execute(a);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isPreview = false;
        panelPreview.setOpacity(0.0);
        panelPreview.setVisible(false);
        panelProcesar.setOpacity(1.0);
        panelProcesar.setVisible(true);
        panelEspera.setOpacity(0.0);
        panelEspera.setVisible(false);
        btNotas.setVisible(false);
        initializeIcons();
        iniciarTablaProcesar();
        iniciarTablaPreview();
        iniciarNotas();

        final ObservableList<ModeloProcesar> ls1 = tvProcesar.getSelectionModel().getSelectedItems();
        ls1.addListener(selectorTablaProcesar);

        final ObservableList<ModeloPreview> ls2 = tvPreview.getSelectionModel().getSelectedItems();
        ls2.addListener(selectorTablaPreview);
    }

    private void initializeIcons() {
        Text text;

        text = GlyphsDude.createIcon(MaterialIcon.FOLDER, "16");
        text.setFill(Color.DARKORANGE);
        btFolderPDF.setGraphic(text);

        text = GlyphsDude.createIcon(MaterialIcon.FOLDER, "16");
        text.setFill(Color.DARKORANGE);
        btFolderFiles.setGraphic(text);

//        btReqObs.setVisible(false);
//        btReqObs.setManaged(false);
//
//        text = GlyphsDude.createIcon(MaterialIcon.CACHED, "32");
//        text.setFill(Paint.valueOf(orange));
//        btRecargarClasificacion.setGraphic(text);
//
//        GlyphsDude.setIcon(btSelectAll, MaterialIcon.PLAYLIST_ADD, "32");
//        GlyphsDude.setIcon(btRecoverS, MaterialIcon.INPUT, "32");
//        GlyphsDude.setIcon(btRecoverD, MaterialIcon.INPUT, "32");
//        GlyphsDude.setIcon(btVerBoletinC, MaterialIcon.FIND_IN_PAGE, "32");
    }

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

    private void iniciarTablaPreview() {
        clExpediente.setCellValueFactory(new PropertyValueFactory<>("expediente"));
        clSancionado.setCellValueFactory(new PropertyValueFactory<>("sancionado"));
        clNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        clLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        clFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        clMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        clCuantia.setCellValueFactory(new PropertyValueFactory<>("cuantia"));
        clArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        clPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        clReqObs.setCellValueFactory(new PropertyValueFactory<>("reqObs"));

        clExpediente.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.1165));
        clSancionado.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.1940));
        clNif.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.09));
        clLocalidad.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.1940));
        clFecha.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.0775));
        clMatricula.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.07));
        clCuantia.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.0485));
        clArticulo.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.0970));
        clPuntos.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.0485));
        clReqObs.prefWidthProperty().bind(tvPreview.widthProperty().multiply(0.0485));

        previewList = FXCollections.observableArrayList();
        tvPreview.setItems(previewList);
    }

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
                                    setText("OK : " + Integer.toString(item));
                                    setTextFill(Color.GREEN);
                                } else {
                                    setText("Estructura no creada : " + Integer.toString(item));
                                    setTextFill(Color.RED);
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
                                setTextFill(Color.ORANGERED);
                                break;

                            case 5:
                                setText(Estado.XLSX_NO_GENERADO.toString());
                                setTextFill(Color.ORANGE);
                                break;
                            case 6:
                                setText(Estado.PROCESAR_MANUAL.toString());
                                setTextFill(Color.RED);
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

    @FXML
    void verNotas(ActionEvent event) {
        popOver = new PopOver();
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.arrowSizeProperty().setValue(12);
        popOver.arrowIndentProperty().setValue(13);
        popOver.arrowLocationProperty().setValue(PopOver.ArrowLocation.LEFT_BOTTOM);
        popOver.cornerRadiusProperty().setValue(7);
        popOver.headerAlwaysVisibleProperty().setValue(false);
        popOver.setAnimated(true);

        popOver.setContentNode(notas);
        popOver.show(btNotas);
    }

    public void cerrarPopOver() {
        popOver.hide();
        tvProcesar.getSelectionModel().clearSelection();
    }

    @FXML
    void previsualizar(ActionEvent event) {
        if (isPreview) {
            btPreview.setText("Previsualizar Extracción");
            mostrarPanel(this.preview_to_procesar);
            isPreview = !isPreview;
            switchControles(false);
        } else {
            Date fecha = Dates.asDate(dpFecha.getValue());
            ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();
            Extraccion ex;

            if (fecha != null || aux != null) {
                ex = new Extraccion(fecha);
                if (ex.fileExist(aux.getCodigo())) {
                    if (listaEstructurasCreadas.contains(aux.getEstructura())) {

                        Thread a = new Thread(() -> {
                            List<Multa> procesados;

                            Platform.runLater(() -> {
                                switchControles(true);
                                btGenerarPdf.setDisable(true);
                                piProgreso.setProgress(-1);
                                lbProgreso.setText("");
                                lbProceso.setText("PROCESANDO PREVISUALIZACIÓN");
                                btPreview.setText("Volver");
                                mostrarPanel(this.procesar_to_wait);
                                isPreview = !isPreview;
                            });

                            try {
                                procesados = ex.previewXLSX(Query.getProcesar(aux.getCodigo()));

                                Platform.runLater(() -> {
                                    cargarDatosPreview(procesados);
                                    piProgreso.setProgress(1);
                                    lbProgreso.setText("");
                                    lbProceso.setText("");
                                    mostrarPanel(this.wait_to_preview);
                                });
                            } catch (Exception e) {
                                Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
                                Platform.runLater(() -> {
                                    piProgreso.setProgress(1);
                                    lbProgreso.setText("");
                                    lbProceso.setText("");

                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("ERROR");
                                    alert.setHeaderText("XLSX CON ERRORES");
                                    alert.setContentText("El XLSX seleccionado contiene errores de estructura \n"
                                            + e.getMessage());
                                    alert.showAndWait();

                                    btPreview.setText("Previsualizar Extracción");
                                    mostrarPanel(this.wait_to_procesar);
                                    isPreview = !isPreview;
                                    switchControles(false);
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
        Date fecha = Dates.asDate(dpFecha.getValue());
        List<ModeloProcesar> list = getBoletinesProcesar();

        if (!list.isEmpty() && fecha != null) {
            Thread a = new Thread(() -> {

                Platform.runLater(() -> {
                    mostrarPanel(this.procesar_to_wait);
                    btGenerarPdf.setDisable(true);
                    piProgreso.setProgress(0);
                    lbProgreso.setText("");
                    lbProceso.setText("PREPARANDO BBDD");
                });

                procesarRunPreClean(Dates.asLocalDate(fecha));

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
                        System.out.println(e.getMessage());
                        System.out.println(aux.getCodigo());
                        pr.SQLSetEstado(Estado.ERROR_PROCESAR.getValue());
                    }
                }

                Platform.runLater(() -> {
                    piProgreso.setProgress(1);
                    lbProgreso.setText("");
                    lbProceso.setText("");
                    btGenerarPdf.setDisable(false);
                    mostrarPanel(this.wait_to_procesar);

                    cambioEnDatePicker(new ActionEvent());
                });
            });
            Var.executor.execute(a);
        }
    }

    private void procesarRunPreClean(LocalDate fecha) {
        String queryMultas = "DELETE FROM " + Var.dbNameBoes + ".multa WHERE fechaPublicacion=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE));
        String queryProcesar = "UPDATE " + Var.dbNameBoes + ".procesar SET estado=1 WHERE fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE));

        try {
            Sql bd = new Sql(Var.con);
            bd.ejecutar(queryMultas);
            bd.ejecutar(queryProcesar);
            bd.close();
            cambioEnDatePicker(new ActionEvent());
        } catch (SQLException ex) {
            Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mostrarPanel(int panel) {
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

    @FXML
    void resetearEstado(ActionEvent event) {
        ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();
        Procesar pr = Query.getProcesar(aux.getCodigo());
        Query.eliminarMultasBoletin(pr.getCodigo());
        pr.SQLSetEstado(Estado.LISTO_PROCESAR.getValue());
        cambioEnDatePicker(new ActionEvent());
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

    void switchControles(boolean aux) {
        dpFecha.setDisable(aux);
        btGenerarPdf.setDisable(aux);
        btFolderPDF.setDisable(aux);
        btProcesar.setDisable(aux);
        btFolderFiles.setDisable(aux);
        btGenerarArchivos.setDisable(aux);
    }

    @FXML
    void verPdf(ActionEvent event) {
        ModeloProcesar pr = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();

        if (pr != null) {
            try {
                Desktop.getDesktop().browse(new URI(pr.link.get()));

            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void verXLSX(ActionEvent event) {
        Date fecha = Dates.asDate(dpFecha.getValue());
        ModeloProcesar pr = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();

        if (pr != null && fecha != null) {
            File fichero = new File(Var.ficheroEx, Dates.imprimeFecha(fecha));
            File archivo = new File(fichero, pr.getCodigo() + ".xlsx");

            try {
                Desktop.getDesktop().browse(archivo.toURI());

            } catch (IOException ex) {
                Logger.getLogger(ExtC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="LISTENER">
    /**
     * Listener de la Tabla Procesar
     */
    private final ListChangeListener<ModeloProcesar> selectorTablaProcesar
            = (ListChangeListener.Change<? extends ModeloProcesar> c) -> {
                ModeloProcesar aux = (ModeloProcesar) tvProcesar.getSelectionModel().getSelectedItem();

                if (aux != null) {
                    if (aux.getEstructura() == -1) {
                        btNotas.setVisible(false);
                    } else {
                        btNotas.setVisible(true);

                        if (notasC.setNota(aux.getEstructura())) {
                            btNotas.setText("VER NOTAS");
                            btNotas.setTextFill(Color.ORANGERED);
                        } else {
                            btNotas.setText("CREAR NOTA");
                            btNotas.setTextFill(Color.GREEN);
                        }
                    }
                } else {
                    btNotas.setVisible(false);
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
