/*
 * The MIT License
 *
 * Copyright 2016 agari.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package datamer.ctrl.boes.ext;

import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.ModeloBoletines;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Sql;
import tools.LoadFile;
import tools.Util;

/**
 *
 * @author Agárimo
 */
public class TXT {

    private LocalDate fecha;
    private File fichero;
    private List<ModeloBoletines> boletines;

    public TXT(LocalDate fecha, File fichero) {
        this.fecha = fecha;
        this.fichero = fichero;
        boletines = Query.listaModeloBoletines("SELECT * FROM " + Var.dbNameBoes + ".vista_boletines "
                + "where "
                + "fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)) + " "
                + "and "
                + "codigo in "
                + "(select codigo from " + Var.dbNameBoes + ".procesar where estructura=-1 and estado=1)");
    }

    public void run() {

    }

    private void caIn(List bol, Date fecha) {
        File file;
        ModeloBoletines aux;
        Iterator<ModeloBoletines> it = bol.iterator();

        try {
            while (it.hasNext()) {
                aux = it.next();
                file = new File(fichero, getNombreArchivo(aux.getCodigo(), fecha, aux.getEntidad()) + ".txt");
                file.createNewFile();
                LoadFile.writeFile(file, getDataArchivo(aux));
            }

        } catch (IOException ex) {
            Logger.getLogger(TXT.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private String getEntidad(String entidad) {
        Sql bd;
        String aux = "";

        try {
            bd = new Sql(Var.con);
            aux = bd.getString("SELECT codigo FROM boes.entidad where nombre=" + Util.comillas(entidad));
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(TXT.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
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
        buffer.append(getDatosBoletin(aux.getCodigo()));

        return buffer.toString();
    }

    private String getFaseBoletin(String codigo) {
        Sql bd;
        String aux = "";

        try {
            bd = new Sql(Var.con);
            aux = bd.getString("SELECT fase FROM boes.boletin where codigo=" + Util.comillas(codigo));
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(TXT.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    private String getCodigoAyutamiento(String nombre) {
        Sql bd;
        String aux = "";

        try {
            bd = new Sql(Var.con);
            aux = bd.getString("SELECT codigoAy FROM boes.origen where nombre=" + Util.comillas(nombre));
            bd.close();
        } catch (SQLException ex) {
            System.out.println("SELECT codigoAy FROM boes.origen where nombre=" + Util.comillas(nombre));
            Logger.getLogger(TXT.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }
    
      private String getDatosBoletin(String codigo) {
        Sql bd;
        String aux;

        try {
            bd = new Sql(Var.con);
            aux = bd.getString("SELECT datos from " + Var.dbNameServer + ".publicacion where codigo=" + Util.comillas(codigo));
            bd.close();
        } catch (SQLException ex) {
            aux = "ERROR AL GENERAR EL ARCHIVO ----- " + ex.getMessage();
            Logger.getLogger(TXT.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

}
