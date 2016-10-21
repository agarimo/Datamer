package datamer.ctrl.boes.ext;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.enty.Multa;
import datamer.model.boes.enty.Procesar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import tools.Dates;
import tools.LoadFile;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class INS {

    private final File fichero;
    private final LocalDate fecha;
    private final List<Procesar> boletines;
    private final List<Procesar> doc;
    private final List<String[]> docData;
    private final List<String[]> data;

    public INS(LocalDate fecha) {
        this.fecha = fecha;
        data = new ArrayList();
        docData = new ArrayList();
        this.boletines = Query
                .listaProcesar("SELECT * FROM " + Var.dbNameBoes + ".procesar "
                        + "WHERE fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE))
                        + " AND estado!=1");
        this.doc = Query
                .listaProcesar("SELECT * FROM " + Var.dbNameBoes + ".procesar "
                        + "WHERE fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)));
        fichero = new File(Var.ficheroTxt, fecha.format(DateTimeFormatter.ISO_DATE));
        fichero.mkdirs();
    }

    public void run() {
        data.clear();
        Procesar aux;
        Iterator<Procesar> it = doc.iterator();

        while (it.hasNext()) {
            aux = it.next();
            getDatosDoc(aux);
        }

        it = boletines.iterator();

        while (it.hasNext()) {
            aux = it.next();
            getDatos(aux);
        }

        crearArchivos();
    }

    private void getDatos(Procesar pr) {
        String[] linea;
        Multa multa;
        List<Multa> multas = Query.listaMultas("SELECT * FROM " + Var.dbNameBoes + ".multa WHERE idBoletin=" + pr.getId());

        Iterator<Multa> it = multas.iterator();

        while (it.hasNext()) {
            linea = new String[17];
            multa = it.next();

            linea[0] = pr.getFecha().format(DateTimeFormatter.ISO_DATE);
            linea[1] = splitNBoe(multa.getCodigoSancion());
            linea[2] = multa.getOrganismo();
            linea[3] = multa.getFase();
            linea[4] = multa.getPlazo();
            linea[5] = multa.getCodigoSancion();
            linea[6] = multa.getExpediente();
            linea[7] = Dates.imprimeFecha(multa.getFechaMulta());
            linea[8] = multa.getArticulo();
            linea[9] = multa.getNif();
            linea[10] = multa.getTipoJuridico();
            linea[11] = multa.getSancionado();
            linea[12] = multa.getMatricula();
            linea[13] = multa.getCuantia();
            linea[14] = multa.getPuntos();
            linea[15] = multa.getLocalidad();
            linea[16] = multa.getLinea();

            for (int i = 0; i < linea.length; i++) {
                String linea1 = linea[i];

                if (linea1 != null) {
                    String aux = linea1.replaceAll("\n", " ");
                    aux = aux.replaceAll(System.lineSeparator(), " ");
                    linea[i] = aux;
                }
            }

            data.add(linea);
        }
    }

    private void getDatosDoc(Procesar aux) {
        String[] linea = new String[4];
        linea[0] = aux.getCodigo().replace("BOE-N-20", "").replace("-", "");
        linea[1] = aux.getFecha().format(DateTimeFormatter.ISO_DATE);
        linea[2] = aux.getCodigo();
        linea[3] = aux.getLink();

        docData.add(linea);
    }

    private String splitNBoe(String codigo) {
        String aux;
        String[] split = codigo.split("-");
        aux = split[0] + split[1];

        split = aux.split("/");
        aux = split[0];

        return aux;
    }

    private String getDataArchivos() {
        StringBuilder sb = new StringBuilder();

        docData.stream().map((arr) -> {
            sb.append(getLinea(arr));
            return arr;
        }).forEach((item) -> {
            sb.append(System.lineSeparator());
        });

        for (int i = 0; i < data.size(); i++) {
            String[] arr = data.get(i);
            sb.append(getLinea(arr));

            if (i != data.size() - 1) {
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    private String getLinea(String[] linea) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < linea.length; i++) {
            sb.append(linea[i]);

            if (i != linea.length - 1) {
                sb.append("|");
            }
        }
        return sb.toString();
    }

    private void crearArchivos() {
        LoadFile.writeFile(new File(fichero, fecha.format(DateTimeFormatter.ISO_DATE) + ".ins"), getDataArchivos());
    }
}
