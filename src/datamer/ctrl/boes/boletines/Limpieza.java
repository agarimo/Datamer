package datamer.ctrl.boes.boletines;

import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.Boletin;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Sql;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Limpieza {

    private Sql bd;
    private Boletin boletin;
//    private List cabeceras;
    private String datos;
    
    public Limpieza(){
        
    }

    public Limpieza(String codigo) {
        boletin = Query.getBoletin("SELECT * FROM " + Var.dbNameBoes + ".boletin where codigo=" + Varios.entrecomillar(codigo));
        if (boletin != null) {
            cargarDatos();
        }
    }

    public Limpieza(Boletin boletin) {
        this.boletin = boletin;
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            bd = new Sql(Var.con);
            datos = bd.getString("SELECT datos FROM " + Var.dbNameBoes + ".descarga where id=" + boletin.getIdDescarga());
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Limpieza.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        List lista = new ArrayList();
        StringBuilder buffer = new StringBuilder();
        String[] split = datos.split(System.getProperty("line.separator"));

        for (String split1 : split) {

            if (!lista.contains(split1)) {
                lista.add(split1);
                buffer.append(split1);
                buffer.append(System.getProperty("line.separator"));
            }
        }
        guardaDatos(buffer.toString());
    }

    private void guardaDatos(String datos) {
        datos = datos.replace("'", "\\'");

        try {
            bd = new Sql(Var.con);
            bd.ejecutar("UPDATE " + Var.dbNameBoes + ".descarga SET datos=" + Varios.entrecomillar(datos) + " where id=" + boletin.getIdDescarga());
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Limpieza.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void runSingle() {
//    }
//    
//    private void lector(String cabecera) {
//        boolean leer = true;
//        StringBuilder buffer = new StringBuilder();
//        String[] split = datos.split(System.getProperty("line.separator"));
//
//        for (String split1 : split) {
//
//            if (split1.contains(cabecera)) {
//                leer = false;
//            }
//
//            if (leer) {
//                buffer.append(split1);
//                buffer.append(System.getProperty("line.separator"));
//            }
//        }
//        escribeArchivo(buffer.toString());
//    }

//    private void escribeArchivo(String datos) {
//        File aux = new File("tempLp.txt");
//
//        try {
//            aux.createNewFile();
//        } catch (IOException ex) {
//            Logger.getLogger(Limpieza.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        Files.escribeArchivo(aux, datos);
//
//        try {
//            Desktop.getDesktop().browse(aux.toURI());
//        } catch (IOException ex) {
//            Logger.getLogger(Limpieza.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
