package datamer.model.boes.enty;

/**
 *
 * @author Agarimo
 */
public class OrigenExpediente {
    private int idOrigen;
    private String cabecera;
    private String origen;
    
    public OrigenExpediente(){
        
    }

    public OrigenExpediente(int idOrigen, String cabecera, String origen) {
        this.idOrigen = idOrigen;
        this.cabecera = cabecera;
        this.origen = origen;
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

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    @Override
    public String toString() {
        return "OrigenExpediente{" + "idOrigen=" + idOrigen + ", cabecera=" + cabecera + ", origen=" + origen + '}';
    }
    
    public String SQLScript(){
        return "";
    }
    
    
}
