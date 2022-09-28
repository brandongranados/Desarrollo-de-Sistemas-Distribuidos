import java.io.DataOutputStream;
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
            conexionCliente = cliente.createSocket("localhost", 5000);
            salida = new DataOutputStream(conexionCliente.getOutputStream());
            salida.writeDouble(123.123);
            Thread.sleep(100);
            conexionCliente.close();
        } catch (Exception e) {}
    } 
}
