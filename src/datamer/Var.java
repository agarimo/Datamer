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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import sql.Conexion;
import tools.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Agarimo
 */
public class Var {

    /**
     * Variables de configuración.
     */
    private static Properties config;
    public static String configFile = "config.xml";

    /**
     * Variables de Ventanas.
     */
    public static Stage stage;
//    public static WinC mainController;

    /**
     * Variables de conexión.
     */
    public static Conexion con;
    public static String socketClientHost;
    public static int socketClientPort;

    /**
     * Gestor de ejecuciones en segundo plano.
     */
    public static ExecutorService executor;

    /**
     * Fichero principal datos de programa.
     */
    public static File fileSystem;

    /**
     * Fichero remoto datos de programa.
     */
    public static File fileRemote;

    /**
     * Fichero almacenaje salida de datos (BB0,BB1).
     */
    public static File ficheroTxt;

    /**
     * Fichero almacenaje PDF y Excell.
     */
    public static File ficheroEx;

    /**
     * Fichero almacenaje Union.
     */
    public static File ficheroUnion;

    /**
     * Archivo temporal para previsualizaciones.
     */
    public static File temporalTxt;

    //<editor-fold defaultstate="collapsed" desc="VARIABLES CONEXION DATABASE">
    //BOES
    public static String dbNameServer = "server";
    public static String dbNameBoes = "boes";

    //TELEMARKETING
    public static String dbNameTkm = "tkm";
    //</editor-fold>

    public static void initVar() {
        initVarDriver();
        initVarLoadConfig();
        initConnection();
        initVarFiles();
        initVarKeyStore();
        executor = Executors.newFixedThreadPool(4);
    }

    private static void initVarDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initVarFiles() {
        fileSystem = new File("data");
        fileRemote = new File("////SERVER/Domain$/appData");
        temporalTxt = new File(fileSystem, "temp.txt");
        ficheroTxt = new File(fileSystem, "txtData");
        ficheroEx = new File(fileSystem, "exData");
        ficheroUnion = new File(fileSystem, "unionData");

        if (!fileSystem.exists()) {
            fileSystem.mkdirs();
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

        try {
            temporalTxt.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initVarKeyStore() {
        System.setProperty("javax.net.ssl.keyStore", "keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "Carras-24");
    }

    private static void initVarLoadConfig() {
        loadConfig();
    }

    private static void initConnection() {
        con = new Conexion();
        con.setDireccion(config.getProperty("con_host"));
        con.setPuerto(config.getProperty("con_port"));
        con.setUsuario(config.getProperty("con_user"));
        con.setPass(config.getProperty("con_pass"));

        socketClientHost = config.getProperty("socketClient_host");
        socketClientPort = Integer.parseInt(config.getProperty("socketClient_port"));
    }

    public static void xit() {
        Files.deleteDirContent(fileSystem);
        saveConfig();
    }

    private static void loadConfig() {
        config = new Properties();
        try (InputStream in = new FileInputStream("config.xml")) {
            config.loadFromXML(in);
        } catch (IOException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveConfig() {
        try (OutputStream out = new FileOutputStream("config.xml")) {
            config.storeToXML(out, "Archivo de propiedades XML de Datamer_server");
        } catch (IOException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
