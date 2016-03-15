package datamer.ctrl.tkm;

import datamer.model.tkm.Estado;
import datamer.model.tkm.enty.Cliente;
import datamer.model.tkm.enty.Comentario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import util.CalculaNif;

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
    private boolean isEditando;

    ObservableList<Estado> comboEstado;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isEditando = false;
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
    void setTipoCif(ActionEvent event) {
        tipoBusqueda = 0;
    }

    @FXML
    void setTipoTelefono(ActionEvent event) {
        tipoBusqueda = 1;
    }

    @FXML
    void buscar(ActionEvent event) {
//        String aux = tfBuscar.getText().toUpperCase().trim();
        String aux = getBusqueda();

        switch (tipoBusqueda) {
            case 0:
                cliente = Query.getCliente(Cliente.SQLBuscarCif(aux));
                break;
            case 1:
                cliente = Query.getCliente(Cliente.SQLBuscarTelf(aux));
                break;
        }

        if (cliente != null) {
            showCliente(cliente);
            showNotas(cliente.getId());

            showPane(VIEW_PANE);
            btNewCliente.setDisable(false);
            btEditCliente.setDisable(false);
            btDeleteCliente.setDisable(false);
            tfBuscar.setText("");
        } else {
            tfBuscar.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMACIÓN");
            alert.setHeaderText("ENTRADA SIN REGISTROS.");
            switch (tipoBusqueda) {
                case 0:
                    alert.setContentText("No existe ningún registro para el CIF : " + aux);
                    break;
                case 1:
                    alert.setContentText("No existe ningún registro para el TELÉFONO : " + aux);
                    break;
            }
            alert.showAndWait();
        }
    }

    private String getBusqueda() {
        String aux = tfBuscar.getText().toUpperCase().trim();

        if (tipoBusqueda == 0) {
            return checkNif(aux);
        } else {
            return aux;
        }
    }
    
    private String checkNif(String aux){
        CalculaNif cn = new CalculaNif();

            if (cn.letrasCif.contains("" + aux.charAt(0))) {
                if (aux.length() == 8) {
                    aux = cn.calcular(aux);
                }
            } else if (cn.letrasNie.contains("" + aux.charAt(0))) {
                if (aux.length() <= 8) {
                    aux = cn.calcular(aux);
                }
            } else {
                if (aux.length() <= 8) {
                    aux = cn.calcular(aux);
                }
            }
            return aux;
    }

    @FXML
    void nuevoCliente(ActionEvent event) {
        if (isEditando) {
            updateCliente();
            showCliente(cliente);
            isEditando = false;
            setViewEditable(false);
            btNewCliente.setText("Nuevo Cliente");
            btEditCliente.setText("Editar Cliente");
            btDeleteCliente.setVisible(true);
        } else {
            showPane(NEW_PANE);
            btNewCliente.setDisable(true);
            btEditCliente.setDisable(true);
            btDeleteCliente.setDisable(true);
        }
    }

    @FXML
    void editaCliente(ActionEvent event) {
        if (isEditando) {
            showCliente(cliente);
            isEditando = false;
            setViewEditable(false);
            btNewCliente.setText("Nuevo Cliente");
            btEditCliente.setText("Editar Cliente");
            btDeleteCliente.setVisible(true);
        } else {
            isEditando = true;
            setViewEditable(true);
            btNewCliente.setText("Aceptar");
            btEditCliente.setText("Cancelar");
            btDeleteCliente.setVisible(false);
        }
    }

    @FXML
    void borraCliente(ActionEvent event) {
        Query.ejecutar(cliente.SQLEliminar());
        showPane(0);
    }

    private void updateCliente() {
        Estado st = (Estado) cbEstado.getSelectionModel().getSelectedItem();
        cliente.setNombre(tfNombre.getText().toUpperCase().trim());
        cliente.setEstado(st.getValue());
        cliente.setTelefono(tfTelf.getText().toUpperCase().trim());
        cliente.setContacto(tfContacto.getText().toUpperCase().trim());
        cliente.setMail(tfMail.getText().trim());

        Query.ejecutar(cliente.SQLEditar());
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

    Cliente cliente;
    ObservableList<String> notas;

    private void initializeView() {
        initializeList();
        cliente = null;
        cbEstado.setItems(comboEstado);
        tfCif.setEditable(false);
        setViewEditable(false);
    }

    private void initializeList() {
        notas = FXCollections.observableArrayList();
        lvNotas.setItems(notas);
        lvNotas.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                final ListCell cell = new ListCell() {
                    private Text text;

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            text = new Text(item.toString());
                            text.setWrappingWidth(lvNotas.getWidth() - 20);
                            setGraphic(text);
                        } else {
                            text = new Text("");
                            setGraphic(text);
                        }
                    }
                };

                return cell;
            }
        });
    }

    private void setViewEditable(boolean aux) {
        tfNombre.setEditable(aux);
        cbEstado.setDisable(!aux);
        tfTelf.setEditable(aux);
        tfContacto.setEditable(aux);
        tfMail.setEditable(aux);
    }

    private void showCliente(Cliente aux) {
        tfNombre.setText(aux.getNombre());
        tfCif.setText(aux.getCif());
        cbEstado.getSelectionModel().select(aux.getEstado());
        tfTelf.setText(aux.getTelefono());
        tfContacto.setText(aux.getContacto());
        tfMail.setText(aux.getMail());

        showNotas(aux.getId());
    }

    @FXML
    void newNota(ActionEvent event) {
        String aux = tfNewNota.getText().toUpperCase().trim();
        Comentario com = new Comentario(cliente.getId(), aux);
        Query.ejecutar(com.SQLCrear());
        tfNewNota.setText("");
        showNotas(cliente.getId());
    }

    private void showNotas(int idCliente) {
        notas.clear();
        notas.addAll(Query.listaComentarios(idCliente));
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

        aux.setCif(checkNif(tfNewCif.getText().trim().toUpperCase()));
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
