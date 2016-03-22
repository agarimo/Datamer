/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamer.ctrl.testra;

import datamer.Var;
import datamer.model.testra.enty.Captura;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class CapturadorC implements Initializable {

    @FXML
    private Label lbEnlaces;

    @FXML
    private Button btFinalizar;

    @FXML
    private Button btReset;

    private Date fecha;
    private List<Captura> enlaces;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        enlaces = new ArrayList();
        initClipboard();
    }

    @FXML
    void close(ActionEvent event) {
        Var.stage.show();
        Var.popup.close();
    }

    @FXML
    void reset(ActionEvent event) {

    }
    
    public void setFecha(Date fecha){
        this.fecha=fecha;
    }

    //<editor-fold defaultstate="collapsed" desc="ClipBoard">
    private void initClipboard() {
        final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        processClipboard(cb);
        cb.addFlavorListener((FlavorEvent e) -> {
            processClipboard(cb);
        });
    }

    public void restartClipboard() {
        initClipboard();
    }

    public void processClipboard(Clipboard cb) {
        String cabecera = "/WEB_TTRA_CONSULTA/VisualizacionEdicto.faces";
        Transferable trans;
        trans = cb.getContents(null);
        if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String s = (String) trans.getTransferData(DataFlavor.stringFlavor);

                if (s.contains(cabecera)) {
                    creaDescarga(s);
                } else {
                    System.out.println("Link no válido");
                }

                StringSelection ss = new StringSelection(s);
                cb.setContents(ss, ss);
            } catch (UnsupportedFlavorException | IOException e2) {
                initClipboard();
            }
        }
    }
    //</editor-fold>

    void creaDescarga(String aux) {

        System.out.println(aux);
    }

}
