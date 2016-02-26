package datamer.ctrl.ext.script;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.OrigenArticulo;
import util.Sql;

/**
 *
 * @author Agarimo
 */
public class ScriptArticulo {

    private List<OrigenArticulo> list;

    public ScriptArticulo() {
        list = Query.listaOrigenArticulo();
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
