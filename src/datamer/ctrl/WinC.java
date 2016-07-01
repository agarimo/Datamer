package datamer.ctrl;

import datamer.Nav;
import datamer.Var;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import static javafx.scene.paint.Color.GREEN;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.poi.hssf.util.HSSFColor;
import socket.ClientSocket;
import socket.enty.ModeloTarea;
import socket.enty.Request;
import socket.enty.Response;
import socket.enty.ServerRequest;

/**
 *
 * @author Agarimo
 */
public class WinC implements Initializable {

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

    private ObservableList<ModeloTarea> taskList;

    private ClientSocket client;
    private boolean keepRefresh;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        prepareSlideMenuAnimation();
        initSocketClient();
    }

    public void initTable() {
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
        TranslateTransition openNav = new TranslateTransition(new Duration(1500), taskPane);
        openNav.setToY(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(1500), taskPane);

        taskButton.setOnAction((ActionEvent evt) -> {
            if (taskPane.getTranslateY() != 0) {
                openNav.play();
            } else {
                closeNav.setToY(-180);
                closeNav.play();
            }
        });

    }

    private void initSocketClient() {
        client = new ClientSocket();
        client.setHost(Var.socketClientHost);
        client.setPort(Var.socketClientPort);
        if(client.conect()){
            serverStatus.setText("On-Line");
            conected.setFill(Color.GREENYELLOW);
        }
    }

    public void initRefresh() {
        keepRefresh = true;
        Runnable refresh = () -> {
            Thread.currentThread().setName("Refresh Thread");
            
            
            while (keepRefresh) {
                Request request=new Request(ServerRequest.STATUS);
                Response response=client.sendRequest(request);
                
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

    }

    @FXML
    void exitApp(ActionEvent event) {
        keepRefresh = false;
        if(client.disconect()){
            serverStatus.setText("Off-Line");
            conected.setFill(Color.RED);
        }
        Platform.exit();
    }

    private void addPane(Tab tab) {
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    private Tab loadPane(String pane, String nombre) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(pane));

            Tab tab = new Tab();
            tab.setText(nombre);
            tab.setContent(node);

            return tab;
        } catch (IOException ex) {
            Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @FXML
    void initCaptura(ActionEvent event) {
        addPane(loadPane(Nav.TESTRA_CAPTURA, "CAPTURA TESTRA"));
    }

    @FXML
    void initCruceTestra(ActionEvent event) {
        addPane(loadPane(Nav.TESTRA_CRUCE, "CRUCE TESTRA"));
    }

    @FXML
    void initBoesBoletines(ActionEvent event) {
        addPane(loadPane(Nav.BOES_BOLETINES, "BOLETINES"));
    }

    @FXML
    void initBoesClasificacion(ActionEvent event) {
        addPane(loadPane(Nav.BOES_CLASIFICACION, "CLASIFICACIÓN"));
    }

    @FXML
    void initBoesExt(ActionEvent event) {
        addPane(loadPane(Nav.BOES_EXTRACCION, "EXTRACCIÓN"));
    }

    @FXML
    void initBoesFases(ActionEvent event) {
        addPane(loadPane(Nav.BOES_FASES, "FASES"));
    }

    @FXML
    void initBoesPattern(ActionEvent event) {
        addPane(loadPane(Nav.BOES_PATTERN, "PATRONES"));
    }

    @FXML
    void initTelemark(ActionEvent event) {
        addPane(loadPane(Nav.TELEMARK, "CLIENTES"));
    }
}
