package datamer.ctrl.boes.boe;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Agarimo
 */
public class Publicacion {

    String entidad;
    String origen;
    String datos;
    List boletines;
    Date fecha;
    List pdfs;

    public Publicacion(String entidad, String datos, Date fecha) {
        this.fecha = fecha;
        this.entidad = entidad;
        boletines = new ArrayList();
        pdfs = new ArrayList();
        StringBuilder buffer = new StringBuilder();
        boolean print = false;
        String linea;
        String[] split = datos.split(System.getProperty("line.separator"));

        for (String a : split) {

            if (a.contains("<h6>")) {
                splitOrigen(a);
            }

            if (a.contains("<li class=\"notif\">")) {
                print = true;
            }

            if (print) {
                buffer.append(a);
                buffer.append(System.getProperty("line.separator"));
            }

            if (a.contains("</li>")) {
                print = false;
                linea = buffer.toString();
                boletines.add(linea);
                buffer = new StringBuilder();
            }
        }
        splitEntidad();
        splitPdf();
    }

    private void splitEntidad() {
        String enty = this.entidad;
        
        enty = enty.replace("<h5>", "");
        enty = enty.replace("</h5>", "");
        enty = enty.trim();
        
        this.entidad = enty;
    }

    private void splitOrigen(String origen) {
        origen = origen.replace("<h6>", "");
        origen = origen.replace("</h6>", "");
        this.origen = origen.trim();
    }

    private void splitPdf() {
        Pdf pdf;
        Iterator it = boletines.iterator();

        while (it.hasNext()) {
            pdf = new Pdf((String) it.next(), this.entidad, this.origen, this.fecha);
            pdfs.add(pdf);
        }
    }

    public void listarPdf() {
        Pdf pd;
        Iterator it = pdfs.iterator();

        while (it.hasNext()) {
            pd = (Pdf) it.next();
            System.out.println(pd.getEntidad());
            System.out.println(pd.getCodigo());
            System.out.println(pd.getOrigen());
            System.out.println(pd.getDescripcion());
            System.out.println(pd.getLink());
            System.out.println("------------------------------------------------");
        }
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public List getBoletines() {
        return boletines;
    }

    public void setBoletines(List boletines) {
        this.boletines = boletines;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List getPdfs() {
        return pdfs;
    }

    public void setPdfs(List pdfs) {
        this.pdfs = pdfs;
    }
}
