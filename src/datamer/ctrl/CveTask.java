package datamer.ctrl;

import datamer.model.Cve;
import files.LoadFile;
import java.io.File;
import java.util.List;
import java.util.Iterator;
import javafx.concurrent.Task;

/**
 *
 * @author agari
 */
public class CveTask extends Task {

    private final File pdf;
    private final File txt;
    private List lista;
    private final String query = "SELECT id,codigo,cve,link FROM boes_stats.boletines WHERE cve='null'";

    public CveTask() {
        pdf = new File(new File("data"), "cve.pdf");
        txt = new File(new File("data"), "txt.txt");
    }

    @Override
    protected Object call() throws Exception {
        this.updateMessage("Cargando datos");
        lista = Query.listaCve(query);
        process();
        clean();
        return null;
    }
    
    private void clean(){
        this.updateMessage("Finalizando proceso");
        pdf.delete();
        txt.delete();
    }

    private void process() {
        this.updateMessage("Iniciando proceso");
        int cont = 1;
        Cve aux;
        Iterator<Cve> it = lista.iterator();

        while (it.hasNext()) {
            this.updateMessage("Procesando "+cont+" de "+lista.size());
            this.updateProgress(cont, lista.size());
            aux = it.next();
            System.out.println(aux.getCodigo());

            if (descarga(aux.getLink())) {
                aux.setCve(getCve());
            } else {
                aux.setCve("No disponible");
            }
            Query.ejecutar(aux.SQLEditar());
            cont++;
        }
    }

    private boolean descarga(String link) {
        try {
            files.Download.downloadFILE(link, pdf);
            files.Pdf.convertPDF(pdf, txt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getCve() {
        LoadFile lf = new LoadFile(txt);
        String aux = "";
        Iterator<String> it = lf.getLineas().iterator();

        while (it.hasNext()) {
            aux = it.next();

            if (aux.contains("cve: BOE-N-")) {
                aux = aux.replace("cve: ", "").trim();
                System.out.println(aux);
                break;
            }
        }
        return aux;
    }
}
