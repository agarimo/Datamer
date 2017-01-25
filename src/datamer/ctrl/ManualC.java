/*
 * The MIT License
 *
 * Copyright 2017 agarimo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package datamer.ctrl;

import datamer.ctrl.boes.ExtC;
import datamer.ctrl.boes.Query;
import datamer.ctrl.boes.ext.ManualStruc;
import datamer.ctrl.boes.ext.XLSXProcess;
import datamer.model.boes.Estado;
import datamer.model.boes.ModeloPreview;
import datamer.model.boes.ModeloProcesar;
import datamer.model.boes.enty.Multa;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import tools.Dates;

/**
 * FXML Controller class
 *
 * @author agarimo
 */
public class ManualC implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML VAR">
    @FXML
    private ToggleButton btVerTexto;

    @FXML
    private ToggleGroup op;

    @FXML
    private ToggleButton btInsertar;

    @FXML
    private ToggleButton btEstructura;

    @FXML
    private ToggleButton btPreview;

    @FXML
    private VBox pAceptar;

    @FXML
    private VBox pPreview;

    @FXML
    private StackPane stackPane;

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
    private Label lbMultasPreview;

    @FXML
    private VBox pEstructura;

    @FXML
    private ComboBox cbExpediente;

    @FXML
    private ComboBox cbSancionado;

    @FXML
    private ComboBox cbNif;

    @FXML
    private ComboBox cbLocalidad;

    @FXML
    private ComboBox cbFecha;

    @FXML
    private ComboBox cbMatricula;

    @FXML
    private ComboBox cbCuantia;

    @FXML
    private ComboBox cbArticulo;

    @FXML
    private ComboBox cbPrecepto;

    @FXML
    private ComboBox cbPuntos;

    @FXML
    private ComboBox cbReqObs;

    @FXML
    private Label lbLineas;

    @FXML
    private Label lbColumnas;

    @FXML
    private VBox pInsertar;

    @FXML
    private TextArea taInsertar;

    @FXML
    private VBox pVerTexto;

    @FXML
    private TextArea taVerTexto;
//</editor-fold>

    private ExtC controller;
    private ManualStruc struc;
    private ModeloProcesar boletin;

    private ObservableList<ModeloPreview> previewList;
    private ObservableList<String> modelo;

    private FadeTransition fadeIn;
    private FadeTransition fadeOut;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initAnimation();
        initTablaPreview();
        initEstructura();
        showPanel(0);
    }

    private void initAnimation() {
        fadeIn = new FadeTransition(Duration.millis(1000), stackPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setAutoReverse(false);

        fadeOut = new FadeTransition(Duration.millis(1000), stackPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setAutoReverse(false);
    }

    private void showPanel(int panel) {
        fadeOut.play();
        pVerTexto.setVisible(false);
        pInsertar.setVisible(false);
        pEstructura.setVisible(false);
        pPreview.setVisible(false);
        pAceptar.setVisible(false);

        switch (panel) {
            case 0:
                pVerTexto.setVisible(true);
                break;
            case 1:
                pInsertar.setVisible(true);
                break;
            case 2:
                pEstructura.setVisible(true);
                break;
            case 3:
                pPreview.setVisible(true);
                break;
            case 4:
                pAceptar.setVisible(true);
                break;
        }
        fadeIn.play();
    }

    public void setBoletin(ModeloProcesar boletin) {
        clearTexto();
        clearInsercion();
        clearEstructura();
        clearPreview();
        btVerTexto.setSelected(true);
        showPanel(0);
        this.boletin = boletin;
        struc = new ManualStruc(boletin, getInsercion());
        setTexto(struc.getTexto());
    }

    public void setParentController(ExtC controller) {
        this.controller = controller;
    }

    private void close() {
        controller.cerrarExtManual();
    }

    @FXML
    void verTexto(ActionEvent event) {
        showPanel(0);
    }

    @FXML
    void insertar(ActionEvent event) {
        showPanel(1);
    }

    @FXML
    void estructura(ActionEvent event) {
        struc.setData(getInsercion());
        loadEstructura();
        showPanel(2);
    }

    @FXML
    void preview(ActionEvent event) {
        getEstructura();
        cargarDatosPreview(struc.getMultas());
        showPanel(3);
    }

    //<editor-fold defaultstate="collapsed" desc="PANEL VERTEXTO">
    private void setTexto(String data) {
        taVerTexto.setText(data);
    }

    private void clearTexto() {
        taVerTexto.setText("");
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PANEL INSERTAR">
    private String getInsercion() {
        return taInsertar.getText();
    }

    private void clearInsercion() {
        taInsertar.setText("");
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PANEL ESTRUCTURA">
    private void initEstructura() {
        modelo = FXCollections.observableArrayList();

        cbExpediente.setItems(modelo);
        cbSancionado.setItems(modelo);
        cbNif.setItems(modelo);
        cbLocalidad.setItems(modelo);
        cbFecha.setItems(modelo);
        cbMatricula.setItems(modelo);
        cbCuantia.setItems(modelo);
        cbArticulo.setItems(modelo);
        cbPrecepto.setItems(modelo);
        cbPuntos.setItems(modelo);
        cbReqObs.setItems(modelo);
    }

    private void loadEstructura() {
        lbLineas.setText(Integer.toString(struc.getLineas()));
        lbColumnas.setText(Integer.toString(struc.getColumnas()));
        modelo.clear();
        modelo.addAll(struc.getModel());
    }

    void getEstructura() {
        struc.setExpediente(cbExpediente.getSelectionModel().getSelectedIndex());
        struc.setSancionado(cbSancionado.getSelectionModel().getSelectedIndex());
        struc.setNif(cbNif.getSelectionModel().getSelectedIndex());
        struc.setLocalidad(cbLocalidad.getSelectionModel().getSelectedIndex());
        struc.setFecha(cbFecha.getSelectionModel().getSelectedIndex());
        struc.setMatricula(cbMatricula.getSelectionModel().getSelectedIndex());
        struc.setCuantia(cbCuantia.getSelectionModel().getSelectedIndex());
        struc.setArticulo(cbArticulo.getSelectionModel().getSelectedIndex());
        struc.setPrecepto(cbPrecepto.getSelectionModel().getSelectedIndex());
        struc.setPuntos(cbPuntos.getSelectionModel().getSelectedIndex());
        struc.setReqObs(cbReqObs.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void restore(ActionEvent event) {
        if (cbExpediente.isFocused()) {
            cbExpediente.getSelectionModel().select(-1);
        }

        if (cbSancionado.isFocused()) {
            cbSancionado.getSelectionModel().select(-1);
        }

        if (cbNif.isFocused()) {
            cbNif.getSelectionModel().select(-1);
        }

        if (cbLocalidad.isFocused()) {
            cbLocalidad.getSelectionModel().select(-1);
        }

        if (cbFecha.isFocused()) {
            cbFecha.getSelectionModel().select(-1);
        }

        if (cbMatricula.isFocused()) {
            cbMatricula.getSelectionModel().select(-1);
        }

        if (cbCuantia.isFocused()) {
            cbCuantia.getSelectionModel().select(-1);
        }

        if (cbArticulo.isFocused()) {
            cbArticulo.getSelectionModel().select(-1);
        }

        if (cbPrecepto.isFocused()) {
            cbPrecepto.getSelectionModel().select(-1);
        }

        if (cbPuntos.isFocused()) {
            cbPuntos.getSelectionModel().select(-1);
        }

        if (cbReqObs.isFocused()) {
            cbReqObs.getSelectionModel().select(-1);
        }
    }

    private void clearEstructura() {
        modelo.clear();
        
        cbExpediente.getSelectionModel().select(-1);
        cbSancionado.getSelectionModel().select(-1);
        cbNif.getSelectionModel().select(-1);
        cbLocalidad.getSelectionModel().select(-1);
        cbFecha.getSelectionModel().select(-1);
        cbMatricula.getSelectionModel().select(-1);
        cbCuantia.getSelectionModel().select(-1);
        cbArticulo.getSelectionModel().select(-1);
        cbPrecepto.getSelectionModel().select(-1);
        cbPuntos.getSelectionModel().select(-1);
        cbReqObs.getSelectionModel().select(-1);

        lbLineas.setText("");
        lbColumnas.setText("");
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PANEL PREVIEW">
    private void initTablaPreview() {
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
        ModeloPreview mdl;
        Multa multa;
        Iterator<Multa> it = list.iterator();

        while (it.hasNext()) {
            multa = it.next();

            if (!multa.equals(new Multa())) {
                mdl = new ModeloPreview();
                mdl.setMulta(multa);
                mdl.expediente.set(multa.getExpediente());
                mdl.sancionado.set(multa.getSancionado());
                mdl.nif.set(multa.getNif());
                mdl.localidad.set(multa.getLocalidad());
                mdl.fecha.set(Dates.imprimeFecha(multa.getFechaMulta()));
                mdl.matricula.set(multa.getMatricula());
                mdl.cuantia.set(multa.getCuantia());
                mdl.articulo.set(multa.getArticulo());
                mdl.puntos.set(multa.getPuntos());
                mdl.reqObs.set(multa.getReqObs());

                previewList.add(mdl);
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

    private void clearPreview() {
        previewList.clear();
    }

    @FXML
    void aceptarYProcesar(ActionEvent event) {
        showPanel(4);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PANEL ACEPTAR">
    @FXML
    void aceptarProcesado(ActionEvent event) {
        List<Multa> list = new ArrayList();
        ModeloPreview mp;
        Iterator<ModeloPreview> it = previewList.iterator();

        while (it.hasNext()) {
            mp = it.next();

            if (!mp.getMulta().equals(new Multa())) {
                list.add(mp.getMulta());
            }
        }

        try {
            Query.eliminarMultasBoletin(boletin.getCodigo());
            XLSXProcess.insertMultas(list);
            struc.getProcesar().SQLSetEstado(Estado.PROCESADO_XLSX.getValue());
        } catch (Exception e) {
            struc.getProcesar().SQLSetEstado(Estado.ERROR_PROCESAR.getValue());
        }
        
        close();
    }

    @FXML
    void cancelarProcesado(ActionEvent event) {
        btPreview.setSelected(true);
        showPanel(3);
    }
//</editor-fold>
}
