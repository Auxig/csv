
import manager.CsvFileReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * Created by auxi on 1/06/17.
 */
public class ApacheCSV {
    public static void main(String[] args) {
//
//        try {
//            FileInputStream in = new FileInputStream("/home/auxi/Documentos/usuarios.csv");
//            InputStreamReader input = new InputStreamReader(in);
//            CsvFileReader.readCsvFile(input);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        CsvFileReader.readCsvFile("/home/auxi/Documentos/usuarios.csv");

    }
}
