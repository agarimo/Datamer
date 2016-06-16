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
    private String codigo;
    private String tipo;
    private String fase;
    private int isFase;
    private int isEstructura;
    private int idioma;

    public Boletin() {
    }

    public Boletin(int id, int idOrigen, int idBoe, String codigo,String tipo, String fase,
            int isFase,int isEstructura,int idioma) {
        this.id = id;
        this.idOrigen = idOrigen;
        this.idBoe = idBoe;
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
        return "INSERT into " + Var.dbNameBoes + ".boletin (idOrigen,idBoe,codigo,tipo,fase,isFase,isEstructura,idioma) values("
                + this.idOrigen + ","
                + this.idBoe + ","
                + Varios.comillas(this.codigo) + ","
                + Varios.comillas(this.tipo) + ","
                + Varios.comillas(this.fase) + ","
                + this.isFase + ","
                + this.isEstructura + ","
                + this.idioma
                + ");";
    }
    
    public String SQLEditar(){
        return "UPDATE " + Var.dbNameBoes + ".boletin SET "
                + "tipo=" + Varios.comillas(this.tipo) + ","
                + "fase=" + Varios.comillas(this.fase) + ","
                + "isFase=" + this.isFase + ","
                + "isEstructura=" + this.isEstructura + ","
                + "idioma=" + this.idioma + " "
                + "WHERE id=" + this.id;
    }
    
    public String SQLBuscar(){
        return "SELECT * from "+Var.dbNameBoes+".boletin where codigo="+Varios.comillas(this.codigo);
    }

    public String SQLUpdateData(String datos) {
        return "UPDATE " + Var.dbNameServer + ".publicacion SET "
                + "datos=" + Varios.comillas(datos) + " "
                + "WHERE codigo=" + Varios.comillas(this.codigo);
    }
}
