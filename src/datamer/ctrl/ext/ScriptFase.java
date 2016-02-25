package datamer.ctrl.ext;

import enty.OrigenFase;
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
public final class ScriptFase {

    List<OrigenFase> list;

    public ScriptFase() {
        list = SqlBoe.listaOrigenFase();
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
