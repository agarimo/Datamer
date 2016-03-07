package datamer.ctrl.boes.ext.script;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.ReqObs;
import util.Dates;
import util.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class ScriptReq {

    private final List<ReqObs> list;

    public ScriptReq(Date fecha) {
        this.list = Query.listaReqObs("SELECT * FROM boes.reqobs WHERE idOrigen in "
                + "(select idOrganismo from boes.multa "
                + "WHERE fechaPublicacion=" + Varios.entrecomillar(Dates.imprimeFecha(fecha)) + ")");
    }

    public void run() {
        ReqObs aux;
        Iterator<ReqObs> it = list.iterator();

        try {
            Sql bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = it.next();
                bd.ejecutar(aux.SQLEjecutar());
            }

            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(ScriptReq.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
