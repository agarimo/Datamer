package datamer.ctrl.boes;

import datamer.Regex;
import datamer.Var;
import datamer.model.boes.ModeloPattern;
import datamer.model.boes.enty.Pattern;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import tools.Dates;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class PatternC implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXMLVar">
    @FXML
    private VBox rootPane;
    @FXML
    private Label lbConPatronNif;
    @FXML
    private Label lbSinPatronNif;
    @FXML
    private Label lbPorcentajeNif;
    @FXML
    private Label lbConPatronMat;
    @FXML
    private Label lbSinPatronMat;
    @FXML
    private Label lbPorcentajeMat;
    @FXML
    private Label lbCount;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TableView tvNif;
    @FXML
    private TableColumn idNifCL;
    @FXML
    private TableColumn nifCL;
    @FXML
    private TableView tvMat;
    @FXML
    private TableColumn idMatCL;
    @FXML
    private TableColumn matCL;
    //</editor-fold>

    List<Pattern> listado;
    ObservableList<ModeloPattern> nifList;
    ObservableList<ModeloPattern> matList;

    int contadorNif;
    int contadorMat;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clearWindow();
        iniciarTablaNif();
        iniciarTablaMat();
    }

    private void iniciarTablaNif() {
        idNifCL.setCellValueFactory(new PropertyValueFactory<>("id"));
        nifCL.setCellValueFactory(new PropertyValueFactory<>("dato"));

        nifList = FXCollections.observableArrayList();
        tvNif.setItems(nifList);
    }

    private void iniciarTablaMat() {
        idMatCL.setCellValueFactory(new PropertyValueFactory<>("id"));
        matCL.setCellValueFactory(new PropertyValueFactory<>("dato"));

        matList = FXCollections.observableArrayList();
        tvMat.setItems(matList);
    }

    private void cargaDatos(Date fecha) {
        nifList.clear();
        matList.clear();
        contadorNif = 0;
        contadorMat = 0;
        this.listado = Query.listaPattern(fecha);
        carga();
    }

    private void carga() {
        Pattern aux;
        Regex rx = new Regex();
        ModeloPattern mp;
        Iterator<Pattern> it = listado.iterator();

        while (it.hasNext()) {
            aux = it.next();

            if (rx.isBuscar(Arrays.asList(Regex.nif), aux.getNif())) {
                contadorNif++;
            } else {
                if (!"".equals(aux.getNif())) {
                    mp = new ModeloPattern();
                    mp.id.set(aux.getCodigo());
                    mp.dato.set(aux.getNif());
                    nifList.add(mp);
                }
            }

            if (rx.isBuscar(Arrays.asList(Regex.matriculas), aux.getMatricula())) {
                contadorMat++;
            } else {
                if (!"".equals(aux.getMatricula())) {
                    mp = new ModeloPattern();
                    mp.id.set(aux.getCodigo());
                    mp.dato.set(aux.getMatricula());
                    matList.add(mp);
                }
            }
        }
    }

    private void setContadores() {
        double porcentajeNif = ((double) contadorNif * 100) / (double) listado.size();
        double porcentajeMat = ((double) contadorMat * 100) / (double) listado.size();
        DecimalFormat f = new DecimalFormat("#.##");

        this.lbCount.setText("Se han cargado " + listado.size() + " multas.");
        this.lbConPatronNif.setText("Cumplen el patrón: " + contadorNif);
        this.lbSinPatronNif.setText("Sin patrón: " + (listado.size() - contadorNif));
        this.lbPorcentajeNif.setText("Porcentaje con patrón: " + f.format(porcentajeNif) + "%");
        this.lbConPatronMat.setText("Cumplen el patrón: " + contadorMat);
        this.lbSinPatronMat.setText("Sin patrón: " + (listado.size() - contadorMat));
        this.lbPorcentajeMat.setText("Porcentaje con patrón: " + f.format(porcentajeMat) + "%");
    }

    private void clearWindow() {
        this.lbCount.setText("");
        this.lbConPatronNif.setText("");
        this.lbSinPatronNif.setText("");
        this.lbPorcentajeNif.setText("");
        this.lbConPatronMat.setText("");
        this.lbSinPatronMat.setText("");
        this.lbPorcentajeMat.setText("");
    }

    @FXML
    void cambioEnDatePicker(ActionEvent event) {
        try {
            Date fecha = Dates.asDate(dpFecha.getValue());

            if (fecha != null) {
                Thread a = new Thread(() -> {
                    Platform.runLater(() -> {
                        clearWindow();
                        nifList.clear();
                        matList.clear();
                        rootPane.getScene().setCursor(Cursor.WAIT);
                        dpFecha.setDisable(true);
                    });

                    cargaDatos(fecha);

                    Platform.runLater(() -> {
                        setContadores();
                        rootPane.getScene().setCursor(Cursor.DEFAULT);
                        dpFecha.setDisable(false);
                    });
                });
                Var.executor.execute(a);
            }
        } catch (NullPointerException ex) {
            //
        }
    }

    @FXML
    void verBoletinNif(ActionEvent event) {
        ModeloPattern mp = (ModeloPattern) this.tvNif.getSelectionModel().getSelectedItem();

        if (mp != null) {
            try {
                Desktop.getDesktop().browse(new URI(Query.getLink(mp.getId())));
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(PatternC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void verBoletinMat(ActionEvent event) {
        ModeloPattern mp = (ModeloPattern) this.tvMat.getSelectionModel().getSelectedItem();

        if (mp != null) {
            try {
                Desktop.getDesktop().browse(new URI(Query.getLink(mp.getId())));
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(PatternC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
