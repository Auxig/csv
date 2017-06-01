import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import dominio.Usuario;

public class EjemploCsvImportacion {

    public static void main(String[] args) {

        try {

            List<Usuario> usuariosEsperados = new ArrayList<Usuario>();
            List<Usuario> usuariosRespuesta = new ArrayList<Usuario>();

            // De la clase AbstractHTTPMethod, método readResponse recuperamos el csv con el inputStream
            FileInputStream in = new FileInputStream("/home/auxi/Documentos/usuarios.csv");
            InputStreamReader input = new InputStreamReader(in);

            CsvReader csvRespuesta = new CsvReader(input);

            csvRespuesta.readHeaders();

            while (csvRespuesta.readRecord()) {
                String codigo = csvRespuesta.get(0);
                String nombres = csvRespuesta.get(1);
                String apellidos = csvRespuesta.get("Apellidos");
                String correo = csvRespuesta.get("Correo");

                Usuario usuarioRespuesta = new Usuario(codigo, nombres, apellidos, correo);

                usuariosRespuesta.add(usuarioRespuesta);

            }

            csvRespuesta.close();

            System.out.println("*****************************************************************************************");
            System.out.println("USUARIOS DE LA RESPUESTA A LA PETICIÓN");
            for(Usuario us : usuariosRespuesta){
                System.out.println(us.getCodigo() + " : " + us.getNombres() + " " + us.getApellidos() + " - " + us.getCorreo());
            }
            System.out.println("*****************************************************************************************");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            CsvReader csvEsperado = new CsvReader("/home/auxi/Documentos/usuarios_import.csv");
            csvEsperado.readHeaders();

            while (csvEsperado.readRecord())
            {
                String codigo = csvEsperado.get(0);
                String nombres = csvEsperado.get(1);
                String apellidos = csvEsperado.get("Apellidos");
                String correo = csvEsperado.get("Correo");

                Usuario usuarioEsperado = new Usuario(codigo, nombres, apellidos, correo);

                usuariosEsperados.add(usuarioEsperado);

                System.out.println("///////////////////////////////////////////////////////");
                System.out.println("el usuario "+ usuarioEsperado.getNombres() +" ¿esta contenido en la respuesta?: " + (usuariosRespuesta.contains(usuarioEsperado)));
            }

            csvEsperado.close();

            System.out.println("*****************************************************************************************");
            System.out.println("USUARIOS ESPERADOS");
            for(Usuario us : usuariosEsperados){
                System.out.println(us.getCodigo() + " : " + us.getNombres() + " " + us.getApellidos() + " - " + us.getCorreo());
            }
            System.out.println("*****************************************************************************************");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//            boolean encontrado = false;
//
//            if (usuariosEsperados.size() > usuariosRespuesta.size()) {
//                System.out.println("ERROR .- Hay más usuarios esperados que recibidos");
//            } else {
//                for (int i = 0; i < usuariosEsperados.size() ; i++) {
//                    encontrado = false;
//                    Usuario esperado = usuariosEsperados.get(i);
//                    int j = 0;
//                    while (!encontrado && j < usuariosRespuesta.size() ) {
//                        System.out.println("*****************************************************************");
//                        System.out.println("Comparando esperado " + i + " con respuesta " + j);
//                        Usuario respuesta = usuariosRespuesta.get(j);
//
//                        if (esperado.getCodigo().equals(respuesta.getCodigo())) {
//                            System.out.println("Los codigos son iguales " +esperado.getCodigo() + " " + respuesta.getCodigo());
//                            if (esperado.getNombres().equals(respuesta.getNombres())) {
//                                System.out.println("Los nombres son iguales " +esperado.getNombres() + " " + respuesta.getNombres());
//                                if (esperado.getApellidos().equals(respuesta.getApellidos())) {
//                                    System.out.println("Los apellidos son iguales " +esperado.getApellidos() + " " + respuesta.getApellidos());
//                                    if (esperado.getCorreo().equals(respuesta.getCorreo())) {
//                                        System.out.println("Los correos son iguales "+esperado.getCorreo() + " " + respuesta.getCorreo());
//                                        System.out.println("OBJETOS IDENTICOS");
//                                        encontrado = true;
//                                    } else {
//                                        System.out.println("Los correos NO son iguales "+esperado.getCorreo() + " " + respuesta.getCorreo());
//                                    }
//                                } else {
//                                    System.out.println("Los apellidos NO son iguales " +esperado.getApellidos() + " " + respuesta.getApellidos());
//                                }
//                            } else {
//                                System.out.println("Los nombres NO son iguales " +esperado.getNombres() + " " + respuesta.getNombres());
//                            }
//                        } else {
//                            System.out.println("Los codigos NO son iguales " +esperado.getCodigo() + " " + respuesta.getCodigo());
//                        }
//
//                        if (j == usuariosRespuesta.size()-1 && !encontrado) {
//                            System.out.println("ERROR.- el usuario " + esperado.getCodigo() + " no está en la respuesta");
//                        }
//
//                        j++;
//                        System.out.println("*****************************************************************");
//                    }
//                }
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}