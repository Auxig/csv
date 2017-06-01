import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by auxi on 1/06/17.
 */
public class DatapointsSuperCSV {
    public static void main(String[] args) throws IOException {
        try {

            // Simulación de lo que responde plataforma frente a la petición de search
            // De la clase AbstractHTTPMethod, método readResponse recuperamos el csv con el inputStream
            FileInputStream in = new FileInputStream("/home/auxi/Documentos/develop/csv/src/main/resources/datapoints_response.csv");
            InputStreamReader input = new InputStreamReader(in);

            ICsvMapReader mapReaderRespuesta = new CsvMapReader(input, (new CsvPreference.Builder('"', 59, "\r\n")).build());
            // the header columns are used as the keys to the Map
            final String[] header = mapReaderRespuesta.getHeader(true);

            List<Map<String, String>> listmapRespuesta = new LinkedList<Map<String, String>>();

            Map<String, String> customerMapRespuesta;
            System.out.println("*****************************************************************************************");
            System.out.println("DATAPOINTS DE LA RESPUESTA A LA PETICIÓN");
            while( (customerMapRespuesta = mapReaderRespuesta.read(header)) != null ) {
                System.out.println(customerMapRespuesta);
                listmapRespuesta.add(customerMapRespuesta);
            }
            System.out.println("*****************************************************************************************");


            // Simulación de lo esperado en el escenario para comprobar que está contenido en la respuesta anterior

            ICsvMapReader mapReaderEsperado = new CsvMapReader(new FileReader("/home/auxi/Documentos/develop/csv/src/main/resources/datapoints_expected.csv"), (new CsvPreference.Builder('"', 59, "\r\n")).build());
            final String[] headerEsperada = mapReaderEsperado.getHeader(true);

            List<Map<String, String>> listmapEsperada = new LinkedList<Map<String, String>>();

            Map<String, String> customerMapEsperado;
            System.out.println("*****************************************************************************************");
            System.out.println("DATAPOINTS QUE ESPERAMOS");
            while( (customerMapEsperado = mapReaderEsperado.read(headerEsperada)) != null ) {
                System.out.println(customerMapEsperado);
                listmapEsperada.add(customerMapEsperado);

            }
            System.out.println("*****************************************************************************************");

            if (listmapEsperada.size() > listmapRespuesta.size()) {
                System.out.println("ERROR .- Hay más datapoints esperados que recibidos");
            } else {
                for (Map<String, String> mapEsperado: listmapEsperada){
                    if (listmapRespuesta.contains(mapEsperado)) {
                        System.out.println("El datapoint esperado " + mapEsperado + " SI está contenido en la respuesta");
                    } else {
                        System.out.println("El datapoint esperado " + mapEsperado + " NO está contenido en la respuesta");
                    }
                }
            }

        }catch (EOFException e) {

        }
    }
}
