package datamer.model;

import util.Varios;

/**
 *
 * @author Agárimowe
 */
public class Cve {
    private int id;
    private String codigo;
    private String cve;
    private String link;
    
    public Cve(){
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    
    public String SQLBuscar(){
        return "";
    }
    
    public String SQLEditar(){
        return "UPDATE boes_stats.boletines SET cve="+Varios.entrecomillar(this.cve)+" WHERE id="+this.id;
    }
}
