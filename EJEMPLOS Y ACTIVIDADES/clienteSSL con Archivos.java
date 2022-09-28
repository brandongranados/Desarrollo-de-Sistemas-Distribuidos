import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

public class clienteSSL {

    private SSLSocketFactory cliente;
    private Socket conexionCliente;
    private DataOutputStream salida;
    public static void main(String[] args) {
        clienteSSL nuevo = new clienteSSL();
    }
    public clienteSSL()
    {
        try {
            //System.getProperty("Djavax.net.ssl.trustStore", "/home/brandon/keystoreCliente.jks");
            //System.getProperty("Djavax.net.ssl.trustStorePassword", "DAILEX980704");
            cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
            conexionCliente = cliente.createSocket("localhost", 55003);
            salida = new DataOutputStream(conexionCliente.getOutputStream());
            byte dos[]= leeArchivo("./uno.txt");
            salida.writeUTF("./hola.txt");
            salida.writeInt(dos.length);
            salida.write(dos);
            Thread.sleep(100);
            conexionCliente.close();
        } catch (Exception e) {}
    }
    public static byte[] leeArchivo(String nombre) throws IOException
    {
        FileInputStream lectura = new FileInputStream(nombre);
        byte[] contenido;
        try {
            contenido = new byte[lectura.available()];
            lectura.read(contenido);
        } 
        finally
        {
            lectura.close();
        }
        return contenido;
    } 
}
