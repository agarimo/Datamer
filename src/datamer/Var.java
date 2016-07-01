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
import java.util.ArrayList;
import java.util.List;
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

    private static Properties config;

    public static Stage stage;
    public static Stage popup;

    public static ExecutorService executor;

    public static Conexion con;
    public static String socketClientHost;
    public static int socketClientPort;

    public static String configFile = "config.xml";

    public static File runtimeData;
    public static boolean isRunning;

    /**
     * BOES
     */
    public static String dbNameServer = "server";
    public static String dbNameBoes = "boes";

    public static File fileSystem;
    public static File ficheroPdf;
    public static File ficheroTxt;
    public static File ficheroEx;
    public static File ficheroUnion;

    public static boolean boesIsDownloading;
    public static boolean boesIsClasificando;

    /**
     * TKM
     */
    public static String dbNameTkm = "tkm";

    /**
     * TESTRA
     */
    public static String dbNameTestra = "testra";
    public static List<String> strucFecha;
    public static File fichero;
    public static File temporal;

    public static String testraUrl = "https://sedeapl.dgt.gob.es/WEB_TTRA_CONSULTA/ServletVisualizacion?params=";
    public static String testraHtml = "&formato=HTML";
    public static String testraPdf = "%26subidioma%3Des&formato=PDF";

    /**
     * IDBL
     */
    public static String dbNameIdbl = "idbl";

    public static void initVar() {
        initVarDriver();
        initVarLoadConfig();
        initConnection();
        initVarFiles();
        initVarStrucFecha();
        initVarKeyStore();
        boesIsClasificando = false;
        boesIsDownloading = false;
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
        fichero = new File(fileSystem, "testraData");
        temporal = new File(fileSystem, "temp.txt");
        ficheroPdf = new File(fileSystem, "pdfData");
        ficheroTxt = new File(fileSystem, "txtData");
        ficheroEx = new File(fileSystem, "exData");
        ficheroUnion = new File(fileSystem, "unionData");

        if (!fileSystem.exists()) {
            fileSystem.mkdirs();
        }

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

        if (!fichero.exists()) {
            fichero.mkdirs();
        }
        try {
            temporal.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initVarKeyStore() {
//        System.setProperty("javax.net.ssl.trustStore", "keystore");
//        System.setProperty("javax.net.ssl.trustStorePassword", "Carras-24");
//        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        System.setProperty("javax.net.ssl.keyStore", "keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "Carras-24");
//        System.setProperty("javax.net.ssl.keyStoreType", "JKS");
    }

    private static void initVarStrucFecha() {
        strucFecha = new ArrayList();

        strucFecha.add("dd-MM-yyyy");
        strucFecha.add("dd/MM/yyyy");
        strucFecha.add("dd-MM-yy");
        strucFecha.add("dd/MM/yy");

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
        Files.deleteDir(runtimeData);
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
