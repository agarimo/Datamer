package datamer.ctrl.tkm;

import datamer.model.tkm.Estado;
import datamer.model.tkm.enty.Cliente;
import datamer.model.tkm.enty.Comentario;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import util.LoadFile;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class TelemarkC implements Initializable {

    /**
     * GENERAL
     */
    @FXML
    private VBox rootPane;

    @FXML
    private TextField tfBuscar;

    @FXML
    private Button btBuscar;

    @FXML
    private RadioButton rbCif;

    @FXML
    private RadioButton rbTelefono;

    @FXML
    private Button btNewCliente;

    @FXML
    private Button btEditCliente;

    @FXML
    private Button btDeleteCliente;

    /**
     * TIPO BÚSQUEDA 0=CIF, 1=TELÉFONO
     */
    private int tipoBusqueda;
    private final int VIEW_PANE = 1;
    private final int NEW_PANE = 2;

    ObservableList<Estado> comboEstado;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vistaPane.setVisible(false);
        newPane.setVisible(false);
        rbCif.setSelected(true);
        tipoBusqueda = 0;
        btEditCliente.setDisable(true);
        btDeleteCliente.setDisable(true);
        comboEstado = FXCollections.observableArrayList();
        comboEstado.addAll(Estado.values());

        initializeNew();
        initializeView();

    }

    private void showPane(int pane) {
        System.out.println("Showing pane: " + pane);
        switch (pane) {
            case 0:
                vistaPane.setVisible(false);
                newPane.setVisible(false);
                break;
            case 1:
                vistaPane.setVisible(true);
                newPane.setVisible(false);
                break;
            case 2:
                vistaPane.setVisible(false);
                newPane.setVisible(true);
                break;
        }
    }

    @FXML
    void showPaneView(ActionEvent event) {
        showPane(VIEW_PANE);
        btNewCliente.setDisable(false);
        btEditCliente.setDisable(false);
        btDeleteCliente.setDisable(false);
    }

    @FXML
    void showPaneNew(ActionEvent event) {
        showPane(NEW_PANE);
        btNewCliente.setDisable(true);
        btEditCliente.setDisable(true);
        btDeleteCliente.setDisable(true);
    }

    @FXML
    void setTipoCif(ActionEvent event) {
        tipoBusqueda = 0;
    }

    @FXML
    void setTipoTelefono(ActionEvent event) {
        tipoBusqueda = 1;
    }

    //<editor-fold defaultstate="collapsed" desc="VIEW_PANE">
    @FXML
    private VBox vistaPane;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfCif;

    @FXML
    private ComboBox cbEstado;

    @FXML
    private TextField tfTelf;

    @FXML
    private TextField tfContacto;

    @FXML
    private TextField tfMail;

    @FXML
    private ListView lvNotas;

    @FXML
    private TextField tfNewNota;

    @FXML
    private Button btAgregar;

    private void initializeView() {
        cbEstado.setItems(comboEstado);
        setViewEditable(false);
    }

    private void setViewEditable(boolean aux) {
        tfNombre.setEditable(aux);
        tfCif.setEditable(aux);
        cbEstado.setEditable(aux);
        tfTelf.setEditable(aux);
        tfContacto.setEditable(aux);
        tfMail.setEditable(aux);
    }

    @FXML
    void newNota(ActionEvent event) {

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NEW_PANE">
    @FXML
    private VBox newPane;

    @FXML
    private TextField tfNewCif;

    @FXML
    private TextField tfNewNombre;

    @FXML
    private ComboBox cbNewEstado;

    @FXML
    private TextField tfNewTelf;

    @FXML
    private TextField tfNewContacto;

    @FXML
    private TextField tfNewMail;

    @FXML
    private Button btNewCancelar;

    @FXML
    private Button btNewGuardar;

    private void initializeNew() {
        cbNewEstado.setItems(comboEstado);
        cbNewEstado.getSelectionModel().select(0);
    }

    @FXML
    void cancelarNew(ActionEvent event) {
        clearNewData();
        showPane(0);
        btNewCliente.setDisable(false);
    }

    @FXML
    void guardarNew(ActionEvent event) {
        getNewData().SQLGuardar();
        clearNewData();
        showPane(0);
        btNewCliente.setDisable(false);
    }

    private Cliente getNewData() {
        Estado estado;
        Cliente aux = new Cliente();

        aux.setCif(tfNewCif.getText().trim().toUpperCase());
        aux.setNombre(tfNewNombre.getText().trim().toUpperCase());
        estado = (Estado) cbNewEstado.getSelectionModel().getSelectedItem();
        aux.setEstado(estado.getValue());
        aux.setTelefono(tfNewTelf.getText().trim().toUpperCase());
        aux.setContacto(tfNewContacto.getText().trim().toUpperCase());
        aux.setMail(tfNewMail.getText().trim().toUpperCase());

        return aux;
    }

    private void clearNewData() {
        tfNewCif.setText("");
        tfNewNombre.setText("");
        cbNewEstado.getSelectionModel().select(0);
        tfNewTelf.setText("");
        tfNewContacto.setText("");
        tfNewMail.setText("");
    }
//</editor-fold>
}
