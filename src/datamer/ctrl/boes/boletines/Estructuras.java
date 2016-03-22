package datamer.ctrl.boes.boletines;

import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.Boletin;
import datamer.model.boes.enty.Estructura;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private List getEstructuras() {
        return Query.listaEstructuras("SELECT * FROM boes.estructura order by estructura");
    }

    public List getBoletines() {
        return this.boletines;
    }

    public void run(Boletin aux) {
        Sql bd;
        Estructura estructura;

        estructura = compruebaEstructura(aux.getIdDescarga());

        if (estructura != null) {
            aux.setIsEstructura(estructura.getId());
        } else {
            aux.setIsEstructura(-1);
        }

        try {
            bd = new Sql(Var.con);
            bd.ejecutar(aux.SQLEditar());
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Estructura compruebaEstructura(int id) {
        Estructura estructura = null;
        Estructura aux;
        Iterator it = estructuras.iterator();

        while (it.hasNext()) {
            aux = (Estructura) it.next();

            if (getDatos(id).contains(aux.getEstructura())) {
                estructura = aux;
            }
        }
        return estructura;
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

    public void limpiarEstructuras() {
        Sql bd;
        Estructura aux;
        List sel = new ArrayList();
        List des = new ArrayList();
        Iterator it = estructuras.iterator();

        while (it.hasNext()) {
            aux = (Estructura) it.next();

            if (sel.contains(aux)) {
                des.add(aux);
            } else {
                sel.add(aux);
            }
        }

        try {
            bd = new Sql(Var.con);
            it = des.iterator();

            while (it.hasNext()) {
                aux = (Estructura) it.next();
                bd.ejecutar(aux.SQLBorrar());
            }
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Estructuras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
