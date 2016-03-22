package datamer.ctrl.testra;

import datamer.Var;
import datamer.model.testra.Estado;
import datamer.model.testra.ModeloCaptura;
import datamer.model.testra.TipoCruce;
import datamer.model.testra.enty.Cruce;
import datamer.model.testra.enty.Descarga;
import datamer.model.testra.enty.CruceTestra;
import datamer.model.testra.enty.Captura;
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
public class Query extends sql.Query {
    
    public static void ejecutar(String query){
        try {
            bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(datamer.ctrl.tkm.Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Captura getCaptura (){
        return new Captura();
    }
    
    public static boolean insertMultas(List<CruceTestra> list) {
        CruceTestra aux;
        Iterator<CruceTestra> it = list.iterator();

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
        String query = "UPDATE "+Var.dbNameTestra+".descarga SET estadoCruce=" + estado.getValue() + " WHERE idDescarga=" + id;

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
            bd.ejecutar("UPDATE "+Var.dbNameTestra+".descarga SET "
                    + "datos=" + Varios.entrecomillar(datos) + " "
                    + "where idDescarga="
                    + "(select idDescarga from datagest.edicto where "
                    + "idEdicto=" + Varios.entrecomillar(idEdicto) + ")");
            bd.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static List<Descarga> listaBoe(String query) {
        List<Descarga> list = new ArrayList();
        Descarga aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Descarga();
                aux.setId(rs.getInt("idDescarga"));
                aux.setCodigo(rs.getString("idEdicto"));
                aux.setFecha(rs.getDate("fecha"));
                aux.setCsv(rs.getString("csv"));
                aux.setDatos(rs.getString("datos"));
                aux.setEstado(rs.getInt("estadoCruce"));
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<CruceTestra> listaMulta(String query) {
        List<CruceTestra> list = new ArrayList();
        CruceTestra aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new CruceTestra();
                aux.setId(rs.getInt("id"));
                aux.setFechaPublicacion(Dates.imprimeFecha(rs.getDate("fechaPublicacion")));
                aux.setCodigoBoletin(rs.getString("codigoEdicto"));
                aux.setExpediente(rs.getString("expediente"));
                aux.setFechaMulta(rs.getDate("fechaMulta"));
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
    
    public static List<Captura> listaCaptura(Date fecha) {
        String query = "SELECT * FROM "+Var.dbNameTestra+".captura WHERE fecha="+Varios.entrecomillar(Dates.imprimeFecha(fecha));
        List<Captura> list = new ArrayList();
        Captura aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Captura();
                aux.setId(rs.getInt("id"));
                aux.setIdEdicto(rs.getString("id_edicto"));
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
        String query = "SELECT id,id_edicto,parametros,csv,estado FROM "+Var.dbNameTestra+".captura WHERE fecha="+Varios.entrecomillar(Dates.imprimeFecha(fecha));
        List<ModeloCaptura> list = new ArrayList();
        ModeloCaptura aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new ModeloCaptura();
                aux.setId(rs.getInt("id"));
                aux.setEdicto(rs.getString("id_edicto"));
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
        String query="";
        
        switch(tipo){
            case TESTRA:
                query="SELECT * FROM "+Var.dbNameTestra+".cruce WHERE fechaPublicacion="+Varios.entrecomillar(Dates.imprimeFecha(fecha));
                break;
            case IDBL:
                query="SELECT * FROM "+Var.dbNameIdbl+".cruce WHERE fecha_publicacion="+Varios.entrecomillar(Dates.imprimeFecha(fecha));
                break;
        }

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Cruce();
                aux.setId(rs.getInt("id"));
                if(tipo==TipoCruce.TESTRA){
                    aux.setCodigoBoletin("TESTRA");
                    aux.setFechaMulta(rs.getString("fechaMulta"));
                     aux.setNif(rs.getString("nif"));
                }else{
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
