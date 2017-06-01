package manager;

import dominio.Usuario;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by auxi on 1/06/17.
 */
public class CsvFileReader {

    //CSV file header
    private static final String [] FILE_HEADER_MAPPING = {"Codigo","Nombres","Apellidos","Correo"};

    //Student attributes
    private static final String CODIGO = "Codigo";
    private static final String NOMBRE = "Nombres";
    private static final String APELLIDOS = "Apellidos";
    private static final String CORREO = "Correo";

    public static void readCsvFile(String fileName) {

        FileReader fileReader = null;

        CSVParser csvFileParser = null;

        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

        try {

            //Create a new list of student to be filled by CSV file data
            List<Usuario> students = new ArrayList();

            //initialize FileReader object
            fileReader = new FileReader(fileName);

            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            //Get a list of CSV file records
            List csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = (CSVRecord) csvRecords.get(i);
                //Create a new student object and fill his data
                Usuario usuario = new Usuario(record.get(CODIGO), record.get(NOMBRE), record.get(APELLIDOS), record.get(CORREO));
                students.add(usuario);
            }

            //Print the new student list
            for (Usuario student : students) {
                System.out.println(student.toString());
            }
        }
        catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }

    }

}
