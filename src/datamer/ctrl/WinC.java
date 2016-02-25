/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamer.ctrl;

import datamer.Nav;
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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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
    }

    private Tab loadPane(String pane) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(pane));

            Tab tab = new Tab();
            tab.setText("TESTRA");
            tab.setContent(node);
            

            return tab;
        } catch (IOException ex) {
            Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @FXML
    void initTestra(ActionEvent event) {
        System.out.println("initTestra");
//        addPane(loadPane(Nav.BOES_CLASIFICACION));
    }
    
    @FXML
    void initBoesClasificacion(ActionEvent event){
        System.out.println("initBoesClasificacion");
        addPane(loadPane(Nav.BOES_CLASIFICACION));
    }

}
