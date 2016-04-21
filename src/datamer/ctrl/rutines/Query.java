package datamer.ctrl.rutines;

import datamer.Var;
import datamer.model.Cve;
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
public class Query extends sql.Query {
    
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

    public static Cve getCve(String query) {
        Cve aux = null;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Cve();
                aux.setId(rs.getInt("id"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setCve(rs.getString("cve"));
                aux.setLink(rs.getString("link"));
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }
    
    public static List<Cve> listaCve(String query) {

        List<Cve> list = new ArrayList();
        Cve aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Cve();
                aux.setId(rs.getInt("id"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setCve(rs.getString("cve"));
                aux.setLink(rs.getString("link"));
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
