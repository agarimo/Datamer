package datamer.ctrl.tkm;

import datamer.model.tkm.enty.Cliente;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
    private Button btDeleteCliente;

    /**
     * TIPO BÚSQUEDA 0=CIF, 1=TELÉFONO
     */
    private int tipoBusqueda;
    private final int VIEW_PANE = 1;
    private final int NEW_PANE = 2;

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
        initializeNew();
        initializeView();
    }

    private void showPane(int pane) {
        switch (pane) {
            case 0:
                vistaPane.setVisible(false);
                newPane.setVisible(false);
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
    }

    @FXML
    void showPaneNew(ActionEvent event) {
        showPane(NEW_PANE);
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
    
    private void initializeView() {

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

    }

    @FXML
    void cancelarNew(ActionEvent event) {
        clearNewData();
        showPane(0);
    }

    @FXML
    void guardarNew(ActionEvent event) {
         
    }

    private Cliente getNewData() {
        Cliente aux = new Cliente();
        
        aux.setCif(tfNewCif.getText().trim().toUpperCase());
        aux.setNombre(tfNewNombre.getText().trim().toUpperCase());
//        aux.setEstado(cbNewEstado.getSelectionModel().getSelectedItem());
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
