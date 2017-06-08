import Utils.JsonUtils;
import org.apache.commons.collections.map.HashedMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by auxi on 1/06/17.
 */
public class DatapointsSuperCSV {

    private static CellProcessor[] getProcessors() {
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

    public static void main(String[] args) throws IOException, URISyntaxException, JSONException {
        try {
            // Simulación de lo que responde plataforma frente a la petición de search
            // De la clase AbstractHTTPMethod, método readResponse recuperamos el csv con el inputStream
            FileInputStream in = new FileInputStream(new File(getSystemClassLoader().getResource("datapoints_response.csv").toURI()));
            InputStreamReader input = new InputStreamReader(in);
            ICsvMapReader mapReaderRespuesta = new CsvMapReader(input, (new CsvPreference.Builder('\'', 59, "\r\n")).build());
            // the header columns are used as the keys to the Map
            final String[] header = mapReaderRespuesta.getHeader(true);

            List<Map<String, Object>> listmapRespuestaObject = new LinkedList<Map<String, Object>>();
            Map<String, Object> customerMapRespuesta = new HashMap<String, Object>();
            System.out.println("*****************************************************************************************");
            System.out.println("DATAPOINTS DE LA RESPUESTA A LA PETICIÓN");
            while( (customerMapRespuesta = mapReaderRespuesta.read(header, getProcessors())) != null ) {
                String value = (String)customerMapRespuesta.get("value");
                if (JsonUtils.convertStringToJSONObject(value) != null) {
                    System.out.println("El valor " + value + " es un json");
                    Iterator it = customerMapRespuesta.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry)it.next();
                        if (e.getKey().equals("value")) {
                            customerMapRespuesta.put((String) e.getKey(), JsonUtils.convertStringToJSONObject(value));
                        }
                    }
                } else if (JsonUtils.convertStringToJSONArray(value) != null) {
                    System.out.println("El valor " + value + " es un array");
                    Iterator it = customerMapRespuesta.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry)it.next();
                        if (e.getKey().equals("value")) {
                            customerMapRespuesta.put((String) e.getKey(), JsonUtils.convertStringToJSONArray(value));
                        }
                    }
                }

                System.out.println(customerMapRespuesta);
                listmapRespuestaObject.add(customerMapRespuesta);
            }
            System.out.println("*****************************************************************************************");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//            String expected_str = "device;datastream;feed;from;at;value\n" +
//                    "device_IOT_collection;health.bmi;feed_1;2015-05-14T11:13:43Z;2015-05-14T11:13:43Z;60.0\n" +
//                    "device_IOT_collection;health.bmi;feed_1;2015-05-14T11:13:43Z;2015-05-14T11:13:43Z;80.0\n" +
//                    "device_IOT_collection;health.bmi;feed_1;2015-05-14T11:13:43Z;2015-05-14T11:13:43Z;85.0\n" +
//                    "device_IOT_collection;health.bmi;feed_1;2015-05-14T11:13:43Z;2015-05-14T11:13:43Z;75.0";
//
//            StringReader fileReader = null;
//            fileReader = new StringReader(expected_str);

            ICsvMapReader mapReaderEsperado = new CsvMapReader(new FileReader("/home/auxi/develop/csv/src/main/resources/datapoints_expected.csv"), (new CsvPreference.Builder('\'', 59, "\r\n")).build());

            // Simulación de lo esperado en el escenario para comprobar que está contenido en la respuesta anterior
//            ICsvMapReader mapReaderEsperado = new CsvMapReader(fileReader, (new CsvPreference.Builder('\'', 59, "\r\n")).build());
            String[] headerEsperada = mapReaderEsperado.getHeader(true);
            List<Map<String, Object>> listmapEsperadaObject = new LinkedList<Map<String, Object>>();
            List<Map<String, Object>> listmapEsperadaObjectConJson = new LinkedList<Map<String, Object>>();
            Map<String, Object> customerMapEsperado = new HashMap<String, Object>();
            System.out.println("*****************************************************************************************");
            System.out.println("DATAPOINTS QUE ESPERAMOS");
            while( (customerMapEsperado = mapReaderEsperado.read(headerEsperada, getProcessors())) != null ) {
                String value = (String)customerMapEsperado.get("value");
                if (JsonUtils.convertStringToJSONObject(value) != null) {
                    System.out.println("El valor " + customerMapEsperado.get("value") + " es un json");
                    Iterator it = customerMapEsperado.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry)it.next();
                        if (e.getKey().equals("value")) {
                            customerMapEsperado.put((String) e.getKey(), JsonUtils.convertStringToJSONObject(value));
                        }
                    }
                } else if (JsonUtils.convertStringToJSONArray(value) != null) {
                    System.out.println("El valor " + value + " es un array");
                    Iterator it = customerMapEsperado.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry)it.next();
                        if (e.getKey().equals("value")) {
                            customerMapEsperado.put((String) e.getKey(), JsonUtils.convertStringToJSONArray(value));
                        }
                    }
                }
                System.out.println(customerMapEsperado);
                listmapEsperadaObject.add(customerMapEsperado);

            }
            System.out.println("*****************************************************************************************");


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (listmapEsperadaObject.size() > listmapRespuestaObject.size()) {
                System.out.println("ERROR .- Hay más datapoints esperados que recibidos");
            } else {
                for (Map<String, Object> mapEsperado: listmapEsperadaObject){
                    if (mapEsperado.get("value").getClass() == JSONObject.class) {

                        System.out.println("*****************************************************************************************");
                        System.out.println("El datapoint esperado contiene un JSON");

                        Map<String, Object> mapAuxEsperado = new HashedMap();
                        mapAuxEsperado.putAll(mapEsperado);
                        mapAuxEsperado.remove("value");

                        List<Map<String, Object>> listMapRespuestaAux = new LinkedList<Map<String, Object>>();
                        Map<String, Object> mapAuxRespuesta = new HashedMap();
                        for (Map<String, Object> mapRespuesta: listmapRespuestaObject){

                            mapAuxRespuesta = new HashedMap();
                            mapAuxRespuesta.putAll(mapRespuesta);
                            mapAuxRespuesta.remove("value");

                            listMapRespuestaAux.add(mapAuxRespuesta);

                        }

                        if (listMapRespuestaAux.contains(mapAuxEsperado)) {
                            System.out.println("El datapoint esperado " + mapEsperado + " DE MOMENTO SI está contenido en la respuesta, hay que mirar el value JSON");
                            //Es igual por ahora, nos queda por mirar el value en formato json

                            boolean found = false;
                            for (int i=0; i < listMapRespuestaAux.size(); i++) {
                                if (listMapRespuestaAux.get(i).equals(mapAuxEsperado)){
                                    System.out.println("Comparamos JSON");
                                    JSONObject jsonObjectEsperado = (JSONObject) mapEsperado.get("value");
                                    if (listmapRespuestaObject.get(i).get("value") instanceof JSONObject) {
                                        JSONObject jsonObjectRespuesta = (JSONObject) listmapRespuestaObject.get(i).get("value");

                                        if (jsonObjectEsperado != null) {
                                            boolean eq = false;
                                            try {
                                                JSONAssert.assertEquals(jsonObjectEsperado, jsonObjectRespuesta, JSONCompareMode.LENIENT);
                                                eq = true;
                                            } catch (AssertionError e) {
                                                System.out.println("De momento este no esta contenido, miramos el siguiente de la respuesta");
                                                found = false;
                                            }
                                            if (eq) {
                                                System.out.println("El datapoint esperado " + mapEsperado + " SI está contenido en la respuesta");
                                                found = true;
                                                break;
                                            }

                                        }
                                    }

                                }
                            }
                            if (!found) {
                                System.out.println("The expected line value field " +mapEsperado.get("value") +" in the csv " +mapEsperado+ " isn't contained in the response");
                            }
                        } else {
                            System.out.println("El datapoint esperado " + mapEsperado + " NO está contenido en la respuesta");
                        }

                    } else if (mapEsperado.get("value").getClass() == JSONArray.class) {
                        System.out.println("*****************************************************************************************");
                        System.out.println("El datapoint esperado contiene un Array");

                        Map<String, Object> mapAuxEsperado = new HashedMap();
                        mapAuxEsperado.putAll(mapEsperado);
                        mapAuxEsperado.remove("value");

                        List<Map<String, Object>> listMapRespuestaAux = new LinkedList<Map<String, Object>>();
                        Map<String, Object> mapAuxRespuesta = new HashedMap();
                        for (Map<String, Object> mapRespuesta: listmapRespuestaObject){

                            mapAuxRespuesta = new HashedMap();
                            mapAuxRespuesta.putAll(mapRespuesta);
                            mapAuxRespuesta.remove("value");

                            listMapRespuestaAux.add(mapAuxRespuesta);
                        }

                        if (listMapRespuestaAux.contains(mapAuxEsperado)) {
                            System.out.println("El datapoint esperado " + mapEsperado + " DE MOMENTO SI está contenido en la respuesta, hay que mirar el value Array");
                            //Es igual por ahora, nos queda por mirar el value en formato array

                            boolean found = false;
                            for (int i=0; i < listMapRespuestaAux.size(); i++) {
                                if (listMapRespuestaAux.get(i).equals(mapAuxEsperado)) {
                                    System.out.println("Comparamos ARRAY");
                                    JSONArray arrayObjectEsperado = (JSONArray) mapEsperado.get("value");
                                    if (listmapRespuestaObject.get(i).get("value") instanceof JSONArray) {
                                        JSONArray arrayObjectRespuesta = (JSONArray) listmapRespuestaObject.get(i).get("value");

                                        if (arrayObjectEsperado != null) {
                                            boolean eq = false;
                                            try {
                                                JSONAssert.assertEquals(arrayObjectEsperado, arrayObjectRespuesta, JSONCompareMode.LENIENT);
                                                eq = true;
                                            } catch (AssertionError e) {
                                                System.out.println("De momento este no esta contenido, miramos el siguiente de la respuesta");
                                                found = false;
                                            }
                                            if (eq) {
                                                System.out.println("El datapoint esperado " + mapEsperado + " SI está contenido en la respuesta");
                                                found = true;
                                                break;
                                            }
                                        }
                                    }

                                }
                            }
                            if (!found) {
                                System.out.println("No se ha encontrado ninguna línea");
                            }

                        }  else {
                            System.out.println("El datapoint esperado " + mapEsperado + " NO está contenido en la respuesta");
                        }

                    } else {
                        System.out.println("*****************************************************************************************");
                        System.out.println("El datapoint esperado NO contiene un Array ni un JSON");
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

//        Object[] obj1 = {1,2,3,4};
//        Object[] obj2 = {4,2,1,3};
//        Object[] obj3 = new Object[3];
//        obj3[0] = 5;
//        obj3[1] = "text1";
//        obj3[2] = "{\"field2\":12,\"field1\":\"text\"}";
//        Object[] obj4 = new Object[3];
//        obj4[0] = 5;
//        obj4[1] = "text1";
//        obj4[2] = "{\"field1\":\"text\",\"field2\":12}";
//        System.out.println("SON IGUALES? " + compareArrays1(obj3, obj4));
//
//
//
//        //CONVERTIR STRING ARRAY EN ARRAY!!!!
//        String arr = "[1,2]";
//        String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
//
//        int[] results = new int[items.length];
//
//        for (int i = 0; i < items.length; i++) {
//            try {
//                results[i] = Integer.parseInt(items[i]);
//            } catch (NumberFormatException nfe) {
//                //NOTE: write something here if you need to recover from formatting errors
//            };
//        }
//        //CONVERTIR STRING ARRAY EN ARRAY!!!!


    }

//    public static Object[] stringToArray(String array) {
//        Object[] items = array.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
//
//        Object[] results = new Object[items.length];
//
//        for (int i = 0; i < items.length; i++) {
//            try {
//                results[i] =
////                results[i] = Integer.parseInt(items[i]);
//            } catch (NumberFormatException nfe) {
//                //NOTE: write something here if you need to recover from formatting errors
//            };
//        }
//    }



    public static boolean compareArrays1(Object[] arr1, Object[] arr2) throws JSONException {
        String[] array1 = new String[arr1.length];
        String[] array2 = new String[arr2.length];
        JSONObject jsonObjectEsperado = null;
        JSONObject jsonObjectRespuesta = null;
        boolean compareJson=false;
        boolean resultCompareJson=false;

        for (int i = 0; i < arr1.length; i++) {
            String elem="";
            if (arr1[i] instanceof Integer) {
                elem = String.valueOf(arr1[i]);
            } else if (arr1[i] instanceof Boolean) {
                elem = String.valueOf(arr1[i]);
            } else if (JsonUtils.convertStringToJSONObject((String)arr1[i]) != null) {
                System.out.println("Comparamos JSON dentro del array");
                jsonObjectEsperado = JsonUtils.convertStringToJSONObject((String)arr1[i]);
                compareJson = true;
            } else {
                elem = (String)arr1[i];
            }

            array1[i]=elem;
        }
        for (int i = 0; i < arr2.length; i++) {
            String elem="";
            if (arr2[i] instanceof Integer) {
                elem = String.valueOf(arr2[i]);
            } else if (arr2[i] instanceof Boolean) {
                elem = String.valueOf(arr2[i]);
            } else if (JsonUtils.convertStringToJSONObject((String)arr2[i]) != null) {
                System.out.println("Comparamos JSON dentro del array");
                jsonObjectRespuesta = JsonUtils.convertStringToJSONObject((String)arr2[i]);
                compareJson = true;
            } else {
                elem = (String)arr2[i];
            }
            array2[i]=elem;
        }

        if (compareJson) {
            try {
                JSONAssert.assertEquals(jsonObjectEsperado, jsonObjectRespuesta, JSONCompareMode.LENIENT);
            } catch (AssertionError e) {
                System.out.println("El datapoint esperado " + jsonObjectEsperado + " NO está contenido en la respuesta");
                throw new AssertionError("El datapoint esperado " + jsonObjectEsperado + " NO está contenido en la respuesta");
            }
            resultCompareJson=true;
            System.out.println("El datapoint esperado " + jsonObjectEsperado + " SI está contenido en la respuesta");
        }

        Arrays.sort(array1);
        Arrays.sort(array2);
        return ((Arrays.equals(array1, array2) && resultCompareJson));
    }

}
