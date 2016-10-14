package datamer.model.boes.enty;

import datamer.model.boes.Estado;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import datamer.Var;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import sql.Sql;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class Procesar {

    private int id;
    private LocalDate fecha;
    private String codigo;
    private String link;
    private int estructura;
    private Estado estado;

    public Procesar() {

    }

    public Procesar(LocalDate fecha, String codigo, String link, int estructura, int estado) {
        this.fecha = fecha;
        this.codigo = codigo;
        this.link = link;
        this.estructura = estructura;
        this.estado = pickEstado(estado);
    }

    public String getCodigo() {
        return codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public int getEstructura() {
        return estructura;
    }

    public int getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setEstado(int estado) {
        if (estado != 1) {
            this.estado = pickEstado(estado);
        } else {
            this.estado = checkEstado();
        }
    }

    public void setEstructura(int estructura) {
        this.estructura = estructura;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return this.codigo;
    }

    private Estado pickEstado(int estado) {
        switch (estado) {
            case 0:
                return Estado.SIN_PROCESAR;
            case 1:
                return Estado.LISTO_PROCESAR;
            case 2:
                return Estado.PROCESADO_XLSX;
            case 3:
                return Estado.ERROR_PROCESAR;
            case 4:
                return Estado.ERROR_PROCESAR;
            case 5:
                return Estado.PDF_NO_GENERADO;
            case 6:
                return Estado.XLSX_NO_GENERADO;
            default:
                throw new IllegalArgumentException();
        }
    }

    private Estado checkEstado() {
        Estado a = Estado.LISTO_PROCESAR;
        File fichero = new File(Var.ficheroEx, fecha.format(DateTimeFormatter.ISO_DATE));
        File fileXLSX = new File(fichero, codigo + ".xlsx");
        File filePDF = new File(fichero, codigo + ".pdf");

        if (!fileXLSX.exists()) {
            a = Estado.XLSX_NO_GENERADO;
        }

        if (!filePDF.exists()) {
            a = Estado.PDF_NO_GENERADO;
        }

        return a;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".procesar (id,fecha,codigo,link,estructura,estado) values("
                + this.id + ","
                + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)) + ","
                + Util.comillas(this.codigo) + ","
                + Util.comillas(this.link) + ","
                + this.estructura + ","
                + 1
                + ");";
    }

    public void SQLSetEstado(int estado) {
        String query = "UPDATE " + Var.dbNameBoes + ".procesar SET "
                + "estado=" + estado + " "
                + "WHERE id=" + this.id;

        try {
            Sql bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Procesar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SQLEliminarEstructura() {
        String query = "UPDATE " + Var.dbNameBoes + ".procesar SET "
                + "estructura=-1 "
                + "WHERE id=" + this.id;

        try {
            Sql bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Procesar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
