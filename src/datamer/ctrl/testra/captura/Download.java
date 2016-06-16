package datamer.ctrl.testra.captura;

import datamer.Var;
import datamer.ctrl.tkm.Query;
import datamer.model.testra.ModeloCaptura;
import datamer.model.testra.enty.Captura;
import tools.LoadFile;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que gestiona la descarga de edictos del testra.
 *
 * @author Agárimo
 */
public class Download {

    Captura cap;

    public void descargar(ModeloCaptura aux) {
        try {
            File pdf = new File("temp.pdf");
            File file = new File("temp.txt");
            cap = new Captura();
            cap.setId(aux.getId());
            cap.setParametros(aux.getParametros());

            String datos = tools.Download.downloadURL(generaEnlace(cap.getParametros(), false));
            cap.setCodigo(getId(datos));
            cap.setCsv(getCsv(datos));

            tools.Download.downloadFILE(generaEnlace(cap.getParametros(), true), pdf);
            tools.Pdf.convertPDF(pdf, file);

            datos = LoadFile.readFile(file);

            cap.setDatos(datos);
            cap.setEstado(2);
            cap.setEstadoCruce(0);
        } catch (IOException ex) {
            cap.setEstado(1);
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }
        Query.ejecutar(cap.SQLsetDatos());
    }
    

    /**
     * Método que extrae el id del edicto del documento pasado como parámetro.
     *
     * @param datos Edicto a comprobar.
     * @return devuelve un String con el id del Edicto.
     */
    private String getId(String datos) {
        String str = "";
        int a = datos.indexOf(". ");

        if (a > 0) {
            str = datos.substring(a + 2, a + 24 - 3);
        }
        return str;
    }

    private String getCsv(String datos) {
        Pattern pt;
        Matcher mt;
        String aux;

        pt = Pattern.compile("CSV: [A-Z0-9]{6}-[A-Z0-9]{6}-[A-Z0-9]{6}-[A-Z0-9]{6}");
        mt = pt.matcher(datos);

        if (mt.find()) {
            aux = mt.group();
            aux = aux.replace("CSV: ", "");
        } else {
            aux = "NOT FOUND";
        }
        return aux;
    }

    /**
     * Método que genera un enlace en base al tipo pasado como parámetro.
     *
     * @param param parámetros del enlace.
     * @param tipo tipo de enlace (True=Pdf, False=Html).
     * @return
     */
    public String generaEnlace(String param, boolean tipo) {
        if (tipo) {
            return Var.testraUrl + param + Var.testraPdf;
        } else {
            return Var.testraUrl + param + Var.testraHtml;
        }
    }
}
