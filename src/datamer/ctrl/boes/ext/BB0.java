package datamer.ctrl.boes.ext;

import datamer.model.boes.enty.Multa;
import datamer.model.boes.enty.Procesar;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import datamer.Var;
import datamer.ctrl.boes.Query;
import datamer.model.boes.ModeloBoletines;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import tools.Dates;
import tools.LoadFile;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public final class BB0 {

    private final File fichero;
    private final LocalDate fecha;
    private final List<Procesar> boletines;
    private final List<ModeloBoletines> boletinesD;
    private final List<String[]> data;

    private final int BB0 = 1;
    private final int BB1 = 2;

    public BB0(LocalDate fecha, File fichero) {
        this.fecha = fecha;
        data = new ArrayList();
        this.boletines = Query
                .listaProcesar("SELECT * FROM " + Var.dbNameBoes + ".procesar "
                        + "WHERE fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE))
                        + " AND estado!=1");
        this.boletinesD = Query
                .listaModeloBoletines("SELECT * FROM " + Var.dbNameBoes + ".vista_boletines "
                        + "where fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)) + " "
                        + "and codigo in (select codigo from " + Var.dbNameBoes + ".procesar where estructura=-1 and estado=1)");
        this.fichero = new File(fichero, fecha.format(DateTimeFormatter.ISO_DATE));
        this.fichero.mkdirs();
    }

    public void run() {
        data.clear();
        Procesar aux;
        Iterator<Procesar> it = boletines.iterator();

        while (it.hasNext()) {
            aux = it.next();
            getDatos(aux);
        }
        crearArchivos();
    }

    private String getLinea(String[] linea, int tipo) {
        StringBuilder sb = new StringBuilder();

        switch (tipo) {
            case BB0:
                for (int i = 0; i < linea.length - 2; i++) {
                    sb.append(linea[i]);

                    if (i != 23) {
                        sb.append("|");
                    }
                }
                break;

            case BB1:
                for (int i = 0; i < linea.length; i++) {
                    sb.append(linea[i]);

                    if (i != 25) {
                        sb.append("|");
                    }
                }
                break;
        }
        return sb.toString();
    }

    private void getDatos(Procesar pr) {
        String[] linea;
        Multa multa;
        List<Multa> multas = Query.listaMultas("SELECT * FROM " + Var.dbNameBoes + ".multa WHERE idBoletin=" + pr.getId());

        Iterator<Multa> it = multas.iterator();

        while (it.hasNext()) {
            linea = new String[26];
            multa = it.next();

            linea[0] = "00000";
            linea[1] = pr.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            linea[2] = multa.getBoe();
            linea[3] = multa.getFase();
            linea[4] = multa.getTipoJuridico();
            linea[5] = formatPlazo(multa.getPlazo());
            linea[6] = Integer.toString(multa.getId());
            linea[7] = "ND";
            linea[8] = splitCodigoSancion(multa.getCodigoSancion());
            linea[9] = Integer.toString(1);
            linea[10] = multa.getExpediente();
            linea[11] = Dates.imprimeFecha(multa.getFechaMulta(), "ddMMyy");
            linea[12] = multa.getArticulo();
            linea[13] = multa.getSancionado();
            linea[14] = multa.getMatricula();
            linea[15] = multa.getNif();
            linea[16] = multa.getOrganismo();
            linea[17] = multa.getCuantia();
            linea[18] = multa.getPuntos();
            linea[19] = " ";
            linea[20] = multa.getReqObs();
            linea[21] = multa.getLocalidad();
            linea[22] = pr.getCodigo().replace("BOE-N-20", "").replace("-", "");
            linea[23] = multa.getLinea();
            linea[24] = pr.getLink();
            linea[25] = multa.getLocalidad();

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

    private String formatPlazo(String plazo) {
        switch (plazo) {
            case "10D":
                return "10";
            case "15D":
                return "15";
            case "20D":
                return "20";
            case "1M":
                return "30";
            case "2M":
                return "60";
            default:
                return null;
        }
    }

    private String splitCodigoSancion(String codigo) {
        return codigo.replace("-", "");
    }

    private String getDataArchivos(int tipo) {
        StringBuilder sb;

        switch (tipo) {
            case BB0:
                sb = new StringBuilder();

                for (int i = 0; i < data.size(); i++) {
                    String[] arr = data.get(i);
                    sb.append(getLinea(arr, BB0));

                    if (i != data.size() - 1) {
                        sb.append(System.lineSeparator());
                    }
                }
                break;

            case BB1:
                sb = new StringBuilder();

                for (int i = 0; i < data.size(); i++) {
                    String[] arr = data.get(i);
                    sb.append(getLinea(arr, BB1));

                    if (i != data.size() - 1) {
                        sb.append(System.lineSeparator());
                    }
                }
                break;

            default:
                throw new IllegalArgumentException();
        }
        return sb.toString();
    }

    private void crearArchivos() {
        File archivoBB0 = new File(fichero, fecha.format(DateTimeFormatter.ISO_DATE) + ".bb0");
        LoadFile.writeFile(archivoBB0, getDataArchivos(BB0));
    }
}
