package Utils;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Created by auxi on 1/06/17.
 */
public class JsonUtils {

    public static JSONObject convertStringToJSONObject(String json_str) {
        try {
            JSONObject json = new JSONObject(json_str);
            // El if siguiente es para controlar la construcción de un json malformado en el json esperado, por ejemplo ---{}, "operations":[{"result": "SUCCESSFUL",...}]}---, del que sobraría "},"
            // El parseo de string a JSONObject que se hace en la línea anterior convierte el ejemplo anterior en un JSONObject con el siguiente contenido: ---{}--- por tanto no estaba saltando la
            // excepción y el paso ThenResponseShouldContain no fallaba en el assertEquals dando lugar a la tarea/bug TCPSIQA-3791
            if (json.toString().trim().equals("{}")) {
                System.out.println("Expected JSON is empty or malformed");
                return json;
            }
            return json;
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            return null;
        }
    }

    public static JSONArray convertStringToJSONArray(String json_str) {
        try {
            JSONArray json = new JSONArray(json_str);
            return json;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
