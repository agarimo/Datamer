package datamer.ctrl.boes.boe;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import datamer.model.boes.ModeloBoes;
import datamer.Var;
import datamer.model.boes.enty.Boletin;
import datamer.model.boes.enty.Descarga;
import datamer.model.boes.enty.Entidad;
import datamer.model.boes.enty.Origen;
import datamer.model.boes.enty.Publicacion;
import files.LoadFile;
import files.Util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Insercion {

    private Sql bd;
    private File pdf;
    private File txt;

    public Insercion() {
        initFiles();
    }

    private void initFiles() {
        pdf = new File(Var.fileSystem, "dwl.pdf");
        txt = new File(Var.fileSystem, "dwl.txt");
    }

    public boolean clean() {
        pdf.delete();
        txt.delete();

        return true;
    }

    public void insertaBoletin(Publicacion aux) {
        Boletin bol = new Boletin();

        try {
            bd = new Sql(Var.con);

            bol.setIdOrigen(getIdOrigen(aux.getEntidad(), aux.getOrigen()));
            bol.setIdBoe(getIdBoe(aux.getFecha().format(DateTimeFormatter.ISO_DATE)));
            bol.setIdDescarga(getIdDescarga(aux.getCodigo(), aux.getLink()));
            bol.setCodigo(aux.getCodigo());
            bol.setTipo("*711*");
            bol.setFase("BCN1");
            bol.setIsFase(0);
            bol.setIsEstructura(0);
            bol.setIdioma(0);

            bd.ejecutar(bol.SQLCrear());
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getIdOrigen(String entidad, String origen) throws SQLException {
        int aux;
        Origen or = new Origen();
        or.setIdEntidad(getIdEntidad(entidad));
        or.setNombre(origen);

        aux = bd.buscar(or.SQLBuscar());

        if (aux == -1) {
            bd.ejecutar(or.SQLCrear());
            aux = bd.ultimoRegistro();
        }

        return aux;
    }

    private int getIdEntidad(String nombre) throws SQLException {
        int aux;
        Entidad en = new Entidad();
        en.setNombre(nombre);

        aux = bd.buscar(en.SQLBuscar());

        if (aux == -1) {
            bd.ejecutar(en.SQLCrear());
            aux = bd.ultimoRegistro();
        }

        return aux;
    }

    private int getIdBoe(String fecha) throws SQLException {
        return bd.getInt("SELECT * FROM " + Var.dbNameBoes + ".boe where fecha=" + Varios.entrecomillar(fecha));
    }

    private int getIdDescarga(String codigo, String link) throws SQLException {
        int aux;
        Descarga ds = new Descarga();
        ds.setCodigo(codigo);
        ds.setLink(link);
        ds.setDatos(getDatos(link));

        aux = bd.buscar(ds.SQLBuscar());

        if (aux == -1) {
            bd.ejecutar(ds.SQLCrear());
            aux = bd.ultimoRegistro();
        }
        return aux;
    }

    private String getDatos(String link) {
        if (descarga(link)) {
            LoadFile lf = new LoadFile(txt);
            return lf.getFileData();
        } else {
            return "ERROR EN DESCARGA Y CONVERSIÓN.";
        }
    }

    private boolean descarga(String link) {
        try {
            files.Download.downloadFILE(link, pdf);
            files.Pdf.convertPDF(pdf, txt);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void guardaStatsD(List lista) {
        ModeloBoes aux;
        Iterator it = lista.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = (ModeloBoes) it.next();
                bd.ejecutar(aux.SQLUpdate());
            }

            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardaStatsS(List lista) {
        ModeloBoes aux;
        Iterator it = lista.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = (ModeloBoes) it.next();
                bd.ejecutar(aux.SQLUpdate());
            }

            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
