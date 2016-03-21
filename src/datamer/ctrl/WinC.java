package datamer.ctrl;

import datamer.Nav;
import datamer.Var;
import static datamer.Var.popup;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Agarimo
 */
public class WinC implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Label leftStatus;

    @FXML
    private Label rigthStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    private void addPane(Tab tab) {
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    private Tab loadPane(String pane,String nombre) {
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
    void initCaptura(ActionEvent event){
        addPane(loadPane(Nav.TESTRA_CAPTURA,"CAPTURA TESTRA"));
    }

    @FXML
    void initCruceTestra(ActionEvent event) {
        addPane(loadPane(Nav.TESTRA_CRUCE,"CRUCE TESTRA"));
    }
    
    @FXML
    void initBoesBoletines(ActionEvent event){
        addPane(loadPane(Nav.BOES_BOLETINES,"BOLETINES"));
    }
    
    @FXML
    void initBoesClasificacion(ActionEvent event){
        addPane(loadPane(Nav.BOES_CLASIFICACION,"CLASIFICACIÓN"));
    }
    
   @FXML
   void initBoesExt(ActionEvent event){
       addPane(loadPane(Nav.BOES_EXTRACCION,"EXTRACCIÓN"));
   }
   
   @FXML
   void initBoesFases(ActionEvent event){
       addPane(loadPane(Nav.BOES_FASES,"FASES"));
   }
   
   @FXML
   void initBoesPattern(ActionEvent event){
       addPane(loadPane(Nav.BOES_PATTERN,"PATRONES"));
   }
   
   @FXML
   void initTelemark (ActionEvent event){
       addPane(loadPane(Nav.TELEMARK,"CLIENTES"));
   }
}
