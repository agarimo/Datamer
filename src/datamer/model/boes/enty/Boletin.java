package datamer.model.boes.enty;

import datamer.Var;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Boletin {
    private int id;
    private int idOrigen;
    private int idBoe;
    private int idDescarga;
    private String codigo;
    private String tipo;
    private String fase;
    private int isFase;
    private int isEstructura;
    private int idioma;

    public Boletin() {
    }

    public Boletin(int id, int idOrigen, int idBoe, int idDescarga, String codigo,String tipo, String fase,
            int isFase,int isEstructura,int idioma) {
        this.id = id;
        this.idOrigen = idOrigen;
        this.idBoe = idBoe;
        this.idDescarga = idDescarga;
        this.codigo = codigo;
        this.tipo=tipo;
        this.fase = fase;
        this.isFase=isFase;
        this.isEstructura=isEstructura;
        this.idioma=idioma;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        this.idOrigen = idOrigen;
    }

    public int getIdBoe() {
        return idBoe;
    }

    public void setIdBoe(int idBoe) {
        this.idBoe = idBoe;
    }

    public int getIdDescarga() {
        return idDescarga;
    }

    public void setIdDescarga(int idDescarga) {
        this.idDescarga = idDescarga;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdioma() {
        return idioma;
    }

    public void setIdioma(int idioma) {
        this.idioma = idioma;
    }

    public int getIsFase() {
        return isFase;
    }

    public void setIsFase(int isFase) {
        this.isFase = isFase;
    }

    public int getIsEstructura() {
        return isEstructura;
    }

    public void setIsEstructura(int isEstructura) {
        this.isEstructura = isEstructura;
    }

    @Override
    public String toString() {
        return codigo;
    }
    
    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".boletin (idOrigen,idBoe,idDescarga,codigo,tipo,fase,isFase,isEstructura,idioma) values("
                + this.idOrigen + ","
                + this.idBoe + ","
                + this.idDescarga + ","
                + Varios.entrecomillar(this.codigo) + ","
                + Varios.entrecomillar(this.tipo) + ","
                + Varios.entrecomillar(this.fase) + ","
                + this.isFase + ","
                + this.isEstructura + ","
                + this.idioma
                + ");";
    }
    
    public String SQLEditar(){
        return "UPDATE " + Var.dbNameBoes + ".boletin SET "
                + "tipo=" + Varios.entrecomillar(this.tipo) + ","
                + "fase=" + Varios.entrecomillar(this.fase) + ","
                + "isFase=" + this.isFase + ","
                + "isEstructura=" + this.isEstructura + ","
                + "idioma=" + this.idioma + " "
                + "WHERE id=" + this.id;
    }
    
    public String SQLBuscar(){
        return "SELECT * from "+Var.dbNameBoes+".boletin where codigo="+Varios.entrecomillar(this.codigo);
    }
}
