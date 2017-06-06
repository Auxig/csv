import Utils.JsonUtils;
import com.csvreader.CsvReader;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by auxi on 5/06/17.
 */
public class TestNuevo {
    public static void main(String[] args) throws JSONException {

        try {

            // De la clase AbstractHTTPMethod, método readResponse recuperamos el csv con el inputStream
            FileInputStream in = new FileInputStream("/home/auxi/develop/csv/src/main/resources/datapoints_response.csv");
            InputStreamReader input = new InputStreamReader(in);

            CsvReader csvRespuesta = new CsvReader(input);

            csvRespuesta.setDelimiter(';');
            csvRespuesta.setTextQualifier('\'');
            csvRespuesta.readHeaders();

            Map<String, String> mapRespuesta = new HashMap<>();

// Construimos con el CSV leido un map. Si el value del csv es un json o un array debemos tratar la comparación de forma diferente, ya que si el orden de estos elementos no es igual en el esperado
// que en el recibido la comparación fallará (dirá que no son iguales). Para ello separamos como clave del map todos los elementos del csv excepto el campo value, que será el value del map.
            while (csvRespuesta.readRecord()) {
                String device = csvRespuesta.get("device");
                String datastream = csvRespuesta.get("datastream");
                String feed = csvRespuesta.get("feed");
                String from = csvRespuesta.get("from");
                String at = csvRespuesta.get("at");
                String clave = new StringBuilder().append(device).append(";").append(datastream).append(";").append(feed).append(";").append(from).append(";").append(at).toString();

                String value = csvRespuesta.get("value");

                mapRespuesta.put(clave,value);

            }

            csvRespuesta.close();

            System.out.println("*****************************************************************************************");
            System.out.println("DATAPOINTS DE LA RESPUESTA A LA PETICIÓN");
            for (Map.Entry<String, String> e: mapRespuesta.entrySet()) {
                System.out.println("["+e.getKey() + "=" + e.getValue()+"]");
            }
            System.out.println("*****************************************************************************************");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            CsvReader csvEsperado = new CsvReader("/home/auxi/develop/csv/src/main/resources/datapoints_expected.csv");

            csvEsperado.setDelimiter(';');
            csvEsperado.setTextQualifier('\'');
            csvEsperado.readHeaders();

            Map<String, String> mapEsperado = new HashMap<>();

            while (csvEsperado.readRecord())
            {
                String device = csvEsperado.get("device");
                String datastream = csvEsperado.get("datastream");
                String feed = csvEsperado.get("feed");
                String from = csvEsperado.get("from");
                String at = csvEsperado.get("at");
                String clave = new StringBuilder().append(device).append(";").append(datastream).append(";").append(feed).append(";").append(from).append(";").append(at).toString();

                String value = csvEsperado.get("value");

                mapEsperado.put(clave,value);

            }

            csvEsperado.close();

            System.out.println("*****************************************************************************************");
            System.out.println("DATAPOINTS ESPERADOS");
            for (Map.Entry<String, String> e: mapEsperado.entrySet()) {
                System.out.println("["+e.getKey() + "=" + e.getValue()+"]");
            }
            System.out.println("*****************************************************************************************");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //Si esperamos más elementos de los que se devuelven no seguimos comparando
            if (mapEsperado.size() > mapRespuesta.size()) {
                System.out.println("ERROR .- Hay más datapoints esperados que recibidos");
                throw new AssertionError("ERROR .- Hay más datapoints esperados que recibidos");
            } else {
                //Recorremos las claves de los datapoints esperados
                for (String esperadoKey: mapEsperado.keySet()){
                    System.out.println("********************************************************************");
                    System.out.println("Buscamos la key esperada " + esperadoKey + " en la respuesta");
                    String respuestaValues = mapRespuesta.get(esperadoKey);
                    String esperadoValues = mapEsperado.get(esperadoKey);
                    //Si la clave que estamos tratando tiene valores en la respuesta quiere decir que existe en la respuesta un datapoint que de momento coincide con lo que esperamos
                    //Decimos de momento porque ahora nos queda por comparar el campo value del csv, que trataremos de forma diferente dependiendo del tipo que sea
                    if (respuestaValues != null) {
                        System.out.println("¡¡¡ENCONTRADA!!!!");

                        //Si el value del csv es de tipo json lo tratamos aqui
                        if (JsonUtils.convertStringToJSONObject(respuestaValues) != null) {
                            System.out.println("*****************************************************************************************");
                            System.out.println("\tEl datapoint esperado contiene un JSON");
                            System.out.println("\tEl datapoint esperado " + esperadoKey + "=" + esperadoValues + " DE MOMENTO SI está contenido en la respuesta, hay que mirar el value JSON");
//                            System.out.println("//////////////////////////////////////////////////////////////////////////");
                            System.out.println("\tEl valor " + respuestaValues + " es un JSON");
                            System.out.println("\tComparamos JSON");
                            JSONObject jsonObjectEsperado = JsonUtils.convertStringToJSONObject(esperadoValues);
                            JSONObject jsonObjectRespuesta = JsonUtils.convertStringToJSONObject(respuestaValues);
                            try {
                                JSONAssert.assertEquals(jsonObjectEsperado, jsonObjectRespuesta, JSONCompareMode.LENIENT);
                            } catch (AssertionError e) {
                                System.out.println("\tEl value esperado " + esperadoValues + " \u001B[1m NO está contenido en la respuesta \033[0m");
                                throw new AssertionError("El value esperado " + esperadoValues + " \u001B[1m NO está contenido en la respuesta \033[0m");
                            }
                            System.out.println("\tEl value esperado " + esperadoValues + "\u001B[1m SI está contenido en la respuesta \033[0m" + respuestaValues);
//                            System.out.println("//////////////////////////////////////////////////////////////////////////");

                        }
                        //Si el value del csv es de tipo array lo tratamos aqui
                        else if (JsonUtils.convertStringToJSONArray(respuestaValues) != null) {
                            System.out.println("*****************************************************************************************");
                            System.out.println("\tEl datapoint esperado contiene un ARRAY");
                            System.out.println("\tEl datapoint esperado " +esperadoKey + "=" + esperadoValues + " DE MOMENTO SI está contenido en la respuesta, hay que mirar el value ARRAY");
//                            System.out.println("//////////////////////////////////////////////////////////////////////////");
                            System.out.println("\tEl valor " + respuestaValues + " es un array");
                            System.out.println("\tComparamos JSON ARRAY");

                            JSONArray arrayObjectEsperado = JsonUtils.convertStringToJSONArray(esperadoValues);
                            JSONArray arrayObjectRespuesta = JsonUtils.convertStringToJSONArray(respuestaValues);

                            try {
                                JSONAssert.assertEquals(arrayObjectEsperado, arrayObjectRespuesta, JSONCompareMode.LENIENT);
                            } catch (AssertionError e) {
                                System.out.println("\tEl value esperado " + esperadoValues + "\u001B[1m NO está contenido en la respuesta \033[0m");
                                throw new AssertionError("\tEl value esperado " + esperadoValues + " \u001B[1m NO está contenido en la respuesta \033[0m");
                            }
                            System.out.println("\tEl value esperado " + esperadoValues + " \u001B[1m SI está contenido en la respuesta \033[0m " + respuestaValues);
//                            System.out.println("//////////////////////////////////////////////////////////////////////////");

                        }
                        //Si el value del csv no es json ni array lo tratamos aqui
                        else {
                            System.out.println("*****************************************************************************************");
                            System.out.println("\tEl datapoint esperado NO contiene un ARRAY ni un JSON");
                            System.out.println("\tEl datapoint esperado " +esperadoKey + "=" + esperadoValues + " DE MOMENTO SI está contenido en la respuesta, hay que mirar el value");
//                            System.out.println("//////////////////////////////////////////////////////////////////////////");
                            System.out.println("\tEl valor " + respuestaValues + " no es del tipo JSON ni ARRAY");
                            if (!(respuestaValues.equals(mapEsperado.get(esperadoKey))) ) {
                                System.out.println("\tEl value esperado " + esperadoValues + " \u001B[1m NO está contenido en la respuesta \033[0m");
                                throw new AssertionError("\tEl value esperado " + esperadoValues + " \u001B[1m NO está contenido en la respuesta \033[0m");
                            } else {
                                System.out.println("\tEl value esperado " + esperadoValues + " \u001B[1m SI está contenido en la respuesta \033[0m " + respuestaValues);
                            }
//                            System.out.println("//////////////////////////////////////////////////////////////////////////");
                        }
                    }
                    //Si la clave que estamos tratando NO tiene valores en la respuesta quiere decir que NO existe en la respuesta un datapoint que coincida con lo que esperamos
                    else {
                        System.out.println("¡¡¡NO ENCONTRADA!!!!");
                        System.out.println("El datapoint " + esperadoKey + "=" + esperadoValues + " \u001B[1m NO viaja en la respuesta \033[0m");
                        throw new AssertionError("La clave esperada " + esperadoKey + " \u001B[1m NO está contenido en la respuesta \033[0m");
                    }
                }
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
