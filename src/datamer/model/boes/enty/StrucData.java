package datamer.model.boes.enty;

import datamer.Var;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class StrucData {

    private int id;
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

    public StrucData(int id) {
        this.id = id;
        this.expediente = 0;
        this.sancionado = 0;
        this.nif = 0;
        this.localidad = 0;
        this.fechaMulta = 0;
        this.matricula = 0;
        this.cuantia = 0;
        this.preceptoA = 0;
        this.preceptoB = 0;
        this.preceptoC = 0;
        this.articuloA = 0;
        this.articuloB = 0;
        this.articuloC = 0;
        this.articuloD = 0;
        this.puntos = 0;
        this.reqObs = 0;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setArticuloD(int articuloD) {
        this.articuloD = articuloD;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setReqObs(int reqObs) {
        this.reqObs = reqObs;
    }

    public int getId() {
        return id;
    }

    public int getExpediente() {
        return expediente;
    }

    public int getSancionado() {
        return sancionado;
    }

    public int getNif() {
        return nif;
    }

    public int getLocalidad() {
        return localidad;
    }

    public int getFechaMulta() {
        return fechaMulta;
    }

    public int getMatricula() {
        return matricula;
    }

    public int getCuantia() {
        return cuantia;
    }

    public int getPreceptoA() {
        return preceptoA;
    }

    public int getPreceptoB() {
        return preceptoB;
    }

    public int getPreceptoC() {
        return preceptoC;
    }

    public int getArticuloA() {
        return articuloA;
    }

    public int getArticuloB() {
        return articuloB;
    }

    public int getArticuloC() {
        return articuloC;
    }

    public int getArticuloD() {
        return articuloD;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getReqObs() {
        return reqObs;
    }

    public static String SQLBuscar(int id) {
        return "SELECT * FROM " + Var.dbNameBoes + ".strucdata WHERE id=" + id;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".strucdata (id,expediente,sancionado,nif,localidad,fechaMulta,matricula,cuantia,"
                + " articuloA, articuloB, articuloC, articuloD, preceptoA, preceptoB, preceptoC, puntos, reqObs) values("
                + this.id + ","
                + this.expediente + ","
                + this.sancionado + ","
                + this.nif + ","
                + this.localidad + ","
                + this.fechaMulta + ","
                + this.matricula + ","
                + this.cuantia + ","
                + this.articuloA + ","
                + this.articuloB + ","
                + this.articuloC + ","
                + this.articuloD + ","
                + this.preceptoA + ","
                + this.preceptoB + ","
                + this.preceptoC + ","
                + this.puntos + ","
                + this.reqObs + ") "
                + "ON DUPLICATE KEY "
                + "UPDATE "
                + "expediente=" + this.expediente + ","
                + "sancionado=" + this.sancionado + ","
                + "nif=" + this.nif + ","
                + "localidad=" + this.localidad + ","
                + "fechaMulta=" + this.fechaMulta + ","
                + "matricula=" + this.matricula + ","
                + "cuantia=" + this.cuantia + ","
                + "articuloA=" + this.articuloA + ","
                + "articuloB=" + this.articuloB + ","
                + "articuloC=" + this.articuloC + ","
                + "articuloD=" + this.articuloD + ","
                + "preceptoA=" + this.preceptoA + ","
                + "preceptoB=" + this.preceptoB + ","
                + "preceptoC=" + this.preceptoC + ","
                + "puntos=" + this.puntos + ","
                + "reqObs=" + this.reqObs + ";";
    }
}
