import Utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.*;

/**
 * Created by auxi on 1/06/17.
 */
public class DatapointsSuperCSV {

    private static CellProcessor[] getProcessors() {
//        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+";
//        StrRegEx.registerMessage(emailRegex, "must be a valid email address");

        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), // Device
                new NotNull(), // datastream
                new NotNull(), // feed
                new NotNull(), // from
                new NotNull(), // at
                new NotNull() // value
        };
        return processors;
    }

    public static void main(String[] args) throws IOException {
        try {

            // Simulación de lo que responde plataforma frente a la petición de search
            // De la clase AbstractHTTPMethod, método readResponse recuperamos el csv con el inputStream
            FileInputStream in = new FileInputStream("/home/auxi/Documentos/develop/csv/src/main/resources/datapoints_response.csv");
            InputStreamReader input = new InputStreamReader(in);

            ICsvMapReader mapReaderRespuesta = new CsvMapReader(input, (new CsvPreference.Builder('\'', 59, "\r\n")).build());
            // the header columns are used as the keys to the Map
            final String[] header = mapReaderRespuesta.getHeader(true);

//            List<Map<String, String>> listmapRespuesta = new LinkedList<Map<String, String>>();
            List<Map<String, Object>> listmapRespuestaObject = new LinkedList<Map<String, Object>>();

//            Map<String, String> customerMapRespuesta;
            Map<String, Object> customerMapRespuestaConJson = new HashMap<String, Object>();
            System.out.println("*****************************************************************************************");
            System.out.println("DATAPOINTS DE LA RESPUESTA A LA PETICIÓN");
//            while( (customerMapRespuesta = mapReaderRespuesta.read(header)) != null ) {
            while( (customerMapRespuestaConJson = mapReaderRespuesta.read(header, getProcessors())) != null ) {
                if (JsonUtils.convertStringToJSONObject((String)customerMapRespuestaConJson.get("value")) != null) {
                    System.out.println("El valor " + customerMapRespuestaConJson.get("value") + " es un json");
                    Iterator it = customerMapRespuestaConJson.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry)it.next();
                        if (e.getKey().equals("value")) {
                            customerMapRespuestaConJson.put((String) e.getKey(), JsonUtils.convertStringToJSONObject((String)customerMapRespuestaConJson.get("value")));
                        }
                    }
                }
                System.out.println(customerMapRespuestaConJson);
                listmapRespuestaObject.add(customerMapRespuestaConJson);
            }
            System.out.println("*****************************************************************************************");


            // Simulación de lo esperado en el escenario para comprobar que está contenido en la respuesta anterior

            ICsvMapReader mapReaderEsperado = new CsvMapReader(new FileReader("/home/auxi/Documentos/develop/csv/src/main/resources/datapoints_expected.csv"), (new CsvPreference.Builder('\'', 59, "\r\n")).build());
            final String[] headerEsperada = mapReaderEsperado.getHeader(true);

//            List<Map<String, String>> listmapEsperada = new LinkedList<Map<String, String>>();
            List<Map<String, Object>> listmapEsperadaObject = new LinkedList<Map<String, Object>>();

//            Map<String, String> customerMapEsperado;
            Map<String, Object> customerMapEsperadoObject;
            System.out.println("*****************************************************************************************");
            System.out.println("DATAPOINTS QUE ESPERAMOS");
//            while( (customerMapEsperado = mapReaderEsperado.read(headerEsperada)) != null ) {
            while( (customerMapEsperadoObject = mapReaderEsperado.read(headerEsperada, getProcessors())) != null ) {
                if (JsonUtils.convertStringToJSONObject((String)customerMapEsperadoObject.get("value")) != null) {
                    System.out.println("El valor " + customerMapEsperadoObject.get("value") + " es un json");
                    Iterator it = customerMapEsperadoObject.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry)it.next();
                        if (e.getKey().equals("value")) {
                            customerMapEsperadoObject.put((String) e.getKey(), JsonUtils.convertStringToJSONObject((String)customerMapEsperadoObject.get("value")));
                        }
                    }
                }
                System.out.println(customerMapEsperadoObject);
                listmapEsperadaObject.add(customerMapEsperadoObject);

            }
            System.out.println("*****************************************************************************************");

            if (listmapEsperadaObject.size() > listmapRespuestaObject.size()) {
                System.out.println("ERROR .- Hay más datapoints esperados que recibidos");
            } else {
                for (Map<String, Object> mapEsperado: listmapEsperadaObject){
                    if (mapEsperado.get("value").getClass() == JSONObject.class) {
                        System.out.println("El datapoint esperado contiene un JSON");
                        for (Map<String, Object> mapRespuesta: listmapRespuestaObject){
                            Set<String> claves = mapEsperado.keySet();
                            for (String c : claves) {
                                if (!c.equals("value") && mapEsperado.get(c).equals(mapRespuesta.get(c))){
                                    //Sin no coinciden los valores pasar al siguiente elemento de mapRespuesta
                                    //Hay que controlarlo fuera, porque si no coinciden mapEsperado.get(c).equals(mapRespuesta.get(c)
                                    //no vamos a entrar en el if
                                    System.out.println("Son iguales las claves " + c + " para el esperado " + mapEsperado.get(c) + " y el de respuesta " + mapRespuesta.get(c));
                                } else if (c.equals("value")) {
                                    System.out.println("Comparamos JSON");
                                }
                            }
                        }
                    } else {
                        if (listmapRespuestaObject.contains(mapEsperado)) {
                            System.out.println("El datapoint esperado " + mapEsperado + " SI está contenido en la respuesta");
                        } else {
                            System.out.println("El datapoint esperado " + mapEsperado + " NO está contenido en la respuesta");
                        }
                    }
                }
            }

        }catch (EOFException e) {

        }
    }
}
