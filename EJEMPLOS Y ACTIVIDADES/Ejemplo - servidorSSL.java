import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocketFactory;

public class servidorSSL {
    private SSLServerSocketFactory serverFactory;
    private ServerSocket servidor;
    private Socket conexionServidor;
    private DataInputStream entrada;
    public static void main(String[] args) {
        servidorSSL nuevo = new servidorSSL();
    }
    public servidorSSL()
    {
        try {
            //System.getProperty("Djavax.net.ssl.keyStore", "/home/brandon/keystoreServidor.jks");
            //System.getProperty("Djavax.net.ssl.keyStorePassword", "DAILEX980704");
            serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            servidor =  serverFactory.createServerSocket(5000);
            conexionServidor = servidor.accept();
            entrada = new DataInputStream(conexionServidor.getInputStream());
            System.out.println(entrada.readDouble());
            conexionServidor.close();
        } catch (Exception e) {}
    }
}
