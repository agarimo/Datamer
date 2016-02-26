package datamer.model.boes.enty;

import datamer.Var;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class OrigenArticulo {
    private int idOrigen;
    private String fase;
    private String articulo;
    private String nuevaFase;
    
    public OrigenArticulo(){
        
    }

    public OrigenArticulo(int idOrigen, String fase, String articulo, String nuevaFase) {
        this.idOrigen = idOrigen;
        this.fase = fase;
        this.articulo = articulo;
        this.nuevaFase = nuevaFase;
    }

    public int getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getNuevaFase() {
        return nuevaFase;
    }

    public void setNuevaFase(String nuevaFase) {
        this.nuevaFase = nuevaFase;
    }
    
    public String SQLScript(){
        return "UPDATE "+Var.dbNameBoes+".multa SET fase="+Varios.entrecomillar(this.nuevaFase)+" "
                + "WHERE "
                + "idOrganismo="+this.idOrigen+" "
                + "AND "
                + "fase="+Varios.entrecomillar(this.fase)+" "
                + "AND "
                + "articulo="+Varios.entrecomillar(articulo)+";";
               
    }
}
