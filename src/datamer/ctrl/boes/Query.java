package datamer.ctrl.boes;

import datamer.Var;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Sql;
import datamer.model.boes.enty.Descarga;

/**
 *
 * @author Agarimo
 */
public class Query extends util.Query{
    
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

}
