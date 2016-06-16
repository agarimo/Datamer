package datamer.model.boes.enty;

import util.Varios;

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
        return "UPDATE boes.multa SET fase=" + Varios.comillas(this.nuevaFase) + " WHERE "
                + "idOrganismo=" + this.idOrigen + " "
                + "AND fase=" + Varios.comillas(this.fase) + " "
                + "AND reqObs=" + Varios.comillas(this.reqObs);
    }
}
