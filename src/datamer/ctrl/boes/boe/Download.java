package datamer.ctrl.boes.boe;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import datamer.Var;
import datamer.ctrl.boes.Query;
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

//    public void descargaPDF(String link) throws MalformedURLException, IOException, SQLException {
//        File fichero = new File("temp.pdf");
//
//        URL enlace = new URL(link);
//        URLConnection connection = enlace.openConnection();
//
//        OutputStream out;
//        try (InputStream in = connection.getInputStream()) {
//            out = new DataOutputStream(new FileOutputStream(fichero));
//            byte[] buffer = new byte[1024];
//            int sizeRead;
//            while ((sizeRead = in.read(buffer)) >= 0) {
//                out.write(buffer, 0, sizeRead);
//            }
//        }
//        out.close();
//    }
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
