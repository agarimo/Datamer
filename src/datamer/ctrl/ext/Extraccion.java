package datamer.ctrl.ext;

import enty.Multa;
import enty.Procesar;
import enty.StrucData;
import enty.VistaExtraccion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import main.SqlBoe;
import main.Var;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class Extraccion {

    Date fecha;
    List<Procesar> lista;
    File fichero;

    public Extraccion(Date fecha) {
        this.fecha = fecha;
        this.lista = cargaBoletines();
        this.fichero = new File(Var.ficheroEx, Dates.imprimeFecha(fecha));
    }

    public boolean fileExist(String codigo) {
        File file = new File(this.fichero, codigo + ".xlsx");
        return file.exists();
    }

    private List<Procesar> cargaBoletines() {
        return SqlBoe.listaProcesar("SELECT * FROM " + Var.nombreBD + ".procesar "
                + "where fecha=" + Varios.entrecomillar(Dates.imprimeFecha(fecha)) + " "
                + "and estado=1");
    }

    public List<Procesar> getBoletines() {
        return this.lista;
    }

    public List<Multa> previewXLSX(Procesar aux) {
        File file = new File(this.fichero, aux.getCodigo() + ".xlsx");
        VistaExtraccion ve = SqlBoe.getVistaExtraccion(VistaExtraccion.SQLBuscar(aux.getCodigo()));
        StrucData sd = SqlBoe.getStrucData(StrucData.SQLBuscar(aux.getEstructura()));
        XLSXProcess process = new XLSXProcess(getRows(file), aux, ve, sd);

        return process.splitXLSX();
    }

    private List<Row> getRows(File archivo) {
        XSSFWorkbook excel = (getXLSX(archivo));

        if (excel != null) {
            List<XSSFSheet> paginas;
            List<Row> filas = new ArrayList();
            XSSFSheet pagina;

            paginas = getPaginas(excel);
            Iterator<XSSFSheet> ite = paginas.iterator();

            while (ite.hasNext()) {
                pagina = ite.next();
                filas.addAll(getFilas(pagina));
            }
            return filas;
        } else {
            return null;
        }
    }

    private XSSFWorkbook getXLSX(File archivo) {
        XSSFWorkbook workbook;
        FileInputStream file;

        try {
            file = new FileInputStream(archivo);
            workbook = new XSSFWorkbook(file);
            return workbook;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    private List<XSSFSheet> getPaginas(XSSFWorkbook excel) {
        List<XSSFSheet> list = new ArrayList();
        XSSFSheet pagina;

        Iterator<XSSFSheet> sheetIterator = excel.iterator();

        while (sheetIterator.hasNext()) {
            pagina = sheetIterator.next();
            list.add(pagina);
        }
        return list;
    }

    private List<Row> getFilas(XSSFSheet pagina) {
        List<Row> aux = new ArrayList();
        Row row;
        Iterator<Row> it = pagina.iterator();

        while (it.hasNext()) {
            row = it.next();
            aux.add(row);
        }

        return aux;
    }

}
