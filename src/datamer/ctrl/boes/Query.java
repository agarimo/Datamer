package datamer.ctrl.boes;

import datamer.Var;
import datamer.ctrl.boes.boe.Boe;
import datamer.model.boes.ModeloBoes;
import datamer.model.boes.enty.ReqObs;
import datamer.model.boes.ModeloBoletines;
import datamer.model.boes.ModeloFases;
import datamer.model.boes.ModeloUnion;
import datamer.model.boes.enty.Boletin;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Sql;
import datamer.model.boes.enty.Entidad;
import datamer.model.boes.enty.Estructura;
import datamer.model.boes.enty.Fase;
import datamer.model.boes.enty.Multa;
import datamer.model.boes.enty.Origen;
import datamer.model.boes.enty.OrigenArticulo;
import datamer.model.boes.enty.OrigenExpediente;
import datamer.model.boes.enty.OrigenFase;
import datamer.model.boes.enty.Pattern;
import datamer.model.boes.enty.Procesar;
import datamer.model.boes.enty.Publicacion;
import datamer.model.boes.enty.StrucData;
import datamer.model.boes.enty.Tipo;
import datamer.model.boes.enty.VistaExtraccion;
import java.util.Date;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Query extends sql.Query {

    public static void eliminaBoletin(String codigo) {
        try {
            bd = new Sql(Var.con);
            bd.ejecutar("DELETE FROM " + Var.dbNameBoes + ".boletin where codigo=" + Varios.entrecomillar(codigo));
            bd.ejecutar("UPDATE " + Var.dbNameServer + ".publicacion set status='DELETED', selected=false where codigo=" + Varios.entrecomillar(codigo));
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eliminarMultasBoletin(String codigo) {
        String query = "DELETE FROM " + Var.dbNameBoes + ".multa WHERE idBoletin="
                + "(SELECT id FROM " + Var.dbNameBoes + ".procesar WHERE codigo=" + Varios.entrecomillar(codigo) + ");";

        try {
            bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
                aux = new Boletin(rs.getInt("id"), rs.getInt("idOrigen"), rs.getInt("idBoe"),
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

    public static String getDesData(String codigo) {
        String query = "SELECT datos FROM " + Var.dbNameBoes + ".descarga where codigo=" + Varios.entrecomillar(codigo);
        String aux = "";

        try {
            bd = new Sql(Var.con);
            aux = bd.getString(query);
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static String getEstructura(int id) {
        String aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs("SELECT estructura FROM " + Var.dbNameBoes + ".estructura where id=" + id);

            while (rs.next()) {
                aux = rs.getString("estructura");
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }
    
    public static Publicacion getPublicacion(String codigo) {
        Publicacion aux = null;
        String query = "SELECT * FROM " + Var.dbNameServer + ".publicacion where codigo=" + Varios.entrecomillar(codigo);

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Publicacion();
                aux.setId(rs.getInt("id"));
                aux.setFecha(Dates.asLocalDate(rs.getDate("fecha")));
                aux.setCodigo(rs.getString("codigo"));
                aux.setEntidad(rs.getString("entidad"));
                aux.setOrigen(rs.getString("origen"));
                aux.setDescripcion(rs.getString("descripcion"));
                aux.setDatos(rs.getString("datos"));
                aux.setLink(rs.getString("link"));
                aux.setCve(rs.getString("cve"));
                aux.setSelected(rs.getBoolean("selected"));
                aux.setStatus(rs.getString("status"));
            }

            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }
    
    public static String getPublicacionData(String codigo){
        String query = "SELECT datos FROM " + Var.dbNameServer + ".publicacion where codigo=" + Varios.entrecomillar(codigo);
        String aux = "";

        try {
            bd = new Sql(Var.con);
            aux = bd.getString(query);
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static ModeloBoletines getModeloBoletines(String codigo) {
        ModeloBoletines aux = null;
        String query = "SELECT * FROM " + Var.dbNameBoes + ".vista_boletines where codigo=" + Varios.entrecomillar(codigo);

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new ModeloBoletines();
                aux.codigo.set(rs.getString("codigo"));
                aux.idBoletin.set(rs.getInt("idBoletin"));
                aux.entidad.set(rs.getString("entidad"));
                aux.origen.set(rs.getString("origen"));
                aux.fecha.set(Dates.imprimeFecha(rs.getDate("fecha")));
                aux.tipo.set(rs.getString("tipo"));
                aux.fase.set(rs.getString("fase"));
                aux.isFase.set(rs.getInt("isFase"));
                aux.isEstructura.set(rs.getString("isEstructura"));
                aux.link.set(rs.getString("link"));
                aux.idioma.set(rs.getInt("idioma"));
            }

            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static Procesar getProcesar(String codigo) {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".procesar where codigo=" + Varios.entrecomillar(codigo);
        Procesar aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Procesar();
                aux.setId(rs.getInt("id"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setEstructura(rs.getInt("estructura"));
                aux.setFecha(rs.getDate("fecha"));
                aux.setLink(rs.getString("link"));
                aux.setEstado(rs.getInt("estado"));
            }

            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static StrucData getStrucData(String query) {
        StrucData aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new StrucData();

                aux.setId(rs.getInt("id"));
                aux.setIdEstructura(rs.getInt("idEstructura"));
                aux.setExpediente(rs.getInt("expediente"));
                aux.setSancionado(rs.getInt("sancionado"));
                aux.setNif(rs.getInt("nif"));
                aux.setLocalidad(rs.getInt("localidad"));
                aux.setFechaMulta(rs.getInt("fechaMulta"));
                aux.setMatricula(rs.getInt("matricula"));
                aux.setCuantia(rs.getInt("cuantia"));
                aux.setPreceptoA(rs.getInt("preceptoA"));
                aux.setPreceptoB(rs.getInt("preceptoB"));
                aux.setPreceptoC(rs.getInt("preceptoC"));
                aux.setArticuloA(rs.getInt("articuloA"));
                aux.setArticuloB(rs.getInt("articuloB"));
                aux.setArticuloC(rs.getInt("articuloC"));
                aux.setArticuloD(rs.getInt("articuloD"));
                aux.setPuntos(rs.getInt("puntos"));
                aux.setReqObs(rs.getInt("reqObs"));
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static VistaExtraccion getVistaExtraccion(String query) {
        VistaExtraccion aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new VistaExtraccion(rs.getString("codigo"), rs.getString("entidad"), rs.getInt("idOrigen"),
                        rs.getString("origen"), rs.getDate("fecha"), rs.getString("tipo"), rs.getString("fase"));
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static List<Boletin> listaBoletin(String query) {

        List<Boletin> list = new ArrayList();
        Boletin aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Boletin(rs.getInt("id"), rs.getInt("idOrigen"), rs.getInt("idBoe"),
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

    public static List<Integer> listaEstructurasCreadas() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".strucdata";
        List<Integer> list = new ArrayList();
        int aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getInt("idEstructura");
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

    public static List listaEstructurasDia(Date fecha) {
        List list = new ArrayList();
        String query = "select isEstructura from " + Var.dbNameBoes + ".vista_union "
                + "where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(fecha)) + " "
                + "group by isEstructura;";
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("isEstructura");
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

    public static List listaEstructurasFechas() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".strucfecha";
        List list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("estructura");
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

    public static List listaEstructurasHeader() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".strucheader";
        List list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("estructura");
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

    public static List<Integer> listaEstructurasManual() {
        String query = "SELECT id FROM " + Var.dbNameBoes + ".estructura where procesarManual=1";
        List<Integer> list = new ArrayList();
        int aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getInt("id");
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

    public static List<ModeloBoes> listaModeloBoes(String query) {
        List<ModeloBoes> list = new ArrayList();
        ModeloBoes aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new ModeloBoes();
                aux.fecha.set(rs.getString("fecha"));
                aux.codigo.set(rs.getString("codigo"));
                aux.entidad.set(rs.getString("entidad"));
                aux.origen.set(rs.getString("origen"));
                aux.descripcion.set(rs.getString("descripcion"));
                aux.link.set(rs.getString("link"));
                aux.setStatus(rs.getString("status"));
                aux.setSelected(rs.getBoolean("selected"));
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
    
    public static List<Publicacion> listaPublicacion(String query) {
        List<Publicacion> list = new ArrayList();
        Publicacion aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Publicacion();
                aux.setId(rs.getInt("id"));
                aux.setFecha(Dates.asLocalDate(rs.getDate("fecha")));
                aux.setCodigo(rs.getString("codigo"));
                aux.setEntidad(rs.getString("entidad"));
                aux.setOrigen(rs.getString("origen"));
                aux.setDescripcion(rs.getString("descripcion"));
                aux.setDatos(rs.getString("datos"));
                aux.setLink(rs.getString("link"));
                aux.setCve(rs.getString("cve"));
                aux.setSelected(rs.getBoolean("selected"));
                aux.setStatus(rs.getString("status"));
                
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

    public static List<ModeloBoletines> listaModeloBoletines(String query) {
        List<ModeloBoletines> list = new ArrayList();
        ModeloBoletines aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new ModeloBoletines();
                aux.codigo.set(rs.getString("codigo"));
                aux.idBoletin.set(rs.getInt("idBoletin"));
                aux.entidad.set(rs.getString("entidad"));
                aux.origen.set(rs.getString("origen"));
                aux.fecha.set(Dates.imprimeFecha(rs.getDate("fecha")));
                aux.tipo.set(rs.getString("tipo"));
                aux.fase.set(rs.getString("fase"));
                aux.isFase.set(rs.getInt("isFase"));
                aux.isEstructura.set(rs.getString("isEstructura"));
                aux.link.set(rs.getString("link"));
                aux.idioma.set(rs.getInt("idioma"));
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

    public static List listaMultas(String query) {
        List list = new ArrayList();
        Multa aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Multa();
                aux.setId(rs.getInt("id"));
                aux.setIdBoletin(rs.getInt("idBoletin"));
                aux.setCodigoSancion(rs.getString("codigoSancion"));
                aux.setFechaPublicacion(rs.getDate("fechaPublicacion"));
                aux.setIdOrganismo(rs.getInt("idOrganismo"));
                aux.setOrganismo(rs.getString("organismo"));
                aux.setBoe(rs.getString("boe"));
                aux.setFase(rs.getString("fase"));
                aux.setTipoJuridico(rs.getString("tipoJuridico"));
                aux.setPlazo(rs.getString("plazo"));
                aux.setExpediente(rs.getString("expediente"));
                aux.setFechaMulta(rs.getDate("fechaMulta"));
                aux.setArticulo(rs.getString("articulo"));
                aux.setNif(rs.getString("nif"));
                aux.setSancionado(rs.getString("sancionado"));
                aux.setLocalidad(rs.getString("localidad"));
                aux.setMatricula(rs.getString("matricula"));
                aux.setCuantia(rs.getString("cuantia"));
                aux.setPuntos(rs.getString("puntos"));
                aux.setReqObs(rs.getString("reqObs"));
                aux.setLinea(rs.getString("linea"));
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

    public static List<OrigenArticulo> listaOrigenArticulo() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".origen_articulo";
        List list = new ArrayList();
        OrigenArticulo aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new OrigenArticulo();
                aux.setIdOrigen(rs.getInt("idOrigen"));
                aux.setArticulo(rs.getString("articulo"));
                aux.setFase(rs.getString("fase"));
                aux.setNuevaFase(rs.getString("nuevaFase"));
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

    public static List<OrigenExpediente> listaOrigenExp() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".origen_expediente";
        List list = new ArrayList();
        OrigenExpediente aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new OrigenExpediente();
                aux.setIdOrigen(rs.getInt("idOrigen"));
                aux.setCabecera(rs.getString("cabecera"));
                aux.setOrigen(rs.getString("origen"));
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

    public static List<OrigenFase> listaOrigenFase() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".origen_fase";
        List list = new ArrayList();
        OrigenFase aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new OrigenFase();
                aux.setIdOrigen(rs.getInt("idOrigen"));
                aux.setCabecera(rs.getString("cabecera"));
                aux.setFase(rs.getString("fase"));
                aux.setNuevaFase(rs.getString("nuevaFase"));
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

    public static List listaProcesar(String query) {
        List list = new ArrayList();
        Procesar aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Procesar();
                aux.setId(rs.getInt("id"));
                aux.setFecha(rs.getDate("fecha"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setLink(rs.getString("link"));
                aux.setEstructura(rs.getInt("estructura"));
                aux.setEstado(rs.getInt("estado"));
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

    public static List<ReqObs> listaReqObs(String query) {
        List<ReqObs> list = new ArrayList();
        ReqObs aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new ReqObs(rs.getInt("id"), rs.getInt("idOrigen"), rs.getString("fase"),
                        rs.getString("reqObs"), rs.getString("nuevaFase"));
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

    public static List<ModeloUnion> listaUnion(String query) {
        List<ModeloUnion> list = new ArrayList();
        ModeloUnion aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new ModeloUnion();
                aux.codigo.set(rs.getString("codigo"));
                aux.fecha.set(rs.getString("fecha"));
                aux.codigoUn.set(rs.getString("codigoUn"));
                aux.estructura.set(rs.getString("isEstructura"));
                aux.codigoProv.set(rs.getString("codigoProv"));
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

    public static List<Origen> listaOrigenFases(int id) {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".origen where idEntidad=" + id + " order by nombre";
        List list = new ArrayList();
        Origen aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Origen();
                aux.setId(rs.getInt("id"));
                aux.setNombre(rs.getString("nombre"));
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

    public static List<Entidad> listaEntidadFases() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".entidad order by nombre";
        List list = new ArrayList();
        Entidad aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Entidad();
                aux.setId(rs.getInt("id"));
                aux.setNombre(rs.getString("nombre"));
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

    public static List<Tipo> listaTipoFases() {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".tipo where tipo=0";
        List list = new ArrayList();
        Tipo aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Tipo();
                aux.setId(rs.getString("id"));
                aux.setNombre(rs.getString("nombre"));
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

    public static List<ModeloFases> listaModeloFases(int id) {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".fase where idOrigen=" + id;
        List list = new ArrayList();
        ModeloFases aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new ModeloFases();
                aux.id.set(rs.getInt("id"));
                aux.idOrigen.set(rs.getInt("idOrigen"));
                aux.codigo.set(rs.getString("codigo"));
                aux.tipo.set(rs.getInt("tipo"));
                aux.texto1.set(rs.getString("texto1"));
                aux.texto2.set(rs.getString("texto2"));
                aux.texto3.set(rs.getString("texto3"));
                aux.plazo.set(rs.getString("plazo"));
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

    public static List listaProcesarPendiente(Date fecha) {
        List list = new ArrayList();
        String query = "select a.id,a.codigo,b.link,a.isEstructura from "+Var.dbNameBoes+".boletin a "
                + "left join "+Var.dbNameServer+".publicacion b on a.codigo=b.codigo "
                + "where a.idBoe=(select id from boes.boe where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(fecha)) + ") "
                + "and a.id not in (select id from "+Var.dbNameBoes+".procesar)";
        Procesar aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Procesar();
                aux.setId(rs.getInt("id"));
                aux.setFecha(fecha);
                aux.setCodigo(rs.getString("codigo"));
                aux.setLink(rs.getString("link"));
                aux.setEstructura(rs.getInt("isEstructura"));
                aux.setEstado(0);
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

    public static List<Estructura> listaEstructuras(String query) {
        List<Estructura> list = new ArrayList();
        Estructura aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Estructura(rs.getInt("id"), rs.getString("nombre"), rs.getString("estructura"));
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

    public static Origen getOrigen(int id) {
        Origen aux = null;
        String query = "SELECT * from " + Var.dbNameBoes + ".origen where id=" + id;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            if (rs.next()) {
                aux = new Origen();
                aux.setId(rs.getInt("id"));
                aux.setIdEntidad(rs.getInt("idEntidad"));
                aux.setNombre(rs.getString("nombre"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setCodigoAy(rs.getString("codigoAy"));
                aux.setCodigoUn(rs.getString("codigoUn"));
                aux.setCodigoTes(rs.getString("codigoTes"));
                aux.setNombreMostrar(rs.getString("nombreMostrar"));
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static List<Fase> listaFase(String query) {
        List list = new ArrayList();
        Fase aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Fase();
                aux.setId(rs.getInt("id"));
                aux.setIdOrigen(rs.getInt("idOrigen"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setTipo(rs.getInt("tipo"));
                aux.setTexto1(rs.getString("texto1"));
                aux.setTexto2(rs.getString("texto2"));
                aux.setTexto3(rs.getString("texto3"));
                aux.setPlazo(rs.getString("plazo"));
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

    public static List<Fase> listaFaseTestra(String query) {
        List list = new ArrayList();
        Fase aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Fase();
                aux.setId(rs.getInt("idfase"));
                aux.setIdOrigen(rs.getInt("origen"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setTipo(rs.getInt("tipo"));
                aux.setTexto1(rs.getString("texto1"));
                aux.setTexto2(rs.getString("texto2"));
                aux.setTexto3(rs.getString("texto3"));
                aux.setPlazo(rs.getString("plazo"));
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

    public static void eliminaBoletinFase(String codigo) {
        try {
            bd = new Sql(Var.con);
            bd.ejecutar("DELETE FROM " + Var.dbNameBoes + ".boletin where codigo=" + Varios.entrecomillar(codigo));
            bd.ejecutar("UPDATE " + Var.dbNameServer + ".publicacion set status='DELETED', selected=false where codigo=" + Varios.entrecomillar(codigo));
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Pattern> listaPattern(Date fecha) {
        String query = "SELECT * FROM " + Var.dbNameBoes + ".vista_pattern where fechaPublicacion=" + Varios.entrecomillar(Dates.imprimeFecha(fecha));
        List list = new ArrayList();
        Pattern aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Pattern();
                aux.setCodigo(rs.getString("codigo"));
                aux.setNif(rs.getString("nif"));
                aux.setMatricula(rs.getString("matricula"));
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

    public static String getLink(String codigo) {
        String query = "SELECT link FROM " + Var.dbNameBoes + ".descarga where codigo=" + Varios.entrecomillar(codigo);
        String aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getString("link");
            }

            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }
}
