package datamer.ctrl;

import datamer.Nav;
import datamer.Var;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.util.Duration;

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
    private Label taskStatus;

    @FXML
    private AnchorPane taskPane;

    @FXML
    private TableView tabla;

    @FXML
    private TableColumn tareaCL;

    @FXML
    private TableColumn propietarioCL;

    @FXML
    private TableColumn porcentajeCL;

    @FXML
    private TableColumn estadoCL;

    @FXML
    private Button taskButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareSlideMenuAnimation();
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
