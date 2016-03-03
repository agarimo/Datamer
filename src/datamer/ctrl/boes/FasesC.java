package datamer.ctrl.boes;

import datamer.Var;
import datamer.model.boes.ModeloFases;
import datamer.model.boes.Kind;
import datamer.model.boes.enty.Entidad;
import datamer.model.boes.enty.Fase;
import datamer.model.boes.enty.Origen;
import datamer.model.boes.enty.Tipo;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import util.Sql;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class FasesC implements Initializable {

    @FXML
    ComboBox cbEntidad;

    @FXML
    ListView lvOrigen;

    @FXML
    TextField tfOrigen;

    @FXML
    TableView tvFases;

    @FXML
    ComboBox cbCodigo;

    @FXML
    ComboBox cbTipo;

    @FXML
    TextField tfDias;

    @FXML
    TextArea taTexto1;

    @FXML
    TextArea taTexto2;

    @FXML
    TextArea taTexto3;

    @FXML
    Button btNuevaFase;

    @FXML
    Button btEditarFase;

    @FXML
    Button btBorrarFase;

    @FXML
    Button btGuardarFase;

    @FXML
    TableColumn<ModeloFases, String> faseCLF;

    @FXML
    TableColumn<ModeloFases, String> codigoCLF;

    @FXML
    TableColumn<ModeloFases, String> tipoCLF;

    @FXML
    TableColumn<ModeloFases, String> diasCLF;

    ObservableList<Entidad> comboEntidades;
    ObservableList<Tipo> comboCodigo;
    ObservableList<Kind> comboTipo;
    ObservableList<Origen> listOrigenes;
    ObservableList<ModeloFases> tablaFases;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void nuevaFase(ActionEvent event) {
        btNuevaFase.setDisable(true);
        Origen origen = (Origen) lvOrigen.getSelectionModel().getSelectedItem();
        btGuardarFase.setVisible(true);
        ModeloFases aux = new ModeloFases();
        aux.id.set(0);
        aux.idOrigen.set(origen.getId());
        aux.codigo.set(null);
        aux.dias.set(0);
        aux.tipo.set(1);

        tablaFases.add(0, aux);
        btBorrarFase.setVisible(false);
        btEditarFase.setVisible(false);

        tvFases.getSelectionModel().select(0);
        tvFases.scrollTo(0);
        tvFases.requestFocus();
    }

    @FXML
    void editaFase(ActionEvent event) {
        Sql bd;
        Fase aux = getDatosFase();

        try {
            bd = new Sql(Var.con);
            bd.ejecutar(aux.SQLEditar());
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(FasesC.class.getName()).log(Level.SEVERE, null, ex);
        }

        btGuardarFase.setVisible(false);
        btBorrarFase.setVisible(true);
        btEditarFase.setVisible(true);
        btNuevaFase.setDisable(false);

        limpiarFases();
        cargaFasesOrigen();
    }

    @FXML
    void borraFase(ActionEvent event) {
        Sql bd;
        Fase aux = getDatosFase();

        try {
            bd = new Sql(Var.con);
            bd.ejecutar(aux.SQLBorrar());
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(FasesC.class.getName()).log(Level.SEVERE, null, ex);
        }

        btGuardarFase.setVisible(false);
        btBorrarFase.setVisible(true);
        btEditarFase.setVisible(true);
        btNuevaFase.setDisable(false);

        limpiarFases();
        cargaFasesOrigen();
    }

    @FXML
    void guardaFase(ActionEvent event) {
        Sql bd;
        Fase aux = getDatosFase();

        try {
            bd = new Sql(Var.con);
            bd.ejecutar(aux.SQLCrear());
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(FasesC.class.getName()).log(Level.SEVERE, null, ex);
        }

        btGuardarFase.setVisible(false);
        btBorrarFase.setVisible(true);
        btEditarFase.setVisible(true);
        btNuevaFase.setDisable(false);

        limpiarFases();
        cargaFasesOrigen();
    }

    @FXML
    void cargaOrigenFase(ActionEvent event) {
        limpiarFases();
        listOrigenes.clear();
        Entidad entidad = (Entidad) cbEntidad.getSelectionModel().getSelectedItem();
        Origen origen;
        Iterator<Origen> it = Query.listaOrigenFases(entidad.getId()).iterator();

        while (it.hasNext()) {
            origen = it.next();
            listOrigenes.add(origen);
        }
        lvOrigen.setItems(listOrigenes);
        lvOrigen.setVisible(false);
        lvOrigen.setVisible(true);
    }

    private void inicializaDatosFases() {
        comboEntidades = FXCollections.observableArrayList();
        cbEntidad.setItems(comboEntidades);
        comboCodigo = FXCollections.observableArrayList();
        cbCodigo.setItems(comboCodigo);
        comboTipo = FXCollections.observableArrayList();
        cbTipo.setItems(comboTipo);
        listOrigenes = FXCollections.observableArrayList();
        lvOrigen.setItems(listOrigenes);

        lvOrigen.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                final ListCell cell = new ListCell() {
                    private Text text;

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            text = new Text(item.toString());
                            text.setWrappingWidth(lvOrigen.getPrefWidth() - 30);
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

        Entidad Entidad;
        Tipo tipo;
        Iterator it = Query.listaEntidadFases().iterator();

        while (it.hasNext()) {
            Entidad = (Entidad) it.next();
            comboEntidades.add(Entidad);
        }

        it = Query.listaTipoFases().iterator();

        while (it.hasNext()) {
            tipo = (Tipo) it.next();
            comboCodigo.add(tipo);
        }

        comboTipo.add(Kind.ND);
        comboTipo.add(Kind.RS);
        comboTipo.add(Kind.RR);
    }

    private void inicializaTablaFases() {
        faseCLF.setCellValueFactory(new PropertyValueFactory<>("fase"));
        codigoCLF.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tipoCLF.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        diasCLF.setCellValueFactory(new PropertyValueFactory<>("dias"));

        tablaFases = FXCollections.observableArrayList();
        tvFases.setItems(tablaFases);
    }

    void cargaFasesOrigen() {
        tablaFases.clear();
        ModeloFases mf;
        Origen aux = (Origen) lvOrigen.getSelectionModel().getSelectedItem();
        tfOrigen.setText(aux.getNombre());
        Iterator it = Query.listaModeloFases(aux.getId()).iterator();

        while (it.hasNext()) {
            mf = (ModeloFases) it.next();
            tablaFases.add(mf);
        }

        btBorrarFase.setDisable(true);
        btEditarFase.setDisable(true);
    }

    void cargaDatosFase() {
        ModeloFases aux = (ModeloFases) tvFases.getSelectionModel().getSelectedItem();

        if (aux != null) {
            ModeloComboBox cb = new ModeloComboBox();
            cb.id.set(1);
            cb.nombre.set(aux.codigo.get());
            cbCodigo.getSelectionModel().select(cb);
            cbTipo.getSelectionModel().select(aux.getIdTipo() - 1);
            tfDias.setText(Integer.toString(aux.getDias()));
            taTexto1.setText(aux.getTexto1());
            taTexto2.setText(aux.getTexto2());
            taTexto3.setText(aux.getTexto3());
        }

        btBorrarFase.setDisable(false);
        btEditarFase.setDisable(false);
    }

    void limpiarFases() {
        cbCodigo.getSelectionModel().select(null);
        cbTipo.getSelectionModel().select(null);
        tfDias.setText(null);
        taTexto1.setText(null);
        taTexto2.setText(null);
        taTexto3.setText(null);
        tfOrigen.setText(null);
    }

    Fase getDatosFase() {
        ModeloFases aux = new ModeloFases();
        ModeloFases mf;
        ModeloComboBox mc;

        mf = (ModeloFases) tvFases.getSelectionModel().getSelectedItem();
        aux.id.set(mf.getId());
        aux.idOrigen.set(mf.getIdOrigen());
        mc = (ModeloComboBox) cbCodigo.getSelectionModel().getSelectedItem();
        aux.codigo.set(mc.getNombre());
        mc = (ModeloComboBox) cbTipo.getSelectionModel().getSelectedItem();
        aux.tipo.set(mc.getId());
        aux.texto1.set(taTexto1.getText().trim());
        aux.texto2.set(taTexto2.getText().trim());
        aux.texto3.set(taTexto3.getText().trim());
        aux.dias.set(Integer.parseInt(tfDias.getText()));

        Fase fase = new Fase();
        fase.setId(aux.getId());
        fase.setCodigo(aux.getCodigo());
        fase.setIdOrigen(aux.getIdOrigen());
        fase.setTipo(aux.getIdTipo());
        fase.setTexto1(aux.getTexto1());
        fase.setTexto2(aux.getTexto2());
        fase.setTexto3(aux.getTexto3());
        fase.setDias(aux.getDias());

        return fase;
    }

    /**
     * Listener de la lista OrigenFase
     */
    private final ListChangeListener<Origen> selectorListaOrigen
            = (ListChangeListener.Change<? extends Origen> c) -> {
                cargaFasesOrigen();
            };

    /**
     * Listener de la Tabla Fases
     */
    private final ListChangeListener<ModeloFases> selectorTablaFases
            = (ListChangeListener.Change<? extends ModeloFases> c) -> {
                cargaDatosFase();
            };
}
