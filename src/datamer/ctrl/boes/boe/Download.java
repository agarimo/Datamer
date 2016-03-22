package datamer.ctrl.boes.boe;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.Descarga;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import files.Util;
import sql.Sql;

/**
 *
 * @author Agarimo
 */
public class Download extends Thread {

    private int modo;
    private List list;

    public Download() {
        this.list = new ArrayList();
    }

    private void cargaList() {
        this.list = Query.listaDescargaPendiente();
    }

    @Override
    public void run() {
        cargaList();
        if (!Var.boesIsDownloading) {
            Var.boesIsDownloading = true;

            if (!list.isEmpty()) {
                try {
                    descarga();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            Var.boesIsDownloading = false;
        }
    }

    public List getListado() {
        return Query.listaDescargaPendiente();
    }

    public void descarga(Descarga aux) {
        try {
            Sql bd;
            String datos;

            bd = new Sql(Var.con);
            descargaPDF(aux.getLink());
            convertirPDF();
            datos = Util.leeArchivo(new File("temp.txt"));
            datos = datos.replace("'", "´");
            aux.setDatos(datos);
            bd.ejecutar(aux.SQLSetDatos());
//            bd.ejecutar("UPDATE " + Variables.nombreBD + ".boletin SET estado=1 where idDescarga=" + aux.getId());

            new File("temp.txt").delete();
            new File("temp.pdf").delete();
            bd.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void descarga() throws SQLException, IOException {
        Sql bd;
        Descarga aux;
        String datos;
        Iterator it = list.iterator();

        bd = new Sql(Var.con);

        while (it.hasNext()) {
            aux = (Descarga) it.next();

            descargaPDF(aux.getLink());
            convertirPDF();
            datos = Util.leeArchivo(new File("temp.txt"));
            datos = datos.replace("'", "´");
            aux.setDatos(datos);
            bd.ejecutar(aux.SQLSetDatos());
//            bd.ejecutar("UPDATE " + Variables.nombreBD + ".boletin SET estado=1 where idDescarga=" + aux.getId());

            new File("temp.txt").delete();
            new File("temp.pdf").delete();
        }

        bd.close();
    }

    public void descargaPDF(String link) throws MalformedURLException, IOException, SQLException {
        File fichero = new File("temp.pdf");

        URL enlace = new URL(link);
        URLConnection connection = enlace.openConnection();

        OutputStream out;
        try (InputStream in = connection.getInputStream()) {
            out = new DataOutputStream(new FileOutputStream(fichero));
            byte[] buffer = new byte[1024];
            int sizeRead;
            while ((sizeRead = in.read(buffer)) >= 0) {
                out.write(buffer, 0, sizeRead);
            }
        }
        out.close();
    }

    public static void descargaPDF(String link, File destino) {
        try {
            URL enlace = new URL(link);
            URLConnection connection = enlace.openConnection();

            OutputStream out;
            try (InputStream in = connection.getInputStream()) {
                out = new DataOutputStream(new FileOutputStream(destino));
                byte[] buffer = new byte[1024];
                int sizeRead;
                while ((sizeRead = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, sizeRead);
                }
            }
            out.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void convertirPDF() {
        File fileO = new File("temp.pdf");
        File fileD = new File("temp.txt");

        try {
            fileD.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Pdf.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            FileWriter fw = new FileWriter(fileD.getAbsolutePath());
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                PdfReader pr = new PdfReader(fileO.getAbsolutePath());
                int pNum = pr.getNumberOfPages();
                for (int page = 1; page <= pNum; page++) {
                    String text = PdfTextExtractor.getTextFromPage(pr, page);
                    bw.write(text);
                    bw.newLine();
                }
                bw.flush();
                pr.close();
                bw.close();
                fw.close();
            }
            fixTxt(fileD);

        } catch (Exception ex) {
            Logger.getLogger(Publicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fixTxt(File txt) {
        String datos = Util.leeArchivo(txt);
        Util.escribeArchivo(txt, datos);
    }
}
