import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class invocacion 
{
    private URL url;
    private HttpURLConnection conex;
    private String parametros;
    private OutputStream salida;
    private BufferedReader lectura;
    public invocacion() throws IOException
    {
        try {
            url = new URL("http://20.25.113.158:8080/Servicio/rest/ws/consulta_usuario");
            conex = (HttpURLConnection) url.openConnection();
            conex.setDoOutput(true);
            conex.setRequestMethod("POST");
            conex.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            parametros = "email=" + URLEncoder.encode("example@yahoo.com", "UTF-8");
            salida = conex.getOutputStream();
            salida.write(parametros.getBytes());
            salida.flush();
            lectura = new BufferedReader(new InputStreamReader(conex.getInputStream()));
            String imprime = null;
            if(conex.getResponseCode() == 200)
                while ( (imprime = lectura.readLine()) != null)
                    System.out.println(imprime);
            else
                while ( (imprime = lectura.readLine()) != null)
                    System.out.println(imprime);
            conex.disconnect();
        } catch (Exception e) {}
        finally
        {
            lectura.close();
            conex.disconnect();
        }
    }
    public static void main(String[] args) {
        try {
            invocacion nuevo = new invocacion();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}