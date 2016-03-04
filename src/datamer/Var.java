/*
 * The MIT License
 *
 * Copyright 2016 Agarimo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package datamer;

import com.sun.javafx.application.HostServicesDelegate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import util.Conexion;
import util.Files;

/**
 *
 * @author Agarimo
 */
public class Var {

    public static Stage stage;
    public static Stage popup;
    public static HostServicesDelegate hostServices;

    public static Conexion con;

    public static String configFile = "config.xml";
    public static String defaultFile = "/resources/default.xml";
//    public static String dbName = "idbl";

//    public static String dbNameStats = "idbl_stats";

    public static boolean modoAdmin;
    public static String passwordAdmin;
    public static String queryLimit;

    public static File runtimeData;
    public static boolean isRunning;

    /**
     * BOES
     */
    public static String dbNameBoes = "boes";
    public static String dbNameBoesStats = "boes_stats";

    public static File ficheroPdf;
    public static File ficheroTxt;
    public static File ficheroEx;
    public static File ficheroUnion;

    public static boolean boesIsDownloading;
    public static boolean boesIsClasificando;
    
    /**
     * TESTRA
     */
    
    public static String dbNameTestra = "datagest";

    public static void initVar() {
        initVarDriver();
        initVarLoadConfig();
        initVarFiles();
        boesIsClasificando = false;
        boesIsDownloading = false;
    }

    private static void initVarDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initVarFiles() {
        ficheroPdf = new File(new File("data"), "pdfData");
        ficheroTxt = new File(new File("data"), "txtData");
        ficheroEx = new File(new File("data"), "exData");
        ficheroUnion = new File(new File("data"), "unionData");

        if (!ficheroPdf.exists()) {
            ficheroPdf.mkdirs();
        }
        if (!ficheroTxt.exists()) {
            ficheroTxt.mkdirs();
        }
        
        if (!ficheroEx.exists()) {
            ficheroEx.mkdirs();
        }
        
        if (!ficheroUnion.exists()) {
            ficheroUnion.mkdirs();
        }
    }

    private static void initVarLoadConfig() {
        XMLLoad(configFile);
    }

    public static void xit() {
        Files.borraDirectorio(runtimeData);
        XMLSave(configFile);
    }

    //<editor-fold defaultstate="collapsed" desc="XML">
    private static void XMLLoad(String ruta) {
        try {
            con = new Conexion();
            SAXBuilder builder = new SAXBuilder();
            File xmlFile = new File(ruta);

            Document document = (Document) builder.build(xmlFile);
            Element config = document.getRootElement();

            Element conexion = config.getChild("conexion");
            con.setDireccion(conexion.getChildText("db-host"));
            con.setPuerto(conexion.getChildText("db-port"));
            con.setUsuario(conexion.getChildText("db-username"));
            con.setPass(conexion.getChildText("db-password"));

        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void XMLSave(String ruta) {
        try {
            Element ele;
            SAXBuilder builder = new SAXBuilder();
            File xmlFile = new File(ruta);

            Document document = (Document) builder.build(xmlFile);
            Element config = document.getRootElement();

            Element conexion = config.getChild("conexion");
            ele = conexion.getChild("db-host");
            ele.setText(con.getDireccion());
            ele = conexion.getChild("db-port");
            ele.setText(con.getPuerto());
            ele = conexion.getChild("db-username");
            ele.setText(con.getUsuario());
            ele = conexion.getChild("db-password");
            ele.setText(con.getPass());

            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            try {
                outputter.output(document, new FileOutputStream(configFile));
            } catch (Exception e) {
                e.getMessage();
            }

        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//</editor-fold>
}
