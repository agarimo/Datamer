package datamer.ctrl.boes.boletines;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.ModeloUnion;
import datamer.model.boes.ModeloBoletines;
import datamer.model.boes.enty.Procesar;
import org.apache.commons.collections4.map.MultiValueMap;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Union {

    Date fecha;
    List estructuras;
    MultiValueMap map;

    public Union(Date fecha) {
        this.fecha = fecha;
        this.estructuras = Query.listaEstructurasDia(fecha);
    }

    public MultiValueMap cargaMap(String estructura) {
        MultiValueMap mp = new MultiValueMap();
        ModeloUnion aux;
        Iterator it;

        if (estructura == null) {
            it = Query.listaUnion("SELECT * FROM " + Var.dbNameBoes + ".vista_union "
                    + "where fecha=" + Varios.comillas(Dates.imprimeFecha(fecha)) + " "
                    + "and isEstructura is null").iterator();
        } else {
            it = Query.listaUnion("SELECT * FROM " + Var.dbNameBoes + ".vista_union "
                    + "where fecha=" + Varios.comillas(Dates.imprimeFecha(fecha)) + " "
                    + "and isEstructura=" + Varios.comillas(estructura)).iterator();
        }

        while (it.hasNext()) {
            aux = (ModeloUnion) it.next();
            mp.put(aux.getCodigoUn(), aux);
        }

        return mp;
    }

    public void setMap(MultiValueMap aux) {
        this.map = aux;
    }

    public List getEstructuras() {
        return this.estructuras;
    }

    public List getKeySet() {
        return new ArrayList(map.keySet());
    }

    public List getBoletines(String aux) {
        List list = new ArrayList();
        ModeloBoletines bol;
        ModeloUnion un;
        Iterator it = map.iterator(aux);

        while (it.hasNext()) {
            un = (ModeloUnion) it.next();
            bol = Query.getModeloBoletines(un.getCodigo());
            list.add(bol);
        }

        return list;
    }

    public List getProcesar(String aux) {
        List list = new ArrayList();
        Procesar bol;
        ModeloUnion un;
        Iterator it = map.iterator(aux);

        while (it.hasNext()) {
            un = (ModeloUnion) it.next();
            bol = Query.getProcesar(un.getCodigo());
            list.add(bol);
        }

        return list;
    }
}
