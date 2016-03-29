package datamer.ctrl.boes;

import datamer.Var;
import datamer.model.boes.ModeloFases;
import datamer.model.boes.Kind;
import datamer.model.boes.Plazo;
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
import sql.Sql;

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
    ComboBox cbPlazo;

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
    ObservableList<Plazo> comboPlazo;
    ObservableList<Origen> listOrigenes;
    ObservableList<ModeloFases> tablaFases;

    @FXML
    void faseDelete(ActionEvent event) {
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

        clear();
        loadFases();
    }

    @FXML
    void faseEdit(ActionEvent event) {
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

        clear();
        loadFases();
    }

    @FXML
    void faseNew(ActionEvent event) {
        btNuevaFase.setDisable(true);
        Origen origen = (Origen) lvOrigen.getSelectionModel().getSelectedItem();
        btGuardarFase.setVisible(true);
        ModeloFases aux = new ModeloFases();
        aux.id.set(0);
        aux.idOrigen.set(origen.getId());
        aux.codigo.set(null);
        aux.plazo.set(null);
        aux.tipo.set(1);

        tablaFases.add(0, aux);
        btBorrarFase.setVisible(false);
        btEditarFase.setVisible(false);

        tvFases.getSelectionModel().select(0);
        tvFases.scrollTo(0);
        tvFases.requestFocus();
    }

    @FXML
    void faseSave(ActionEvent event) {
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

        clear();
        loadFases();
    }

    void clear() {
        cbCodigo.getSelectionModel().select(null);
        cbTipo.getSelectionModel().select(null);
        cbPlazo.getSelectionModel().select(null);
        taTexto1.setText(null);
        taTexto2.setText(null);
        taTexto3.setText(null);
        tfOrigen.setText(null);
    }

    Fase getDatosFase() {
        ModeloFases mf = (ModeloFases) tvFases.getSelectionModel().getSelectedItem();
        Tipo tipo = (Tipo) cbCodigo.getSelectionModel().getSelectedItem();
        Kind kind = (Kind) cbTipo.getSelectionModel().getSelectedItem();
        Plazo plazo = (Plazo) cbPlazo.getSelectionModel().getSelectedItem();

        Fase fase = new Fase();
        fase.setId(mf.getId());
        fase.setCodigo(tipo.getId());
        fase.setIdOrigen(mf.getIdOrigen());
        fase.setTipo(kind.getValue());
        fase.setTexto1(taTexto1.getText().trim());
        fase.setTexto2(taTexto2.getText().trim());
        fase.setTexto3(taTexto3.getText().trim());
        fase.setDias(plazo.getValue());

        return fase;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTabla();
        initializeItems();

        final ObservableList<Origen> ls2 = lvOrigen.getSelectionModel().getSelectedItems();
        ls2.addListener(selectorListaOrigen);

        final ObservableList<ModeloFases> ls3 = tvFases.getSelectionModel().getSelectedItems();
        ls3.addListener(selectorTablaFases);
    }

    private void initializeItems() {
        comboEntidades = FXCollections.observableArrayList();
        cbEntidad.setItems(comboEntidades);
        comboCodigo = FXCollections.observableArrayList();
        cbCodigo.setItems(comboCodigo);
        comboTipo = FXCollections.observableArrayList();
        cbTipo.setItems(comboTipo);
        comboPlazo = FXCollections.observableArrayList();
        cbPlazo.setItems(comboPlazo);
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
                            text.setWrappingWidth(lvOrigen.getWidth() - 20);
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

        comboPlazo.add(Plazo.D10);
        comboPlazo.add(Plazo.D15);
        comboPlazo.add(Plazo.D20);
        comboPlazo.add(Plazo.M1);
        comboPlazo.add(Plazo.M2);
    }

    private void initializeTabla() {
        faseCLF.setCellValueFactory(new PropertyValueFactory<>("fase"));
        codigoCLF.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tipoCLF.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        diasCLF.setCellValueFactory(new PropertyValueFactory<>("dias"));

        faseCLF.prefWidthProperty().bind(tvFases.widthProperty().multiply(0.30));
        codigoCLF.prefWidthProperty().bind(tvFases.widthProperty().multiply(0.30));
        tipoCLF.prefWidthProperty().bind(tvFases.widthProperty().multiply(0.29));
        diasCLF.prefWidthProperty().bind(tvFases.widthProperty().multiply(0.10));

        tablaFases = FXCollections.observableArrayList();
        tvFases.setItems(tablaFases);
    }

    void loadData() {
        ModeloFases aux = (ModeloFases) tvFases.getSelectionModel().getSelectedItem();

        if (aux != null) {
            Tipo tipo = new Tipo();
            tipo.setId(aux.getCodigo());
            tipo.setNombre("");
            cbCodigo.getSelectionModel().select(tipo);
            cbTipo.getSelectionModel().select(aux.getIdTipo() - 1);
            cbPlazo.getSelectionModel().select(loadDataGetPlazo(aux.getPlazo()));
            taTexto1.setText(aux.getTexto1());
            taTexto2.setText(aux.getTexto2());
            taTexto3.setText(aux.getTexto3());
        }

        btBorrarFase.setDisable(false);
        btEditarFase.setDisable(false);
    }

    Plazo loadDataGetPlazo(String plazo) {
        switch (plazo) {
            case "10D":
                return Plazo.D10;
            case "15D":
                return Plazo.D15;
            case "20D":
                return Plazo.D20;
            case "1M":
                return Plazo.M1;
            case "2M":
                return Plazo.M2;
            default:
                return null;
        }
    }

    void loadFases() {
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

    @FXML
    void loadOrigen(ActionEvent event) {
        clear();
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

    /**
     * Listener de la lista OrigenFase
     */
    private final ListChangeListener<Origen> selectorListaOrigen
            = (ListChangeListener.Change<? extends Origen> c) -> {
                loadFases();
            };

    /**
     * Listener de la Tabla Fases
     */
    private final ListChangeListener<ModeloFases> selectorTablaFases
            = (ListChangeListener.Change<? extends ModeloFases> c) -> {
                loadData();
            };
}
