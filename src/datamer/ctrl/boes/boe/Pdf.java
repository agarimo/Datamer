package datamer.ctrl.boes.boe;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import datamer.Var;
import util.Dates;
import files.Util;

/**
 *
 * @author Agarimo
 */
public class Pdf {

    private int id;
    private String codigo;
    private Date fecha;
    private String entidad;
    private String origen;
    private String descripcion;
    private String link;
    private File ficheroPDF;
    private File ficheroTXT;
    private final String preLink = "http://www.boe.es";

    public Pdf(String datos, String entidad, String origen, Date fecha) {
        this.fecha = fecha;
        this.entidad = entidad;
        this.origen = origen;

        String[] split = datos.split(System.getProperty("line.separator"));

        for (String a : split) {

            if (a.contains("<p>")) {
                splitDescripcion(a);
            }

            if (a.contains("<a href=\"")) {
                splitLink(a);
            }
        }
        splitCodigo(this.link);
        checkFicheros();
    }

    private void checkFicheros() {
        ficheroPDF = new File(Var.ficheroPdf, Dates.imprimeFecha(fecha));
        ficheroPDF.mkdirs();
        ficheroTXT = new File(Var.ficheroTxt, Dates.imprimeFecha(fecha));
        ficheroTXT.mkdirs();
    }

    private void splitDescripcion(String descripcion) {
        descripcion = descripcion.replace("<p>", "");
        descripcion = descripcion.replace("</p>", "");
        this.descripcion = descripcion.trim();
    }

    private void splitLink(String link) {
        String[] split = link.split("title=");

        link = split[0];
        link = link.replace("<a href=\"", "");
        link = link.replace("\"", "");
        this.link = link.trim();
    }

    private void splitCodigo(String codigo) {
        String[] split = codigo.split("id=");
        this.codigo = split[1].trim();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLink() {
        return preLink + link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public void descargaPDF() throws MalformedURLException, IOException, SQLException {
        File fichero = new File(this.ficheroPDF, this.codigo + ".pdf");

        URL enlace = new URL(this.getLink());
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

    public void convertirPDF() {
        File fileO = new File(this.ficheroPDF, this.codigo + ".pdf");
        File fileD = new File(this.ficheroTXT, this.codigo + ".txt");

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

    @Override
    public String toString() {
        return codigo;
    }
}
