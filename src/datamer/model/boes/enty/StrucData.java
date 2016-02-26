package datamer.model.boes.enty;

import datamer.Var;

/**
 *
 * @author Agarimo
 */
public class StrucData {
    int id;
    int idEstructura;
    
    public int expediente;
    public int sancionado;
    public int nif;
    public int localidad;
    public int fechaMulta;
    public int matricula;
    public int cuantia;
    public int preceptoA;
    public int preceptoB;
    public int preceptoC;
    public int articuloA;
    public int articuloB;
    public int articuloC;
    public int articuloD;
    public int puntos;
    public int reqObs;
    
    public StrucData(){
        
    }
    
    public StrucData(int idEstructura){
        this.idEstructura=idEstructura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdEstructura(int idEstructura) {
        this.idEstructura = idEstructura;
    }

    public void setExpediente(int expediente) {
        this.expediente = expediente;
    }

    public void setSancionado(int sancionado) {
        this.sancionado = sancionado;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public void setLocalidad(int localidad) {
        this.localidad = localidad;
    }

    public void setFechaMulta(int fechaMulta) {
        this.fechaMulta = fechaMulta;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public void setCuantia(int cuantia) {
        this.cuantia = cuantia;
    }

    public void setPreceptoA(int preceptoA) {
        this.preceptoA = preceptoA;
    }

    public void setPreceptoB(int preceptoB) {
        this.preceptoB = preceptoB;
    }

    public void setPreceptoC(int preceptoC) {
        this.preceptoC = preceptoC;
    }

    public void setArticuloA(int articuloA) {
        this.articuloA = articuloA;
    }

    public void setArticuloB(int articuloB) {
        this.articuloB = articuloB;
    }

    public void setArticuloC(int articuloC) {
        this.articuloC = articuloC;
    }
    
    public void setArticuloD(int articuloD){
        this.articuloD = articuloD;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setReqObs(int reqObs) {
        this.reqObs = reqObs;
    }
    
    public static String SQLBuscar(int idEstructura){
        return "SELECT * FROM "+Var.dbNameBoes+".strucdata WHERE idEstructura="+idEstructura;
    }
}
