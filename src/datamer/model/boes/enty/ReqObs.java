package datamer.model.boes.enty;

import tools.Util;

/**
 *
 * @author Agarimo
 */
public class ReqObs {

    int id;
    int idOrigen;
    String fase;
    String reqObs;
    String nuevaFase;

    public ReqObs() {

    }

    public ReqObs(int id, int idOrigen, String fase, String reqObs, String nuevaFase) {
        this.id = id;
        this.idOrigen = idOrigen;
        this.fase = fase;
        this.reqObs = reqObs;
        this.nuevaFase = nuevaFase;
    }

    public String SQLEjecutar() {
        return "UPDATE boes.multa SET fase=" + Util.comillas(this.nuevaFase) + " WHERE "
                + "idOrganismo=" + this.idOrigen + " "
                + "AND fase=" + Util.comillas(this.fase) + " "
                + "AND reqObs=" + Util.comillas(this.reqObs);
    }
}
