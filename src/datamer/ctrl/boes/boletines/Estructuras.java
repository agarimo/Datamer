package datamer.ctrl.boes.boletines;

import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.Boletin;
import datamer.model.boes.enty.Estructura;
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
public class Estructuras {

    Sql bd;
    Date fecha;
    List boletines;
    List estructuras;

    public Estructuras() {
        this.fecha = null;
        this.boletines = null;
        this.estructuras = getEstructuras();
    }

    public Estructuras(Date fecha, boolean pendientes) {
        this.fecha = fecha;
        this.boletines = getBol(pendientes);
        this.estructuras = getEstructuras();
    }

    private List getBol(boolean pendientes) {
        if (pendientes) {
            return Query.listaBoletin("SELECT * FROM " + Var.dbNameBoes + ".boletin where isEstructura<1 and idBoe in "
                    + "(SELECT id FROM " + Var.dbNameBoes + ".boe where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(this.fecha)) + ")");
        } else {
            return Query.listaBoletin("SELECT * FROM " + Var.dbNameBoes + ".boletin where idBoe in "
                    + "(SELECT id FROM " + Var.dbNameBoes + ".boe where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(this.fecha)) + ")");
        }
    }

    private boolean conectar() {
        try {
            bd = new Sql(Var.con);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Estructuras.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean desconectar() {
        try {
            bd.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Estructuras.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private List getEstructuras() {
        return Query.listaEstructuras("SELECT * FROM boes.estructura order by estructura");
    }

    public List getBoletines() {
        return this.boletines;
    }

    public void run(Boletin aux) {
        Estructura struc;
        conectar();
        
        struc = compruebaEstructura(aux.getCodigo());

        if (struc != null) {
            aux.setIsEstructura(struc.getId());
        } else {
            aux.setIsEstructura(-1);
        }

        try {
            bd.ejecutar(aux.SQLEditar());
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }

        desconectar();
    }

    private Estructura compruebaEstructura(String codigo) {
        String datos = getDatos(codigo);
        Estructura estructura = null;
        Estructura aux;
        Iterator it = estructuras.iterator();

        while (it.hasNext()) {
            aux = (Estructura) it.next();

            if (datos.contains(aux.getEstructura())) {
                estructura = aux;
            }
        }
        return estructura;
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
}
