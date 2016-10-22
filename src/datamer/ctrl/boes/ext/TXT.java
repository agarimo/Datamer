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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private File fichero;
    private LocalDate fecha;
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
        boletines.forEach((item) -> {
            LoadFile.writeFile(new File(fichero, buildFileName(item)), buildFile(item));
        });
    }

    private String buildFileName(ModeloBoletines mb) {
        StringBuilder sb = new StringBuilder();
        String anno = "0" + Integer.toString(fecha.getYear()).charAt(3);
        String day = fecha.format(DateTimeFormatter.ofPattern("dd"));
        String month = fecha.format(DateTimeFormatter.ofPattern("MM"));

        sb.append(day)
                .append(anno)
                .append(mb.getCodigoEntidad());

        switch (fecha.getMonthValue()) {
            case 10:
                sb.append("XA-");
                break;
            case 11:
                sb.append("YA-");
                break;
            case 12:
                sb.append("ZA-");
                break;
            default:
                sb.append(month)
                        .append("A-");
                break;
        }

        sb.append(day)
                .append(".")
                .append(month)
                .append(".")
                .append("--")
                .append(mb.getCodigo())
                .append(".txt");

        return sb.toString();
    }

    private String buildFile(ModeloBoletines aux) {
        StringBuilder buffer;

        buffer = new StringBuilder();
        buffer.append("BCN2 ");
        buffer.append(aux.getCodigo().replace("BOE-N-20", "").replace("-", ""));
        buffer.append(System.getProperty("line.separator"));
        buffer.append(aux.getFase());
        buffer.append("BCN5 ");
        buffer.append(aux.getOrigen());
        buffer.append(System.getProperty("line.separator"));
        buffer.append(aux.getCodigoAy());
        buffer.append(System.getProperty("line.separator"));
        buffer.append(getRawData(aux.getCodigo()));

        return buffer.toString();
    }

    private String getRawData(String codigo) {
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
