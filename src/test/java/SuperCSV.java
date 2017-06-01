import dominio.Usuario;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by auxi on 1/06/17.
 */
public class SuperCSV {
    public static void main(String[] args) throws IOException {
        try {

            // Simulación de lo que responde plataforma frente a la petición de search
            // De la clase AbstractHTTPMethod, método readResponse recuperamos el csv con el inputStream
//            FileInputStream in = new FileInputStream("/home/auxi/Documentos/usuarios.csv");
            FileInputStream in = new FileInputStream("/home/auxi/Documentos/develop/csv/src/main/resources/usuarios.csv");
            InputStreamReader input = new InputStreamReader(in);

            ICsvMapReader mapReaderRespuesta = new CsvMapReader(input, (new CsvPreference.Builder('"', 59, "\r\n")).build());

//            ICsvMapReader mapReaderRespuesta = new CsvMapReader(input, CsvPreference.STANDARD_PREFERENCE);

            // the header columns are used as the keys to the Map
            final String[] header = mapReaderRespuesta.getHeader(true);

            List<Map<String, String>> listmapRespuesta = new LinkedList<Map<String, String>>();

            Map<String, String> customerMapRespuesta;
            System.out.println("*****************************************************************************************");
            System.out.println("USUARIOS DE LA RESPUESTA A LA PETICIÓN");
            while( (customerMapRespuesta = mapReaderRespuesta.read(header)) != null ) {
                System.out.println(String.format("lineNo=%s, rowNo=%s, customerMap=%s", mapReaderRespuesta.getLineNumber(),
                        mapReaderRespuesta.getRowNumber(), customerMapRespuesta));
                listmapRespuesta.add(customerMapRespuesta);
            }
            System.out.println("*****************************************************************************************");

//            System.out.println("Lista de respuesta: " +listmapRespuesta.toString());

//            System.out.println("*****************************************************************************************");
//            System.out.println("USUARIOS DE LA RESPUESTA A LA PETICIÓN");
//            for (Map <String, String> e: listmap) {
//                Iterator it = e.entrySet().iterator();
//
//                while (it.hasNext()) {
//                    Map.Entry ef = (Map.Entry)it.next();
//                    System.out.println("["+ef.getKey() + "=" + ef.getValue()+ "]");
//                }
//            }
//            System.out.println("*****************************************************************************************");


            // Simulación de lo esperado en el escenario para comprobar que está contenido en la respuesta anterior


            ICsvMapReader mapReaderEsperado = new CsvMapReader(new FileReader("/home/auxi/Documentos/develop/csv/src/main/resources/usuarios_import.csv"), (new CsvPreference.Builder('"', 59, "\r\n")).build());
//            ICsvMapReader mapReaderEsperado = new CsvMapReader(new FileReader("/home/auxi/Documentos/usuarios_import.csv"), CsvPreference.STANDARD_PREFERENCE);

            final String[] headerEsperada = mapReaderEsperado.getHeader(true);

            List<Map<String, String>> listmapEsperada = new LinkedList<Map<String, String>>();

            Map<String, String> customerMapEsperado;
            System.out.println("*****************************************************************************************");
            System.out.println("USUARIOS QUE ESPERAMOS");
            while( (customerMapEsperado = mapReaderEsperado.read(headerEsperada)) != null ) {
                System.out.println(String.format("lineNo=%s, rowNo=%s, customerMap=%s", mapReaderEsperado.getLineNumber(),
                        mapReaderEsperado.getRowNumber(), customerMapEsperado));
                listmapEsperada.add(customerMapEsperado);

            }
            System.out.println("*****************************************************************************************");
//            System.out.println("Lista esperada: " +listmapEsperada.toString());

            if (listmapEsperada.size() > listmapRespuesta.size()) {
                System.out.println("ERROR .- Hay más usuarios esperados que recibidos");
            } else {
                for (Map<String, String> mapEsperado: listmapEsperada){
                    if (listmapRespuesta.contains(mapEsperado)) {
                        System.out.println("El usuario esperado " + mapEsperado.get("Nombres") + " SI está contenido en la respuesta");
                    } else {
                        System.out.println("El usuario esperado " + mapEsperado.get("Nombres") + " NO está contenido en la respuesta");
                    }
                }
            }

        }catch (EOFException e) {

        }
    }
}
