package datamer.ctrl.boes.boe;

import datamer.model.boes.ModeloBoes;
import datamer.Var;
import datamer.model.boes.enty.Boletin;
import datamer.model.boes.enty.Descarga;
import datamer.model.boes.enty.Entidad;
import datamer.model.boes.enty.Origen;
import datamer.model.boes.enty.Stats;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Insercion {

    private Sql bd;

    public Insercion() {

    }

    //<editor-fold defaultstate="collapsed" desc="Inserción">
    public void insertaBoletin(ModeloBoes aux) {
        Boletin bol = new Boletin();

        try {
            bd = new Sql(Var.con);

            bol.setIdOrigen(getIdOrigen(aux.getEntidad(), aux.getOrigen()));
            bol.setIdBoe(getIdBoe(aux.getFecha()));
            bol.setIdDescarga(getIdDescarga(aux.getCodigo(), aux.getLink()));
            bol.setCodigo(aux.getCodigo());
            bol.setTipo("*711*");
            bol.setFase("BCN1");
            bol.setIsFase(0);
            bol.setIsEstructura(0);
            bol.setIdioma(getIdioma(bol.getIdOrigen()));

            bd.ejecutar(bol.SQLCrear());
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getIdOrigen(String entidad, String origen) throws SQLException {
        int aux;
        Origen or = new Origen();
        or.setIdEntidad(getIdEntidad(entidad));
        or.setNombre(origen.replace("'", "\\'"));

        aux = bd.buscar(or.SQLBuscar());

        if (aux == -1) {
            bd.ejecutar(or.SQLCrear());
            aux = bd.ultimoRegistro();
        }

        return aux;
    }

    private int getIdEntidad(String nombre) throws SQLException {
        int aux;
        Entidad en = new Entidad();
        en.setNombre(nombre.replace("'", "\\'"));

        aux = bd.buscar(en.SQLBuscar());

        if (aux == -1) {
            bd.ejecutar(en.SQLCrear());
            aux = bd.ultimoRegistro();
        }

        return aux;
    }

    private int getIdBoe(String fecha) throws SQLException {
        return bd.getInt("SELECT * FROM " + Var.dbNameBoes + ".boe where fecha=" + Varios.entrecomillar(fecha));
    }

    private int getIdioma(int idOrigen) throws SQLException {
        return bd.getInt("SELECT idioma FROM " + Var.dbNameBoes + ".origen where id=" + idOrigen);
    }

    private int getIdDescarga(String codigo, String link) throws SQLException {
        int aux;
        Descarga ds = new Descarga();
        ds.setCodigo(codigo);
        ds.setLink(link);
        ds.setDatos("null");

        aux = bd.buscar(ds.SQLBuscar());

        if (aux == -1) {
            bd.ejecutar(ds.SQLCrear());
            aux = bd.ultimoRegistro();
        }
        return aux;
    }
//</editor-fold>

    public void guardaStatsD(List lista) {
        Stats bp;
        ModeloBoes aux;
        Iterator it = lista.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = (ModeloBoes) it.next();
                bd.ejecutar(aux.SQLUpdate());
            }

            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardaStatsS(List lista) {
        Stats bp;
        ModeloBoes aux;
        Iterator it = lista.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = (ModeloBoes) it.next();
                bd.ejecutar(aux.SQLUpdate());
            }

            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Insercion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
