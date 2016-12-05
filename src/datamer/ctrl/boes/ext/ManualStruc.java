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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author agarimo
 */
public class ManualStruc {

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

    private int lineas;
    private int columnas;
    private List<String[]> lista;
    private List<String> model;

    public ManualStruc() {
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
    }

    public ManualStruc(String data) {
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

    public void setData(String data) {
        splitData(data);
    }
    
    public int getLineas(){
        return this.lineas;
    }
    
    public int getColumnas(){
        return this.columnas;
    }
    
    public List getModel(){
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
}
