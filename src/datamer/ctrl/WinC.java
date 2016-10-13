package datamer.ctrl;

import static datamer.Modulos.*;
import datamer.Var;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import socket.ClientSocket;
import socket.enty.ModelTask;
import socket.enty.ModeloTarea;
import socket.enty.Request;
import socket.enty.Response;
import socket.enty.ServerRequest;
import socket.enty.ServerResponse;

/**
 *
 * @author Agarimo
 */
public class WinC implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML VAR">
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label serverStatus;

    @FXML
    private Rectangle conected;

    @FXML
    private Label taskStatus;

    @FXML
    private Label lbEjecutar;

    @FXML
    private AnchorPane taskPane;

    @FXML
    private TableView tabla;

    @FXML
    private TableColumn<ModeloTarea, String> tareaCL;

    @FXML
    private TableColumn<ModeloTarea, String> propietarioCL;

    @FXML
    private TableColumn<ModeloTarea, String> estadoCL;

    @FXML
    private TableColumn<ModeloTarea, String> porcentajeCL;

    @FXML
    private Button taskButton;
    //</editor-fold>

    private PopOver popOver;
    private Node tasker;
    private LaunchTaskC taskerController;
    private static String MAIN_TASKER = "/datamer/view/LaunchTask.fxml";
    private ObservableList<ModeloTarea> taskList;

    private ClientSocket client;
    private boolean keepRefresh;
    private boolean isConected;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initTasker();
            if (initSocketClient()) {
                initRefresh();
            }
        } catch (IOException ex) {
            Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTasker() throws IOException {
        initTable();
        prepareSlideMenuAnimation();

        FXMLLoader loader = new FXMLLoader();
        tasker = loader.load(getClass().getResourceAsStream(MAIN_TASKER));
        taskerController = loader.getController();
        taskerController.setParentController(this);

        popOver = new PopOver();
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.arrowSizeProperty().setValue(12);
        popOver.arrowIndentProperty().setValue(13);
        popOver.arrowLocationProperty().setValue(ArrowLocation.LEFT_BOTTOM);
        popOver.cornerRadiusProperty().setValue(7);
        popOver.headerAlwaysVisibleProperty().setValue(false);
        popOver.setAnimated(true);
        popOver.setContentNode(tasker);
    }

    private void initTable() {
        tareaCL.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tareaCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                super.setAlignment(Pos.CENTER);
                if (!isEmpty()) {
                    text = new Text(item);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });
        propietarioCL.setCellValueFactory(new PropertyValueFactory<>("propietario"));
        propietarioCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                super.setAlignment(Pos.CENTER);
                if (!isEmpty()) {
                    text = new Text(item);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });
        porcentajeCL.setCellValueFactory(new PropertyValueFactory<>("porcentaje"));
        porcentajeCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                super.setAlignment(Pos.CENTER);
                if (!isEmpty()) {
                    text = new Text(item);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });
        estadoCL.setCellValueFactory(new PropertyValueFactory<>("progreso"));
        estadoCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                super.setAlignment(Pos.CENTER_LEFT);
                if (!isEmpty()) {
                    text = new Text(item);
                    text.setWrappingWidth(estadoCL.getWidth() - 10);
                    this.setWrapText(true);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });

        taskList = FXCollections.observableArrayList();
        tabla.setItems(taskList);
    }

    private void prepareSlideMenuAnimation() {
        TranslateTransition openNav = new TranslateTransition(new Duration(350), taskPane);
        openNav.setToY(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), taskPane);

        taskButton.setOnAction((ActionEvent evt) -> {
            if (taskPane.getTranslateY() != 0) {
                openNav.play();
            } else {
                closeNav.setToY(-180);
                closeNav.play();
            }
        });

    }

    private boolean initSocketClient() {
        try {
            client = new ClientSocket();
            client.setHost(Var.socketClientHost);
            client.setPort(Var.socketClientPort);
            client.conect();

            serverStatus.setText("On-Line");
            conected.setFill(Color.GREENYELLOW);
            isConected = true;
            taskButton.setDisable(false);
            return true;

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error en conexión - " + ex.getMessage());
            serverStatus.setText("Off-Line");
            conected.setFill(Color.RED);
            isConected = false;
            taskButton.setDisable(true);
            taskStatus.setText("Sin conexión");
            return false;
        }
    }

    public void initRefresh() {
        keepRefresh = true;
        Runnable refresh = () -> {
            List<ModelTask> list;
            Thread.currentThread().setName("Refresh Thread");

            while (keepRefresh) {
                Request request = new Request(ServerRequest.STATUS);
                Response response = client.sendRequest(request);
                list = (List<ModelTask>) response.getParametros().get(0);
                taskList.clear();

                list.stream().forEach((aux) -> {
                    taskList.add(aux.toModeloTarea());
                });

                Platform.runLater(() -> {
                    if (taskList.isEmpty()) {
                        taskStatus.setText("No hay tareas en ejecución");
                    } else if (taskList.size() > 1) {
                        taskStatus.setText(taskList.size() + " tareas en ejecución");
                    } else {
                        taskStatus.setText(taskList.size() + " tarea en ejecución");
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Var.executor.execute(refresh);
    }

    @FXML
    void launchTask(Event event) {
        popOver.show(lbEjecutar);
    }

    public void launchTask(ModeloTarea mt) {
        popOver.hide();

        Runnable launch = () -> {
            List<ModelTask> list = new ArrayList();
            Thread.currentThread().setName("Launch Thread");

            Request request = new Request(ServerRequest.RUN_TASK);
            list.add(new ModelTask(mt));
            request.setParametros(list);
            Response response = client.sendRequest(request);

            if (response.getResponse() != ServerResponse.OK) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Existe otro proceso ejecutando la tarea.");
                alert.showAndWait();
            }
        };
        Var.executor.execute(launch);

    }

    @FXML
    void exitApp(ActionEvent event) {
        keepRefresh = false;

        if (isConected) {
            if (client.disconect()) {
                serverStatus.setText("Off-Line");
                conected.setFill(Color.RED);
                isConected = false;
            }
        }

        Var.executor.shutdown();
        Var.xit();
        Platform.exit();
    }

    private void addPane(Tab tab) {
        Tab aux = contains(tab);

        if (contains(tab) != null) {
            tabPane.getSelectionModel().select(aux);
        } else {
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }
    }

    private Tab contains(Tab tab) {
        Tab aux;
        Iterator it = tabPane.getTabs().iterator();

        while (it.hasNext()) {
            aux = (Tab) it.next();

            if (tab.getId().equals(aux.getId())) {
                return aux;
            }
        }

        return null;
    }

    private Tab loadPane(String pane, String nombre) {
        Node node = loadNode(pane);

        Tab tab = new Tab();
        tab.setId(nombre);
        tab.setText(nombre);
        tab.setContent(node);

        return tab;
    }

    private Node loadNode(String pane) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(pane));
            return node;
        } catch (IOException ex) {
            Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @FXML
    void initBoesBoletines(ActionEvent event) {
        addPane(loadPane(BOES_BOLETINES.getRuta(), BOES_BOLETINES.getNombre()));
    }

    @FXML
    void initBoesClasificacion(ActionEvent event) {
        Tab tab = loadPane(BOES_CLASIFICACION.getRuta(), BOES_CLASIFICACION.getNombre());
        addPane(tab);
    }

    @FXML
    void initBoesExt(ActionEvent event) {
        Tab tab = loadPane(BOES_EXTRACCION.getRuta(), BOES_EXTRACCION.getNombre());
        addPane(tab);
    }

    @FXML
    void initBoesPattern(ActionEvent event) {
        Tab tab = loadPane(BOES_PATTERN.getRuta(), BOES_PATTERN.getNombre());
        addPane(tab);
    }

    @FXML
    void initBoesFases(ActionEvent event) {
        Tab tab = loadPane(BOES_FASES.getRuta(),BOES_FASES.getNombre());
        addPane(tab);
    }

    @FXML
    void initTelemark(ActionEvent event) {
        Tab tab = loadPane(TELEMARK.getRuta(), TELEMARK.getNombre());
        addPane(tab);
    }
}
