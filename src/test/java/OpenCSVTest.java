import com.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by auxi on 1/06/17.
 */
public class OpenCSVTest {

    public static void main(String[] args) {
        CSVReader reader = null;
        try {
            FileInputStream in = new FileInputStream("/home/auxi/Documentos/usuarios.csv");
            InputStreamReader input = new InputStreamReader(in);

            reader = new CSVReader(input);
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println("User [code= " + line[0] + ", name= " + line[1] + " , surname=" + line[2] + " , email=" + line[3] + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
