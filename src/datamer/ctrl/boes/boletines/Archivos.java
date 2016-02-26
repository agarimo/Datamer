package datamer.ctrl.boes.boletines;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import datamer.model.boes.ModeloBoletines;
import datamer.Var;
import datamer.ctrl.boes.Query;
import util.Dates;
import util.Files;
import util.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Archivos {

    private File dir;
    private Date fecha;
    private List<ModeloBoletines> boletines;

    public Archivos() {

    }

    public Archivos(Date fecha) {
        this.fecha = fecha;
        dir = new File(Var.ficheroUnion, Dates.imprimeFecha(fecha));
        cargaBoletines();
    }

    public Archivos(Date fecha, File dir, List boletines) {
        this.fecha = fecha;
        this.dir = dir;
        this.boletines = boletines;
    }

    private void cargaBoletines() {
        String query = "SELECT * FROM boes.vista_boletines where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(this.fecha));
        boletines = Query.listaModeloBoletines(query);
    }

    public List getBoletines() {
        return this.boletines;
    }

    public void creaArchivos() {
        caIn(boletines, fecha);
    }

    public void creaArchivos(List bol, Date fecha, String struc, String codigoUn) {
        dir=new File(Var.ficheroUnion, Dates.imprimeFecha(fecha));
        if (struc == null) {
            caIn(bol, fecha);
        } else {
            caUn(bol, fecha, struc, codigoUn);
        }
    }

    /**
     * Crea archivos unidos
     */
    private void caUn(List bol, Date fecha, String struc, String codigoUn) {
        File file;
        StringBuilder buffer = new StringBuilder();
        ModeloBoletines aux;
        Iterator<ModeloBoletines> it = bol.iterator();

        try {
            
            file = new File(dir, getNombreArchivoUn(struc, fecha, codigoUn) + ".txt");
            file.createNewFile();

            while (it.hasNext()) {
                aux = it.next();
                buffer.append(getDataArchivo(aux));
                buffer.append(System.getProperty("line.separator"));
                buffer.append(System.getProperty("line.separator"));
                buffer.append("-------------------------------------------------");
                buffer.append(System.getProperty("line.separator"));
                buffer.append("-------------------------------------------------");
                buffer.append(System.getProperty("line.separator"));
                buffer.append(System.getProperty("line.separator"));
            }

            buffer.append("SE HAN UNIDO ").append(bol.size()).append(" BOLETINES");
            buffer.append(System.getProperty("line.separator"));

            it = bol.iterator();
            while (it.hasNext()) {
                aux = it.next();
                buffer.append(aux.getCodigo());
                buffer.append(System.getProperty("line.separator"));
            }

            buffer.append("-------------------------------------------------");

            Files.escribeArchivo(file, buffer.toString());

        } catch (IOException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Crea archivos individuales
     */
    private void caIn(List bol, Date fecha) {
        File file;
        ModeloBoletines aux;
        Iterator<ModeloBoletines> it = bol.iterator();

        try {
            while (it.hasNext()) {
                aux = it.next();
                file = new File(dir, getNombreArchivo(aux.getCodigo(), fecha, aux.getEntidad()) + ".txt");
                file.createNewFile();
                Files.escribeArchivo(file, getDataArchivo(aux));
            }

        } catch (IOException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getDataArchivo(ModeloBoletines aux) {
        StringBuilder buffer;

        buffer = new StringBuilder();
        buffer.append("BCN2 ");
        buffer.append(aux.getCodigo().replace("BOE-N-20", "").replace("-", ""));
        buffer.append(System.getProperty("line.separator"));
        buffer.append(getFaseBoletin(aux.codigo.get()));
        buffer.append("BCN5 ");
        buffer.append(aux.getOrigen());
        buffer.append(System.getProperty("line.separator"));
        buffer.append(getCodigoAyutamiento(aux.getOrigen()));
        buffer.append(System.getProperty("line.separator"));
        buffer.append(getDatosBoletin(aux.getIdDescarga()));

        return buffer.toString();
    }

    private String getNombreArchivo(String codigo, Date fecha, String entidad) {
        String str = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        String ori = codigo;

        int dia = cal.get(Calendar.DAY_OF_MONTH);
        if (dia < 10) {
            str = str + "0" + dia;
        } else {
            str = str + dia;
        }

        str = str + "0";

        String anno = Integer.toString(cal.get(Calendar.YEAR));
        str = str + anno.charAt(3);

        str = str + getEntidad(entidad);

        int mes = cal.get(Calendar.MONTH);
        mes++;
        if (mes < 10) {
            str = str + mes + "A-";
        } else {
            if (mes == 10) {
                str = str + "XA-";
            }
            if (mes == 11) {
                str = str + "YA-";
            }
            if (mes == 12) {
                str = str + "ZA-";
            }
        }

        if (dia < 10) {
            str = str + "0" + dia + ".";
        } else {
            str = str + dia + ".";
        }

        if (mes < 10) {
            str = str + "0" + mes + ".";
        } else {
            str = str + mes + ".";
        }

        str = str + "--" + ori;

        return str;
    }

    private String getNombreArchivoUn(String estructura, Date fecha, String codigoUn) {
        String str = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        String ori = estructura + "-" + codigoUn;

        int dia = cal.get(Calendar.DAY_OF_MONTH);
        if (dia < 10) {
            str = str + "0" + dia;
        } else {
            str = str + dia;
        }

        str = str + "0";

        String anno = Integer.toString(cal.get(Calendar.YEAR));
        str = str + anno.charAt(3);

        str = str + "00";

        int mes = cal.get(Calendar.MONTH);
        mes++;
        if (mes < 10) {
            str = str + mes + "A-";
        } else {
            if (mes == 10) {
                str = str + "XA-";
            }
            if (mes == 11) {
                str = str + "YA-";
            }
            if (mes == 12) {
                str = str + "ZA-";
            }
        }

        if (dia < 10) {
            str = str + "0" + dia + ".";
        } else {
            str = str + dia + ".";
        }

        if (mes < 10) {
            str = str + "0" + mes + ".";
        } else {
            str = str + mes + ".";
        }

        str = str + "--" + ori;

        return str;
    }

    private String getEntidad(String entidad) {
        Sql bd;
        String aux = "";

        try {
            bd = new Sql(Var.con);
            aux = bd.getString("SELECT codigo FROM boes.entidad where nombre=" + Varios.entrecomillar(entidad));
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    private String getDatosBoletin(int id) {
        Sql bd;
        String aux;

        try {
            bd = new Sql(Var.con);
            aux = bd.getString("SELECT datos from " + Var.dbNameBoes + ".descarga where id=" + id);
            bd.close();
        } catch (SQLException ex) {
            aux = "ERROR AL GENERAR EL ARCHIVO ----- " + ex.getMessage();
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    private String getFaseBoletin(String codigo) {
        Sql bd;
        String aux = "";

        try {
            bd = new Sql(Var.con);
            aux = bd.getString("SELECT fase FROM boes.boletin where codigo=" + Varios.entrecomillar(codigo));
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    private String getCodigoAyutamiento(String nombre) {
        Sql bd;
        nombre=nombre.replace("'", "\\'");
        String aux = "";

        try {
            bd = new Sql(Var.con);
            aux = bd.getString("SELECT codigoAy FROM boes.origen where nombre=" + Varios.entrecomillar(nombre));
            bd.close();
        } catch (SQLException ex) {
            System.out.println("SELECT codigoAy FROM boes.origen where nombre=" + Varios.entrecomillar(nombre));
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }
}
