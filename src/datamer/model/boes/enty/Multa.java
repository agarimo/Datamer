package datamer.model.boes.enty;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import datamer.Regex;
import datamer.Var;
import util.CalculaNif;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Multa {

    private int id;
    private int idBoletin;
    private String codigoSancion;
    private Date fechaPublicacion;
    private int idOrganismo;
    private String organismo;
    private String boe;
    private String fase;
    private String tipoJuridico;
    private String plazo;
    private String expediente;
    private Date fechaMulta;
    private String articulo;
    private String nif;
    private String sancionado;
    private String localidad;
    private String matricula;
    private String cuantia;
    private String puntos;
    private String reqObs;
    private String linea;

    public Multa() {

    }

    public Multa(String expediente) {
        this.expediente = expediente;
    }

    public Multa(int idBoletin, String codigoSancion, Date fechaPublicacion, int idOrganismo, String organismo, String boe, String fase, String tipoJuridico, String plazo, String expediente, Date fechaMulta, String articulo, String nif, String sancionado, String localidad, String matricula, String cuantia, String puntos, String reqObs, String linea) {
        this.idBoletin = idBoletin;
        this.codigoSancion = codigoSancion;
        this.fechaPublicacion = fechaPublicacion;
        this.idOrganismo = idOrganismo;
        this.organismo = organismo;
        this.boe = boe;
        this.fase = fase;
        this.tipoJuridico = tipoJuridico;
        this.plazo = plazo;
        this.expediente = expediente;
        this.fechaMulta = fechaMulta;
        this.articulo = articulo;
        this.nif = nif;
        this.sancionado = sancionado;
        this.localidad = localidad;
        this.matricula = matricula;
        this.cuantia = cuantia;
        this.puntos = puntos;
        this.reqObs = reqObs;
        this.linea = linea;
    }

    public Multa(int id, int idBoletin, String codigoSancion, Date fechaPublicacion, int idOrganismo, String organismo, String boe, String fase, String tipoJuridico, String plazo, String expediente, Date fechaMulta, String articulo, String nif, String sancionado, String localidad, String matricula, String cuantia, String puntos, String reqObs, String linea) {
        this.id = id;
        this.idBoletin = idBoletin;
        this.codigoSancion = codigoSancion;
        this.fechaPublicacion = fechaPublicacion;
        this.idOrganismo = idOrganismo;
        this.organismo = organismo;
        this.boe = boe;
        this.fase = fase;
        this.tipoJuridico = tipoJuridico;
        this.plazo = plazo;
        this.expediente = expediente;
        this.fechaMulta = fechaMulta;
        this.articulo = articulo;
        this.nif = nif;
        this.sancionado = sancionado;
        this.localidad = localidad;
        this.matricula = matricula;
        this.cuantia = cuantia;
        this.puntos = puntos;
        this.reqObs = reqObs;
        this.linea = linea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBoletin() {
        return idBoletin;
    }

    public void setIdBoletin(int idBoletin) {
        this.idBoletin = idBoletin;
    }

    public String getCodigoSancion() {
        return codigoSancion;
    }

    public void setCodigoSancion(String codigoSancion) {
        this.codigoSancion = codigoSancion;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getIdOrganismo() {
        return idOrganismo;
    }

    public void setIdOrganismo(int idOrganismo) {
        this.idOrganismo = idOrganismo;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public String getBoe() {
        return boe;
    }

    public void setBoe(String boe) {
        this.boe = boe;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getTipoJuridico() {
        return tipoJuridico;
    }

    public void setTipoJuridico(String tipoJuridico) {
        this.tipoJuridico = tipoJuridico;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Date getFechaMulta() {
        return fechaMulta;
    }

    public void setFechaMulta(Date fechaMulta) {
        this.fechaMulta = fechaMulta;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = checkNifPattern(checkNif(limpia(nif)));
    }

    public String getSancionado() {
        return sancionado;
    }

    public void setSancionado(String sancionado) {
        this.sancionado = sancionado;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = checkMatricula(limpia(matricula));
    }

    public String getCuantia() {
        return cuantia;
    }

    public void setCuantia(String cuantia) {
        this.cuantia = cuantia;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getReqObs() {
        return reqObs;
    }

    public void setReqObs(String reqObs) {
        this.reqObs = reqObs;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    @Override
    public String toString() {
        return "Multa: "+Dates.imprimeFecha(fechaPublicacion)+" - "+this.idOrganismo+" - "+this.expediente;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.idBoletin;
        hash = 29 * hash + Objects.hashCode(this.expediente);
        hash = 29 * hash + Objects.hashCode(this.nif);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Multa other = (Multa) obj;
        if (this.idBoletin != other.idBoletin) {
            return false;
        }
        if (!Objects.equals(this.expediente, other.expediente)) {
            return false;
        }
        if (!Objects.equals(this.nif, other.nif)) {
            return false;
        }
        return true;
    }

    private String limpia(String str) {
        Pattern p = Pattern.compile("[^0-9A-Z]");
        Matcher m = p.matcher(str);

        if (m.find()) {
            str = m.replaceAll("");
        }
        return str.trim();
    }

    private String checkMatricula(String matricula) {
        Regex rx = new Regex();
        String str;

        if (rx.isBuscar("[C]{1}[0]{2}[0-9]{4}[A-Z]{3}", matricula)) {
            str = matricula.substring(0, 1);
            str = str + matricula.substring(3, matricula.length());
        } else if (rx.isBuscar("[0]{2}[0-9]{4}[A-Z]{3}", matricula)) {
            str = matricula.substring(2, matricula.length());
        } else if (rx.isBuscar("[A-Z]{1}[0]{2}[0-9]{4}[A-Z]{1,2}", matricula)) {
            str = matricula.substring(0, 1);
            str = str + matricula.substring(3, matricula.length());
        } else if (rx.isBuscar("[A-Z]{2}[0]{2}[0-9]{4}[A-Z]{1,2}", matricula)) {
            str = matricula.substring(0, 2);
            str = str + matricula.substring(4, matricula.length());
        } else if (rx.isBuscar("[0-9]{3}[A-Z]{3}", matricula)) {
            str = "0" + matricula;
        } else if (rx.isBuscar("[0-9]{2}[A-Z]{3}", matricula)) {
            str = "00" + matricula;
        }else{
            str=matricula;
        }

        return str;
    }

    private String checkNif(String nif) {
        CalculaNif cn = new CalculaNif();
        String aux;

        try {
            if (cn.isvalido(nif)) {
                aux = nif;
            } else {
                aux = cn.calcular(nif);
            }
            return aux;
        } catch (Exception ex) {
            return nif;
        }
    }

    private String checkNifPattern(String nif) {
        Regex rx = new Regex();
        String str;

        if (rx.isBuscar("[0-9]{4,7}[TRWAGMYFPDXBNJZSQVHLCKE]{1}", nif)) {
            str = add0(nif, 9);
        } else if (rx.isBuscar("[0]{1}[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]{1}", nif)) {
            str = nif.substring(1, nif.length());
        } else if (rx.isBuscar("[XYZ]{1}[0-9]{4,6}[TRWAGMYFPDXBNJZSQVHLCKE]{1}", nif)) {
            str = nif.substring(0, 1);
            str = str + add0(nif.substring(1, nif.length()), 8);
        } else if (rx.isBuscar("[XYZ]{1}[0]{1}[0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKE]{1}", nif)) {
            str = nif.substring(0, 1);
            str = str + nif.substring(2, nif.length());
        } else if (rx.isBuscar("[ABCDEFGHJKLMNPQRSUVW]{1}[0]{1}[0-9]{8}", nif)) {
            str = nif.substring(0, 1);
            str = str + nif.substring(2, nif.length());
        }else{
            str=nif;
        } 

        return str;
    }

    private String add0(String aux, int size) {

        while (aux.length() < size) {
            aux = "0" + aux;
        }

        return aux;
    }

    public String SQLCrear() {
        if (this.fechaMulta != null) {
            return "INSERT into " + Var.dbNameBoes + ".multa (idBoletin,codigoSancion,fechaPublicacion,idOrganismo,organismo,boe,fase,tipoJuridico,plazo,expediente,fechaMulta,articulo,nif,sancionado,localidad,matricula,cuantia,puntos,reqObs,linea) values("
                    + this.idBoletin + ","
                    + Varios.comillas(this.codigoSancion) + ","
                    + Varios.comillas(Dates.imprimeFecha(fechaPublicacion)) + ","
                    + this.idOrganismo + ","
                    + Varios.comillas(this.organismo) + ","
                    + Varios.comillas(this.boe) + ","
                    + Varios.comillas(this.fase) + ","
                    + Varios.comillas(this.tipoJuridico) + ","
                    + Varios.comillas(this.plazo) + ","
                    + Varios.comillas(this.expediente) + ","
                    + Varios.comillas(Dates.imprimeFecha(this.fechaMulta)) + ","
                    + Varios.comillas(this.articulo) + ","
                    + Varios.comillas(this.nif) + ","
                    + Varios.comillas(this.sancionado) + ","
                    + Varios.comillas(this.localidad) + ","
                    + Varios.comillas(this.matricula) + ","
                    + Varios.comillas(this.cuantia) + ","
                    + Varios.comillas(this.puntos) + ","
                    + Varios.comillas(this.reqObs) + ","
                    + Varios.comillas(this.linea)
                    + ");";
        } else {
            return "INSERT into " + Var.dbNameBoes + ".multa (idBoletin,codigoSancion,fechaPublicacion,idOrganismo,organismo,boe,fase,tipoJuridico,plazo,expediente,fechaMulta,articulo,nif,sancionado,localidad,matricula,cuantia,puntos,reqObs,linea) values("
                    + this.idBoletin + ","
                    + Varios.comillas(this.codigoSancion) + ","
                    + Varios.comillas(Dates.imprimeFecha(fechaPublicacion)) + ","
                    + this.idOrganismo + ","
                    + Varios.comillas(this.organismo) + ","
                    + Varios.comillas(this.boe) + ","
                    + Varios.comillas(this.fase) + ","
                    + Varios.comillas(this.tipoJuridico) + ","
                    + Varios.comillas(this.plazo) + ","
                    + Varios.comillas(this.expediente) + ","
                    + null + ","
                    + Varios.comillas(this.articulo) + ","
                    + Varios.comillas(this.nif) + ","
                    + Varios.comillas(this.sancionado) + ","
                    + Varios.comillas(this.localidad) + ","
                    + Varios.comillas(this.matricula) + ","
                    + Varios.comillas(this.cuantia) + ","
                    + Varios.comillas(this.puntos) + ","
                    + Varios.comillas(this.reqObs) + ","
                    + Varios.comillas(this.linea)
                    + ");";
        }
    }
    
    public String SQLEditarOrganismo(String organismo){
        return "UPDATE boes.multa SET organismo="+Varios.comillas(organismo)+" WHERE id="+this.id;
    }
    
    public String SQLEditarFase(String fase){
        return "UPDATE boes.multa SET fase="+Varios.comillas(fase)+" WHERE id="+this.id;
    }
}
