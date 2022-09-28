import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import com.google.gson.*;

class clienteREST
{
    private Usuario user;
    private URL url;
    private HttpURLConnection conectarServer;
    private BufferedReader entrada;
    private OutputStream salida;
    private String json, ip, correo, dominio;
    private Gson objetoJSON;
    private boolean update;
    public clienteREST(String ip)
    {
        boolean validar = true;
        objetoJSON = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
        user = new Usuario();
        this.ip = ip;
        Scanner leer = new Scanner(System.in);
        do
        {
            System.out.println("a. Alta usuario");
            System.out.println("b. Consulta usuario");
            System.out.println("c. Borra usuario");
            System.out.println("d. Salir");
            System.out.print("Opcion:_ ");
            validar = true;
            switch(leer.nextLine())
            {
                case "a":
                case "A":
                    altaUsuario();
                break;
                case "b":
                case "B":
                    consultaUsuario();
                break;
                case "c":
                case "C":
                    borraUsuario();
                break;
                case "d":
                case "D":
                    System.exit(0);
                break;
                default:
                    System.out.println("OPCION INVALIDA INTENTE DE NUEVO");
                    validar = false;
                break;
            }
        }while(!validar);
        leer.close();
    }
    private void altaUsuario()
    {
        Scanner lee = new Scanner(System.in);
        System.out.println("Ingresa tu email");
        user.email = lee.nextLine();
        System.out.println("Ingresa de favor tu nombre de usuario");
        user.nombre = lee.nextLine();
        System.out.println("INgresa tu apellido paterno");
        user.apellido_paterno = lee.nextLine();
        System.out.println("Ingresa tu apellido Materno");
        user.apellido_materno = lee.nextLine();
        System.out.println("Ingresa tu fecha nacimiento YYYY-MM-DD:mm:ss");
        user.fecha_nacimiento = lee.nextLine();
        System.out.println("Ingresa tu telefono");
        user.telefono = lee.nextLine();
        System.out.println("Ingresa tu genero M o F");
        user.genero = lee.nextLine();
        user.foto = null;
        lee.close();
        json = objetoJSON.toJson(user);
        dominio = "http://"+ip+":8080/Servicio/rest/ws/alta_usuario";
        estableceConex(1);
    }
    private void consultaUsuario()
    {
        Scanner lee = new Scanner(System.in);
        System.out.println("INGRESA EL EMAIL A CONSULTAR");
        correo = lee.nextLine();
        dominio = "http://"+ip+":8080/Servicio/rest/ws/consulta_usuario";
        estableceConex(2);
        if(update)
        {
            System.out.println("DESEA MODIFICAR EL USUARIO  [S/N]");
            switch(lee.nextLine())
            {
                case "s":
                case "S":
                    System.out.println("Ingresa de favor tu nombre de usuario");
                    user.nombre = lee.nextLine();
                    System.out.println("INgresa tu apellido paterno");
                    user.apellido_paterno = lee.nextLine();
                    System.out.println("Ingresa tu apellido Materno");
                    user.apellido_materno = lee.nextLine();
                    System.out.println("Ingresa tu fecha nacimiento YYYY-MM-DD:mm:ss");
                    user.fecha_nacimiento = lee.nextLine();
                    System.out.println("Ingresa tu telefono");
                    user.telefono = lee.nextLine();
                    System.out.println("Ingresa tu genero M o F");
                    user.genero = lee.nextLine();
                    user.foto = null;
                    json = objetoJSON.toJson(user);
                    dominio = "http://"+ip+":8080/Servicio/rest/ws/modifica_usuario";
                    estableceConex(4);
                break;
            }
        }
        lee.close();
    }
    private void borraUsuario()
    {
        Scanner leer =  new Scanner(System.in);
        System.out.println("INGRESA EL EMAIL");
        correo = leer.nextLine();
        leer.close();
        dominio = "http://"+ip+":8080/Servicio/rest/ws/borra_usuario";
        estableceConex(3);
    }
    private void estableceConex(int opt)
    {
        String mensaje = null;
        try {
            url = new URL(dominio);
            conectarServer = (HttpURLConnection) url.openConnection();
            conectarServer.setDoOutput(true);
            conectarServer.setRequestMethod("POST");
            conectarServer.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            switch(opt)
            {
                case 1://FUNCION ALTA USUARIO DEL SERVIDOR
                case 4://FUNCION MODIFICA USUARIO
                    json = "usuario=" + URLEncoder.encode(json, "UTF-8");
                break;
                case 2://FUNCION CONSULTA USUARIO DEL SERVIDOR
                case 3://FUNCION BORRA USUARIO DEL SERVIDOR
                    json = "email=" + URLEncoder.encode(correo, "UTF-8");
                break;
            }
            salida = conectarServer.getOutputStream();
            salida.write(json.getBytes());
            salida.flush();
            String imprime = "";
            switch(opt)
            {
                case 1://FUNCION ALTA USUARIO DEL SERVIDOR
                case 3://FUNCION BORRA USUARIO DEL SERVIDOR
                case 4://FUNCION MODIFICA USUARIO
                    if(conectarServer.getResponseCode() == 200)
                        System.out.println("OK");
                break;
                case 2://FUNCION CONSULTA USUARIO DEL SERVIDOR
                if(conectarServer.getResponseCode() == 200)
                {
                    entrada = new BufferedReader(new InputStreamReader(conectarServer.getInputStream()));
                    user = objetoJSON.fromJson(entrada.readLine(), Usuario.class);
                    System.out.println(user.email);
                    System.out.println(user.nombre);
                    System.out.println(user.apellido_paterno);
                    System.out.println(user.apellido_materno);
                    System.out.println(user.fecha_nacimiento);
                    System.out.println(user.telefono);
                    System.out.println(user.genero);
                    entrada.close();
                    update = true;
                }
                break;
            }
            if(conectarServer.getResponseCode() != 200)
            {
                entrada = new BufferedReader(new InputStreamReader(conectarServer.getErrorStream()));
                while ((imprime = entrada.readLine()) != null)
                    System.out.println(imprime);
                entrada.close();
                update = false;
            }
            conectarServer.disconnect();
        } catch (Exception e) {}
        finally
        {
            conectarServer.disconnect();
        }
    }
    public static void main(String[] args) {
        clienteREST nuevo = new clienteREST("20.185.19.172");
    }
}