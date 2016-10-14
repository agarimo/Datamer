package datamer.ctrl.boes.ext.script;

import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.ModeloBoletines;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Dates;
import sql.Sql;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class ScriptOrigen {

    String query;
    List<ModeloBoletines> list;

    public ScriptOrigen() {
        query = "SELECT * FROM "+Var.dbNameBoes+".vista_boletines where idOrigen=372";
        list = Query.listaModeloBoletines(query);
    }

    public ScriptOrigen(LocalDate fecha) {
        query = "SELECT * FROM "+Var.dbNameBoes+".vista_boletines where idOrigen=372 and fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE));
        list = Query.listaModeloBoletines(query);
    }

    public void run() {
        Sql bd;
        String str;
        ModeloBoletines aux;
        Iterator<ModeloBoletines> it = list.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = it.next();
                str = getAy(aux.getCodigo()).toUpperCase();
                if (!str.equals("")) {
                    bd.ejecutar(getQuery(aux.getIdBoletin(), str));
                }
            }

            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(ScriptOrigen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getAy(String codigo) {
        String rtn = "";
        String cabecera = "Ayuntamiento de ";
        String rx = "[.]{1}";
        String post = "08/DI-";
        String aux = Query.getDesData(codigo);
        String[] split = aux.split(System.lineSeparator());

        try {
            for (String split1 : split) {
                if (split1.contains(cabecera)) {
                    String[] sp = split1.split(rx);
                    rtn = sp[0].replace(cabecera, post);
                }
            }
        } catch (Exception e) {
            //
        }
        return rtn;
    }

    private String getQuery(int idBoletin, String organismo) {
        return "UPDATE " + Var.dbNameBoes + ".multa SET organismo=" + Util.comillas(organismo) + " WHERE idBoletin=" + idBoletin;
    }
}
