/*
 * The MIT License
 *
 * Copyright 2016 agarimo.
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
package datamer.ctrl.boes.ext;

import datamer.ctrl.boes.Query;
import datamer.model.boes.ModeloProcesar;
import datamer.model.boes.enty.Multa;
import datamer.model.boes.enty.Procesar;
import datamer.model.boes.enty.VistaExtraccion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tools.CalculaNif;
import tools.Dates;

/**
 *
 * @author agarimo
 */
public class ManualStruc {

    ModeloProcesar boletin;
    Procesar pr;
    VistaExtraccion ve;
    String texto;

    private int expediente;
    private int sancionado;
    private int nif;
    private int localidad;
    private int fecha;
    private int matricula;
    private int cuantia;
    private int articulo;
    private int precepto;
    private int puntos;
    private int reqObs;

    private int contador;
    private int lineas;
    private int columnas;
    private List<String[]> lista;
    private List<String> model;

    public ManualStruc(ModeloProcesar boletin, String data) {
        this.boletin = boletin;
        ve = Query.getVistaExtraccion(VistaExtraccion.SQLBuscar(boletin.getCodigo()));
        pr = Query.getProcesar(boletin.getCodigo());
        texto = Query.getTextData(pr.getCodigo());
        contador = 1;
        this.expediente = -1;
        this.sancionado = -1;
        this.nif = -1;
        this.localidad = -1;
        this.fecha = -1;
        this.matricula = -1;
        this.cuantia = -1;
        this.articulo = -1;
        this.precepto = -1;
        this.puntos = -1;
        this.reqObs = -1;
        splitData(data);
    }

    private void splitData(String data) {
        lista = new ArrayList();
        model = new ArrayList();
        String[] split = data.split("\n");
        String[] splitModel = split[0].split("\\|");

        for (String aux : split) {
            lista.add(aux.split("\\|"));
        }

        for (int i = 0; i < splitModel.length; i++) {
            model.add("[" + (i + 1) + "] " + splitModel[i]);
        }

        lineas = lista.size();
        columnas = model.size();
    }

    public List<Multa> getMultas() {

        List<Multa> multas = new ArrayList();
        Multa multa;
        String art, prec;

        for (String[] aux : lista) {

            multa = new Multa();

            multa.setIdBoletin(pr.getId());
            multa.setCodigoSancion(getCodigoMulta());
            multa.setFechaPublicacion(ve.getFecha());
            multa.setIdOrganismo(ve.getIdOrigen());
            multa.setOrganismo(ve.getOrigen());
            multa.setBoe(ve.getBoe());
            multa.setFase(ve.getFase());
            multa.setPlazo(ve.getPlazo().getValue());

            if (expediente != -1) {
                multa.setExpediente(aux[expediente]);
            } else {
                multa.setExpediente("");
            }

            if (sancionado != -1) {
                multa.setSancionado(aux[sancionado]);
            } else {
                multa.setSancionado("");
            }

            if (nif != -1) {
                multa.setNif(aux[nif]);
                multa.setTipoJuridico(setTipoJuridico(multa.getNif()));
            } else {
                multa.setNif("");
            }

            if (localidad != -1) {
                multa.setLocalidad(aux[localidad]);
            } else {
                multa.setLocalidad("");
            }

            if (fecha != -1) {
                multa.setFechaMulta(parseFecha(aux[fecha]));
            }

            if (matricula != -1) {
                multa.setMatricula(aux[matricula]);
            } else {
                multa.setMatricula("");
            }

            if (cuantia != -1) {
                multa.setCuantia(aux[cuantia]);
            } else {
                multa.setCuantia("");
            }

            if (articulo != -1) {
                art = aux[articulo];
            } else {
                art = "";
            }

            if (precepto != -1) {
                prec = aux[precepto];
            } else {
                prec = "";
            }

            multa.setArticulo((art.trim() + " " + prec.trim()).toUpperCase().trim());

            if (puntos != -1) {
                multa.setPuntos(aux[puntos]);
            } else {
                multa.setPuntos("");
            }

            if (reqObs != -1) {
                multa.setReqObs(aux[reqObs]);
            } else {
                multa.setReqObs("");
            }

            multa.setLinea(getLinea(aux));

            multas.add(multa);
        }

        contador = 1;
        return multas;
    }
    
    public Procesar getProcesar(){
        return this.pr;
    }
    
    public ModeloProcesar getModeloProcesar(){
        return this.boletin;
    }

    private Date parseFecha(String fecha) {
        LocalDate aux;
        Date date;

        try {
            aux = LocalDate.parse(fecha);
            date = Dates.asDate(aux);
        } catch (Exception e) {
            date = null;
        }

        return date;
    }

    private String setTipoJuridico(String nif) {
        CalculaNif cn = new CalculaNif();
        return cn.getTipoJuridico(nif);
    }

    private String getCodigoMulta() {
        String aux = this.ve.getCodigo().replace("BOE-N-20", "");
        aux = aux + "/" + Integer.toString(contador);
        contador++;

        return aux;
    }

    private String getLinea(String[] linea) {

        StringBuilder sb = new StringBuilder();

        for (String aux : linea) {
            sb.append(aux);
            sb.append(" ");
        }
        return clean(sb.toString());
    }

    private String clean(String str) {
        String aux = str.trim();
        aux = aux.replace("|", "");

        return aux;
    }

    public void setData(String data) {
        contador=1;
        splitData(data);
    }

    public int getLineas() {
        return this.lineas;
    }

    public int getColumnas() {
        return this.columnas;
    }

    public List getModel() {
        return this.model;
    }

    public int getExpediente() {
        return expediente;
    }

    public void setExpediente(int expediente) {
        this.expediente = expediente;
    }

    public int getSancionado() {
        return sancionado;
    }

    public void setSancionado(int sancionado) {
        this.sancionado = sancionado;
    }

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public int getLocalidad() {
        return localidad;
    }

    public void setLocalidad(int localidad) {
        this.localidad = localidad;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getCuantia() {
        return cuantia;
    }

    public void setCuantia(int cuantia) {
        this.cuantia = cuantia;
    }

    public int getArticulo() {
        return articulo;
    }

    public void setArticulo(int articulo) {
        this.articulo = articulo;
    }

    public int getPrecepto() {
        return precepto;
    }

    public void setPrecepto(int precepto) {
        this.precepto = precepto;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getReqObs() {
        return reqObs;
    }

    public void setReqObs(int reqObs) {
        this.reqObs = reqObs;
    }
    
    public String getTexto(){
        return this.texto;
    }
}
