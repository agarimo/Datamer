package datamer.ctrl.ext;

import enty.Multa;
import enty.OrigenExpediente;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import main.SqlBoe;
import main.Var;
import org.apache.commons.collections4.map.MultiValueMap;
import util.Dates;
import util.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public final class ScriptExp {

    private final Date fecha;
    private final MultiValueMap map;
    private final String codigoBoletin;

    public ScriptExp(Date fecha) {
        this.fecha = fecha;
        this.codigoBoletin = null;
        map = cargaMap();
    }

    public void run() {
        int idOrigen;
        List<Multa> listado;
        Iterator it = getKeySet().iterator();

        while (it.hasNext()) {
            idOrigen = (int) it.next();
            if (fecha != null) {
                listado = cargaMultas(fecha, idOrigen);
            } else {
                listado = cargaMultas(codigoBoletin);
            }

            if (!listado.isEmpty()) {
                runMultas(listado, idOrigen);

            }
        }
    }

    private void runMultas(List<Multa> multas, int idOrigen) {
        Sql bd;
        Multa multa;
        OrigenExpediente aux;
        Collection<Multa> result;
        List script = getItems(idOrigen);
        Iterator it = script.iterator();

        try {
            bd = new Sql(Var.con);

            while (it.hasNext()) {
                aux = (OrigenExpediente) it.next();
                final String str = aux.getCabecera();

                result = multas.stream().filter(p -> p.getExpediente().startsWith(str)).collect(Collectors.toList());
                Iterator<Multa> ite = result.iterator();

                while (ite.hasNext()) {
                    multa = ite.next();
                    bd.ejecutar(multa.SQLEditarOrganismo(aux.getOrigen()));
                }
            }
            
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(ScriptExp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private MultiValueMap cargaMap() {
        OrigenExpediente aux;
        MultiValueMap mp = new MultiValueMap();
        List<OrigenExpediente> list = SqlBoe.listaOrigenExp();
        Iterator<OrigenExpediente> it = list.iterator();

        while (it.hasNext()) {
            aux = it.next();
            mp.put(aux.getIdOrigen(), aux);
        }

        return mp;
    }

    private List getKeySet() {
        return new ArrayList(map.keySet());
    }

    private List getItems(int aux) {
        List list = new ArrayList();
        OrigenExpediente bol;
        Iterator it = map.iterator(aux);

        while (it.hasNext()) {
            bol = (OrigenExpediente) it.next();
            list.add(bol);
        }

        return list;
    }

    private List cargaMultas(Date fecha, int idOrigen) {
        List aux = SqlBoe.listaMultas("SELECT * FROM boes.multa where "
                + "fechaPublicacion=" + Varios.entrecomillar(Dates.imprimeFecha(fecha)) + " "
                + "and idOrganismo=" + idOrigen);
        return aux;
    }

    private List cargaMultas(String codigoBoletin) {
        List aux = SqlBoe.listaMultas("SELECT * FROM boes.multa where "
                + "idBoletin IN"
                + "(SELECT id FROM boes.procesar WHERE codigo=" + Varios.entrecomillar(codigoBoletin) + ") "
                + "AND "
                + "idOrganismo IN "
                + "(select idOrigen from boes.origen_expediente group by idOrigen)");
        return aux;
    }

}
