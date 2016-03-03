package datamer.model.boes.enty;

import java.util.Objects;
import datamer.Var;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Fase {

    private int id;
    private int idOrigen;
    private String codigo;
    private int tipo;
    private String texto1;
    private String texto2;
    private String texto3;
    private int dias;

    public Fase() {

    }

    public Fase(int idOrigen, String codigo, int tipo, String texto1, String texto2, String texto3, int dias) {
        this.idOrigen = idOrigen;
        this.codigo = codigo;
        this.tipo = tipo;
        this.texto1 = texto1;
        this.texto2 = texto2;
        this.texto3 = texto3;
        this.dias = dias;
    }

    public Fase(int id, int idOrigen, String codigo, int tipo, String texto1, String texto2, String texto3, int dias) {
        this.id = id;
        this.idOrigen = idOrigen;
        this.codigo = codigo;
        this.tipo = tipo;
        this.texto1 = texto1;
        this.texto2 = texto2;
        this.texto3 = texto3;
        this.dias = dias;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
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

    public String getTexto1() {
        return texto1;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTexto2() {
        return texto2;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public String getTexto3() {
        return texto3;
    }

    public void setTexto3(String texto3) {
        this.texto3 = texto3;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "(" + codigo + ")" + dias + tipoToString();
    }

    public boolean contiene(String str) {
        return str.contains(this.texto1) && str.contains(this.texto2) && str.contains(this.texto3);
    }

    private String tipoToString() {
        switch (tipo) {
            case 1:
                return "ND";
            case 2:
                return "RS";
            case 3:
                return "RR";
            default:
                return "Desconocido";
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.idOrigen;
        hash = 67 * hash + Objects.hashCode(this.codigo);
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
        final Fase other = (Fase) obj;
        return true;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameBoes + ".fase (idOrigen, codigo, tipo, texto1, texto2,texto3, dias) values("
                + this.idOrigen + ","
                + Varios.entrecomillar(this.codigo) + ","
                + this.tipo + ","
                + Varios.entrecomillar(getTexto1()) + ","
                + Varios.entrecomillar(getTexto2()) + ","
                + Varios.entrecomillar(getTexto3()) + ","
                + this.dias
                + ")";
    }

    public String SQLEditar() {
        return "UPDATE " + Var.dbNameBoes + ".fase SET "
                + "codigo=" + Varios.entrecomillar(this.codigo) + ","
                + "tipo=" + this.tipo + ","
                + "dias=" + this.dias + ","
                + "texto1=" + Varios.entrecomillar(this.texto1) + ","
                + "texto2=" + Varios.entrecomillar(this.texto2) + ","
                + "texto3=" + Varios.entrecomillar(this.texto3)
                + "WHERE id=" + this.id;
    }

    public String SQLBorrar() {
        return "DELETE FROM " + Var.dbNameBoes + ".fase WHERE id=" + this.id + ";";
    }

    public String SQLBuscar() {
        return "SELECT * FROM " + Var.dbNameBoes + ".fase "
                + "WHERE texto1=" + Varios.entrecomillar(this.texto1) + " "
                + "and texto2=" + Varios.entrecomillar(this.texto2) + " "
                + "and texto3=" + Varios.entrecomillar(this.texto3) + " "
                + "and idOrigen=" + this.idOrigen;
    }
}
