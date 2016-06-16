package datamer.ctrl.testra;

import datamer.Var;
import datamer.model.testra.Estado;
import datamer.model.testra.ModeloCaptura;
import datamer.model.testra.TipoCruce;
import datamer.model.testra.enty.Cruce;
import datamer.model.testra.enty.Multa;
import datamer.model.testra.enty.Captura;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class Query extends sql.Query {

    public static void ejecutar(String query) {
        try {
            bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(datamer.ctrl.tkm.Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Captura getCaptura() {
        return new Captura();
    }

    public static boolean insertMultas(List<Multa> list) {
        Multa aux;
        Iterator<Multa> it = list.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = it.next();
                bd.ejecutar(aux.SQLCrear());
            }

            bd.close();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static void setEstadoDescarga(int id, Estado estado) {
        String query = "UPDATE " + Var.dbNameTestra + ".captura SET estado_cruce=" + estado.getValue() + " WHERE id=" + id;

        try {
            bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean guardarBoletin(String idEdicto, String datos) {
        try {
            bd = new Sql(Var.con);
            bd.ejecutar("UPDATE " + Var.dbNameTestra + ".captura SET "
                    + "datos=" + Util.comillas(datos) + " "
                    + "where id_edicto=" + Util.comillas(idEdicto) + ")");
            bd.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static List<Captura> listaBoe(String query) {
        List<Captura> list = new ArrayList();
        Captura aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Captura();
                aux.setId(rs.getInt("id"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setFecha(rs.getDate("fecha"));
                aux.setCsv(rs.getString("csv"));
                aux.setDatos(rs.getString("datos"));
                aux.setEstado(rs.getInt("estado_cruce"));
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<Multa> listaMulta(String query) {
        List<Multa> list = new ArrayList();
        Multa aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Multa();
                aux.setId(rs.getInt("id"));
                aux.setFechaPublicacion(Dates.imprimeFecha(rs.getDate("fecha_publicacion")));
                aux.setCodigoBoletin(rs.getString("id_edicto"));
                aux.setExpediente(rs.getString("expediente"));
                aux.setFechaMulta(rs.getDate("fecha_multa"));
                aux.setNif(rs.getString("nif"));
                aux.setMatricula(rs.getString("matricula"));
                aux.setLineaQuery(rs.getString("linea"));

                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<String> listaCapturaParam(Date fecha) {
        String query = "SELECT parametros FROM " + Var.dbNameTestra + ".captura WHERE fecha=" + Util.comillas(Dates.imprimeFecha(fecha));
        List<String> list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("parametros");
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<Captura> listaCaptura(Date fecha) {
        String query = "SELECT * FROM " + Var.dbNameTestra + ".captura WHERE fecha=" + Util.comillas(Dates.imprimeFecha(fecha));
        List<Captura> list = new ArrayList();
        Captura aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Captura();
                aux.setId(rs.getInt("id"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setParametros(rs.getString("parametros"));
                aux.setCsv(rs.getString("csv"));
                aux.setFecha(fecha);
                aux.setEstado(rs.getInt("estado"));
                aux.setDatos(rs.getString("datos"));
                aux.setEstadoCruce(rs.getInt("estado_cruce"));

                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<ModeloCaptura> listaModeloCaptura(Date fecha) {
        String query = "SELECT id,codigo,parametros,csv,estado FROM " + Var.dbNameTestra + ".captura WHERE fecha=" + Util.comillas(Dates.imprimeFecha(fecha));
        List<ModeloCaptura> list = new ArrayList();
        ModeloCaptura aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new ModeloCaptura();
                aux.setId(rs.getInt("id"));
                aux.setEdicto(rs.getString("codigo"));
                aux.setParametros(rs.getString("parametros"));
                aux.setCsv(rs.getString("csv"));
                aux.setEstado(rs.getInt("estado"));

                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<Cruce> listaCruce(TipoCruce tipo, Date fecha) {
        List<Cruce> list = new ArrayList();
        Cruce aux;
        String query = "";

        switch (tipo) {
            case TESTRA:
                query = "SELECT * FROM " + Var.dbNameTestra + ".cruce WHERE fechaPublicacion=" + Util.comillas(Dates.imprimeFecha(fecha));
                break;
            case IDBL:
                query = "SELECT * FROM " + Var.dbNameIdbl + ".cruce WHERE fecha_publicacion=" + Util.comillas(Dates.imprimeFecha(fecha));
                break;
        }

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Cruce();
                aux.setId(rs.getInt("id"));
                if (tipo == TipoCruce.TESTRA) {
                    aux.setCodigoBoletin("TESTRA");
                    aux.setFechaMulta(rs.getString("fechaMulta"));
                    aux.setNif(rs.getString("nif"));
                } else {
                    aux.setCodigoBoletin("IDBL");
                    aux.setFechaMulta(rs.getString("fecha_multa"));
                    aux.setNif(rs.getString("cif"));
                }
                aux.setFechaPublicacion(fecha);
                aux.setExpediente(rs.getString("expediente"));
                aux.setMatricula(rs.getString("matricula"));
                aux.setLinea(rs.getString("linea"));
                aux.setTipo(tipo);
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
