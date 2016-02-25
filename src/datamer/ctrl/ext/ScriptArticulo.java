package datamer.ctrl.ext;

import enty.OrigenArticulo;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.SqlBoe;
import main.Var;
import util.Sql;

/**
 *
 * @author Agarimo
 */
public class ScriptArticulo {

    List<OrigenArticulo> list;

    public ScriptArticulo() {
        list = SqlBoe.listaOrigenArticulo();
    }

    public void run() {
        OrigenArticulo aux;
        Iterator<OrigenArticulo> it = list.iterator();
        Sql bd;

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = it.next();
                bd.ejecutar(aux.SQLScript());
            }
            
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(ScriptArticulo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
