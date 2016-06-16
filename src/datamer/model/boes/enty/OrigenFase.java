package datamer.model.boes.enty;

import datamer.Var;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class OrigenFase {

    private int idOrigen;
    private String cabecera;
    private String fase;
    private String nuevaFase;

    public OrigenFase() {

    }

    public OrigenFase(int idOrigen, String cabecera, String fase, String nuevaFase) {
        this.idOrigen = idOrigen;
        this.cabecera = cabecera;
        this.fase = fase;
        this.nuevaFase = nuevaFase;
    }

    public int getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getNuevaFase() {
        return nuevaFase;
    }

    public void setNuevaFase(String nuevaFase) {
        this.nuevaFase = nuevaFase;
    }
    
    public String SQLScript(){
        return "UPDATE "+Var.dbNameBoes+".multa SET fase="+Varios.comillas(this.nuevaFase)+" "
                + "WHERE "
                + "idOrganismo="+this.idOrigen+" "
                + "AND "
                + "fase="+Varios.comillas(this.fase)+" "
                + "AND "
                + "expediente like '"+this.cabecera+"%'";
               
    }

}
