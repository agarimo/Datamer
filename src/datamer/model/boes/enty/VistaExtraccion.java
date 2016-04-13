package datamer.model.boes.enty;

import datamer.Var;
import datamer.model.boes.Plazo;
import java.util.Calendar;
import java.util.Date;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class VistaExtraccion {

    private String codigo;
    private String entidad;
    private int idOrigen;
    private String origen;
    private Date fecha;
    private String tipo;
    private String faseCompleta;
    private String fase;
    private Plazo plazo;
    private String boe;
    
    public VistaExtraccion(){
        
    }

    public VistaExtraccion(String codigo, String entidad, int idOrigen, String origen, Date fecha, String tipo, String faseCompleta) {
        this.codigo = codigo;
        this.entidad = entidad;
        this.idOrigen = idOrigen;
        this.origen = origen.replace("'", "\'");
        this.fecha = fecha;
        this.tipo = tipo;
        this.faseCompleta = faseCompleta;
        splitFase();
        this.boe = getBoe(this.fecha,this.entidad);
    }

    public int getIdOrigen() {
        return idOrigen;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEntidad() {
        return entidad;
    }

    public String getOrigen() {
        return origen;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFase() {
        return fase;
    }

    public Plazo getPlazo() {
        return plazo;
    }

    public String getBoe() {
        return boe;
    }

    private void splitFase() {
        String aux = this.faseCompleta;
        String[] split = aux.split("-");
        aux = split[1];

        fase = aux.substring(1, 4);
        splitPlazo(aux.substring(5, 7));
    }
    
    private void splitPlazo(String plazo){
        switch (plazo) {
            case "10":
                this.plazo = Plazo.D10;
                break;
            case "15":
                this.plazo = Plazo.D15;
                break;
            case "20":
                this.plazo = Plazo.D20;
                break;
            case "30":
                this.plazo = Plazo.M1;
                break;
            case "60":
                this.plazo = Plazo.M2;
                break;
            default:
                this.plazo = Plazo.D10;
                break;
        }
    }

    private String getBoe(Date fecha,String entidad) {
        String str = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        int dia = cal.get(Calendar.DAY_OF_MONTH);
        if (dia < 10) {
            str = str + "0" + dia;
        } else {
            str = str + dia;
        }

        str = str + "0";

        String anno = Integer.toString(cal.get(Calendar.YEAR));
        str = str + anno.charAt(3);

        str = str + entidad;

        int mes = cal.get(Calendar.MONTH);
        mes++;
        if (mes < 10) {
            str = str + mes + "A";
        } else {
            if (mes == 10) {
                str = str + "XA";
            }
            if (mes == 11) {
                str = str + "YA";
            }
            if (mes == 12) {
                str = str + "ZA";
            }
        }
        return str;
    }

    public static String SQLBuscar(String codigo) {
        return "SELECT * FROM "+Var.dbNameBoes+".vista_extraccion where codigo=" + Varios.entrecomillar(codigo);
    }
}
