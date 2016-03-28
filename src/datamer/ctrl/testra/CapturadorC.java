package datamer.ctrl.testra;

import datamer.Var;
import datamer.model.testra.enty.Captura;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sql.Sql;

/**
 * FXML Controller class
 *
 * @author Agarimo
 */
public class CapturadorC implements Initializable {

    @FXML
    private VBox rootPane;
    
    @FXML
    private Label lbEnlaces;

    @FXML
    private Button btFinalizar;

    @FXML
    private Button btReset;

    private Date fecha;
    private List<String> captura;
    private List<Captura> enlaces;

    private final String pre = "https://sedeapl.dgt.gob.es/WEB_TTRA_CONSULTA/VisualizacionEdicto.faces?params=";
    private final String post = "%26subidioma%3Des";

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        enlaces = new ArrayList();
        captura = new ArrayList();
        initClipboard();
    }

    @FXML
    void close(ActionEvent event) {
        insertCapturas();
        Var.stage.show();
        Var.popup.close();
    }

    void insertCapturas() {
        Platform.runLater(() -> {
            rootPane.setCursor(Cursor.WAIT);
        });
        
        try {
            Sql bd = new Sql(Var.con);

            for (Captura enlace : enlaces) {
                bd.ejecutar(enlace.SQLCrear());
            }

            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(CapturadorC.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Platform.runLater(() -> {
            rootPane.setCursor(Cursor.DEFAULT);
        });
    }

    @FXML
    void reset(ActionEvent event) {
        initClipboard();
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
        loadFecha(fecha);
    }

    private void loadFecha(Date fecha) {
        captura.addAll(Query.listaCapturaParam(fecha));

        Platform.runLater(() -> {
            lbEnlaces.setText(Integer.toString(captura.size()));
        });
    }

    //<editor-fold defaultstate="collapsed" desc="ClipBoard">
    private void initClipboard() {
        final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        processClipboard(cb);
        cb.addFlavorListener((FlavorEvent e) -> {
            processClipboard(cb);
        });
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
        Captura cap = new Captura();
        aux = aux.replace(pre, "");
        aux = aux.replace(post, "");

        cap.setParametros(aux);
        cap.setFecha(fecha);
        cap.setIdEdicto("Sin descargar");
        cap.setCsv("Sin descargar");
        cap.setDatos("Sin descargar");

        if (!checkCaptura(cap)) {
            captura.add(cap.getParametros());
            enlaces.add(cap);
        }

        Platform.runLater(() -> {
            lbEnlaces.setText(Integer.toString(captura.size()));
        });

    }

    boolean checkCaptura(Captura cap) {
        return captura.contains(cap.getParametros());
    }

}
