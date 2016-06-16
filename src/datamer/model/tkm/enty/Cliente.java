package datamer.model.tkm.enty;

import datamer.Var;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Sql;
import tools.Util;

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

    public static String SQLBuscarCif(String aux) {
        return "SELECT * FROM " + Var.dbNameTkm + ".cliente WHERE cif=" + Util.comillas(aux);
    }

    public static String SQLBuscarTelf(String aux) {
        return "SELECT * FROM " + Var.dbNameTkm + ".cliente WHERE telefono=" + Util.comillas(aux);
    }

    public String SQLCrear() {
        return "INSERT into " + Var.dbNameTkm + ".cliente (estado,cif,nombre,telefono,contacto,mail) values("
                + this.estado + ","
                + Util.comillas(this.cif) + ","
                + Util.comillas(this.nombre) + ","
                + Util.comillas(this.telefono) + ","
                + Util.comillas(this.contacto) + ","
                + Util.comillas(this.mail)
                + ");";
    }

    public String SQLEditar() {
        return "UPDATE " + Var.dbNameTkm + ".cliente SET "
                + "nombre=" + Util.comillas(this.nombre) + ","
                + "estado=" + this.estado + ","
                + "telefono=" + Util.comillas(this.telefono) + ","
                + "contacto=" + Util.comillas(this.contacto) + ","
                + "mail=" + Util.comillas(this.mail) + " "
                + "WHERE id=" + this.id;
    }
    
    public String SQLEliminar(){
        return "DELETE FROM "+Var.dbNameTkm+".cliente WHERE id="+this.id;
    }

    public int SQLGuardar() {
        try {
            int idBd;
            Sql bd = new Sql(Var.con);
            bd.ejecutar(this.SQLCrear());
            idBd = bd.ultimoRegistro();
            bd.close();

            return idBd;
        } catch (SQLException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
