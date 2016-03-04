package datamer.ctrl.boes.boletines;


import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.Boletin;
import datamer.model.boes.enty.Fase;
import datamer.model.boes.enty.Origen;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Dates;
import util.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Fases {

    Date fecha;
    List boletines;

    public Fases(Date fecha) {
        this.fecha = fecha;
        this.boletines = getBol();
    }

    private List getBol() {
        return Query.listaBoletin("SELECT * FROM " + Var.dbNameBoes + ".boletin where idBoe in "
                + "(SELECT id FROM " + Var.dbNameBoes + ".boe where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(this.fecha)) + ")");
    }

    public List getBoletines() {
        return this.boletines;
    }

    private String getDatos(int id) {
        Sql bd;
        String str = null;

        try {
            bd = new Sql(Var.con);
            str = bd.getString("SELECT datos FROM " + Var.dbNameBoes + ".descarga where id=" + id);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    @Deprecated
    public void run() {
        Sql bd;
        Fase fase;
        Boletin aux;
        Iterator it = boletines.iterator();

        while (it.hasNext()) {

            aux = (Boletin) it.next();
            fase = compruebaFase(aux.getIdDescarga(), getFases(aux.getIdOrigen()));

            if (fase != null) {
                aux.setTipo(fase.getCodigo());
                aux.setFase(getBCN(aux.getIdOrigen(), aux.getIsEstructura()) + "-" + fase.toString());

                if (fase.getCodigo().equals("*DSC*")) {
                    aux.setIsFase(3);
                } else {
                    aux.setIsFase(2);
                }
            } else {
                aux.setFase(getBCN(aux.getIdOrigen(), aux.getIsEstructura()));
                aux.setIsFase(1);
            }

            try {
                bd = new Sql(Var.con);
                bd.ejecutar(aux.SQLEditar());
                bd.close();
            } catch (SQLException ex) {
                Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void runFase(Boletin aux) {
        Sql bd;
        Fase fase;

        fase = compruebaFase(aux.getIdDescarga(), getFases(aux.getIdOrigen()));

        if (fase != null) {
            aux.setTipo(fase.getCodigo());
            aux.setFase(getBCN(aux.getIdOrigen(), aux.getIsEstructura()) + "-" + fase.toString());

            if (fase.getCodigo().equals("*DSC*")) {
                aux.setIsFase(3);
            } else {
                aux.setIsFase(2);
            }
        } else {
            aux.setFase(getBCN(aux.getIdOrigen(), aux.getIsEstructura()));
            aux.setIsFase(1);
        }

        try {
            bd = new Sql(Var.con);
            bd.ejecutar(aux.SQLEditar());
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Fase compruebaFase(int id, List fases) {
        Fase fase = null;
        Fase aux;
        Iterator it = fases.iterator();

        while (it.hasNext()) {
            aux = (Fase) it.next();

            if (aux.contiene(getDatos(id))) {
                fase = aux;
            }
        }
        return fase;
    }

    private String getBCN(int idOrigen, int estructura) {
        Sql bd;
        String str = "";

        try {
            bd = new Sql(Var.con);
            if (estructura == -1) {
                str = "BCN1null";
            } else {
                str = bd.getString("SELECT nombre FROM " + Var.dbNameBoes + ".estructura where id=" + estructura);
            }
            str = str + bd.getString("SELECT codigoUn FROM " + Var.dbNameBoes + ".origen where id=" + idOrigen);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }

        return str;
    }

    private List getFases(int id) {
        return Query.listaFase("SELECT * FROM " + Var.dbNameBoes + ".fase where idOrigen=" + id);
    }
}
