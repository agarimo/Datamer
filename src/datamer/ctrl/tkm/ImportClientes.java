/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamer.ctrl.tkm;

import datamer.model.tkm.Estado;
import datamer.model.tkm.enty.Cliente;
import datamer.model.tkm.enty.Comentario;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import util.CalculaNif;
import util.LoadFile;

/**
 *
 * @author Agarimo
 */
public class ImportClientes {


    public ImportClientes() {
    }

    public void loadFile() {
        int contador=1;
        File aux = new File("Exportar.txt");
        String str;
        LoadFile lf = new LoadFile(aux);

        Iterator<String> it = lf.getLineas().iterator();

        while (it.hasNext()) {
            System.out.println(contador+" de "+lf.getCount());
            str = it.next();
            split(str.replace("\"", "").replace(";", "; "));
            contador++;
        }
    }

    private void split(String aux) {
        Cliente cliente = new Cliente();
        String[] split = aux.split(";");

        String cif = validaCif(split[0].trim());
        String nombre = split[1].trim().replace("'", "\\'");
        String telefono = split[2].trim();
        int estado = validaEstado(split[3].trim());
        String contacto = split[4].replace("'", "\\'");

        cliente.setCif(cif);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setEstado(estado);
        cliente.setContacto(contacto);
        
        int idCliente=cliente.SQLGuardar();
        

        if (split.length == 6) {
            splitComentario(split[5].replace("'", "\\'"), idCliente);
        }
    }

    private String validaCif(String cif) {
        CalculaNif cn = new CalculaNif();
        
        if (cif.length() == 9) {
            return cif;
        } else {
            return cn.calcular(cif);
        }
    }

    private int validaEstado(String estado) {
        if(estado.contains("01.")){
            return Estado.CONTRATO.getValue();
        }else if(estado.contains("02.")){
            return Estado.INTERESADO.getValue();
        }else if(estado.contains("03.")){
            return Estado.NO_INTERESADO.getValue();
        }else if(estado.contains("04.")){
            return Estado.ENVIADA_INFO.getValue();
        }else if(estado.contains("05.")){
            return Estado.VOLVER_LLAMAR.getValue();
        }else if(estado.contains("06.")){
            return Estado.DEJADO_MSG.getValue();
        }else if(estado.contains("07.")){
            return Estado.SOCIO_OTRA_DELEGACION.getValue();
        }else if(estado.contains("08.")){
            return Estado.NO_CONTESTA.getValue();
        }else if(estado.contains("09.")){
            return Estado.NO_LLAMAR.getValue();
        }else if(estado.contains("10.")){
            return Estado.EMPRESA_CERRADA.getValue();
        }else if(estado.contains("11.")){
            return Estado.OTRA_CIA.getValue();
        }else if(estado.contains("12.")){
            return Estado.BAJA_CONTRATO.getValue();
        }else if(estado.contains("13.")){
            return Estado.SIN_ASIGNAR.getValue();
        }else if(estado.contains("14.")){
            return Estado.SIN_ASIGNAR.getValue();
        }else if(estado.contains("15.")){
            return Estado.TELF_ERROR.getValue();
        }else if(estado.contains("16.")){
            return Estado.INFORMADO.getValue();
        }else if(estado.contains("17.")){
            return Estado.TELF_NO_LOCALIZADO.getValue();
        }else{
            return Estado.SIN_ASIGNAR.getValue();
        }
    }

    private void splitComentario(String aux, int idCliente) {
        List<String> list = new ArrayList();
        String[] split = aux.split("\\|");

        for (String split1 : split) {
            String string = split1.trim();
            if (string.length() > 0) {
                list.add(string);
            }
        }
        
        if(!list.isEmpty()){
            StringBuilder sb = new StringBuilder();
            Iterator it = list.iterator();
            
            while(it.hasNext()){
                sb.append(it.next());
                sb.append(System.lineSeparator());
            }
            
            Comentario com = new Comentario();
            com.setIdCliente(idCliente);
            com.setComentario(sb.toString());
            com.SQLGuardar();
        }
    }
}
