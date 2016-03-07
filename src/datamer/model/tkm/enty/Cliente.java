package datamer.model.tkm.enty;

import java.util.List;

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
    private List comentarios;
    
    public Cliente(){
        
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
}
