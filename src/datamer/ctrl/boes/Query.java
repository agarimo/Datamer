package datamer.ctrl.boes;

import datamer.Var;
import datamer.ctrl.boes.boe.Boe;
import datamer.model.boes.enty.Boletin;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Sql;
import datamer.model.boes.enty.Descarga;
import java.util.Date;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Query extends util.Query {

    public static Boe getBoe(Date fecha) {
        Boe aux = null;
        String query = "SELECT * from " + Var.dbNameBoes + ".boe where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(fecha));

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            if (rs.next()) {
                aux = new Boe(rs.getInt("id"), rs.getDate("fecha"), rs.getString("link"), rs.getBoolean("isClas"));
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static Boletin getBoletin(String query) {
        Boletin aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Boletin(rs.getInt("id"), rs.getInt("idOrigen"), rs.getInt("idBoe"), rs.getInt("idDescarga"),
                        rs.getString("codigo"), rs.getString("tipo"), rs.getString("fase"), rs.getInt("isFase"),
                        rs.getInt("isEstructura"), rs.getInt("idioma"));
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    //<editor-fold defaultstate="collapsed" desc="LISTADOS">
    public static List<Boletin> listaBoletin(String query) {

        List<Boletin> list = new ArrayList();
        Boletin aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Boletin(rs.getInt("id"), rs.getInt("idOrigen"), rs.getInt("idBoe"), rs.getInt("idDescarga"),
                        rs.getString("codigo"), rs.getString("tipo"), rs.getString("fase"), rs.getInt("isFase"),
                        rs.getInt("isEstructura"), rs.getInt("idioma"));
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<Descarga> listaDescargaPendiente() {
        String query = "SELECT * FROM boes.descarga where datos='null'";
        List<Descarga> list = new ArrayList();
        Descarga aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Descarga(rs.getInt("id"), rs.getString("codigo"), rs.getString("link"), rs.getString("datos"));
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List listaAlreadySelected() {
        String query = "SELECT codigo FROM " + Var.dbNameBoes + ".boletin";
        List list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("codigo");
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List listaAlreadyDiscarted() {
        String query = "SELECT codigo FROM " + Var.dbNameBoesStats + ".boletines where isSelected=false;";
        List list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("codigo");
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List listaOrigenDescartado() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".origen_descartado";
        List list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("nombre");
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List listaTextoDescartado() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".texto_descartado";
        List list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("texto");
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List listaTextoSeleccionado() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".texto_seleccionado";
        List list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("texto");
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    //</editor-fold>

}
