package datamer.model.tkm.enty;

import datamer.Var;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Comentario {
    
    private int id;
    private int idCliente;
    private String comentario;
    
    public Comentario(){
        
    }
    
    public Comentario (int idCliente, String comentario){
        this.idCliente=idCliente;
        this.comentario=comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String SQLCrear(){
        return "INSERT into " + Var.dbNameTkm + ".comentario (id_cliente,comentario,fecha) values("
                + this.idCliente + ","
                + Varios.comillas(this.comentario) + ","
                + "CURDATE()"
                + ");";
    }
}
