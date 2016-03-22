package datamer.ctrl.tkm;

import datamer.Var;
import datamer.model.tkm.enty.Cliente;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Sql;

/**
 *
 * @author Agarimo
 */
public class Query extends sql.Query{
    
    public static void ejecutar(String query){
        try {
            bd = new Sql(Var.con);
            bd.ejecutar(query);
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Cliente getCliente(String query) {
        Cliente aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            if (rs.next()) {
                aux = new Cliente();
                aux.setId(rs.getInt("id"));
                aux.setEstado(rs.getInt("estado"));
                aux.setCif(rs.getString("cif"));
                aux.setNombre(rs.getString("nombre"));
                aux.setTelefono(rs.getString("telefono"));
                aux.setContacto(rs.getString("contacto"));
                aux.setMail(rs.getString("mail"));
            }

            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static List<String> listaComentarios(int id) {
        String query = "SELECT * FROM " + Var.dbNameTkm + ".comentario where id_cliente="+id+" order by id desc";
        List<String> list = new ArrayList();
        String aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux= "["+rs.getString("fecha")+"] - ";
                aux = aux + rs.getString("comentario");
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
}
