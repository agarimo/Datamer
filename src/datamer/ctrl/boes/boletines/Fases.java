package datamer.ctrl.boes.boletines;

import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.Boletin;
import datamer.model.boes.enty.Fase;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Dates;
import sql.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Fases {

    Sql bd;
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

    private void conectar() {
        try {
            bd = new Sql(Var.con);
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void desconectar() {
        try {
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getDatos(String codigo) {
        String str = null;

        try {
            str = bd.getString("SELECT datos FROM " + Var.dbNameServer + ".publicacion where codigo=" + Varios.comillas(codigo));
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }


    public void run(Boletin aux) {
        Fase fase;
        conectar();
        
        fase = compruebaFase(aux.getCodigo(), getFases(aux.getIdOrigen()));

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
            bd.ejecutar(aux.SQLEditar());
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
        desconectar();
    }

    private Fase compruebaFase(String codigo, List fases) {
        String datos = getDatos(codigo);
        Fase fase = null;
        Fase aux;
        Iterator it = fases.iterator();

        while (it.hasNext()) {
            aux = (Fase) it.next();

            if (aux.contiene(datos)) {
                fase = aux;
            }
        }
        return fase;
    }

    private String getBCN(int idOrigen, int estructura) {
        String str = "";

        try {
            if (estructura == -1) {
                str = "BCN1null";
            } else {
                str = bd.getString("SELECT nombre FROM " + Var.dbNameBoes + ".estructura where id=" + estructura);
            }
            str = str + bd.getString("SELECT codigoUn FROM " + Var.dbNameBoes + ".origen where id=" + idOrigen);
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }

        return str;
    }

    private List getFases(int id) {
        return Query.listaFase("SELECT * FROM " + Var.dbNameBoes + ".fase where idOrigen=" + id);
    }
}
