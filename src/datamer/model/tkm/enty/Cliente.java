package datamer.model.tkm.enty;

import datamer.Var;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Cliente {

    private int id;
    private int estado;
    private String cif;
    private String nombre;
    private String telefono;
    private String contacto;
    private String mail;
    private List<Comentario> comentarios;

    public Cliente() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List getComentarios() {
        return comentarios;
    }

    public void setComentarios(List comentarios) {
        this.comentarios = comentarios;
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameTkm + ".cliente (estado,cif,nombre,telefono,contacto,mail) values("
                + this.estado + ","
                + Varios.entrecomillar(this.cif) + ","
                + Varios.entrecomillar(this.nombre) + ","
                + Varios.entrecomillar(this.telefono) + ","
                + Varios.entrecomillar(this.contacto) + ","
                + Varios.entrecomillar(this.mail)
                + ");";
    }

    public int SQLGuardar() {
        try {
            int idBd;
            Sql bd = new Sql(Var.con);
            bd.ejecutar(this.SQLCrear());
            idBd=bd.ultimoRegistro();
            bd.close();
            
            return idBd;
        } catch (SQLException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
