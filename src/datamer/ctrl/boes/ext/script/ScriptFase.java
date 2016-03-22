package datamer.ctrl.boes.ext.script;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.OrigenFase;
import sql.Sql;

/**
 *
 * @author Agarimo
 */
public final class ScriptFase {

    private List<OrigenFase> list;
    

    public ScriptFase() {
        list = Query.listaOrigenFase();
    }

    public void run() {
        Sql bd;
        OrigenFase aux;
        Iterator<OrigenFase> it = list.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = it.next();
                bd.ejecutar(aux.SQLScript());
            }
            
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(ScriptFase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
