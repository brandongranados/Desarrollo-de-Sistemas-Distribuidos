import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class token
{
    private Scanner leer;
    private ServerSocket servidor;
    private Socket servConex, clientConex;
    private SSLSocketFactory clientSSL;
    private SSLServerSocketFactory servSSL;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private int token, temp;
    private boolean escape;
    public token()
    {
        token = 0;
        temp = 0;
        escape = false;
        System.out.println("Ingresa el numero de nodo a ejecutar");
        leer = new Scanner(System.in);
        escogeNodo(leer.nextInt());
    }
    public void escogeNodo(int nodo)
    {
        if(nodo != 5)
            temp = nodo+1; 
        switch (nodo) 
        {
            case 0:
                while(!escape)
                {
                    cliente(nodo);
                    if(!escape)
                        server(nodo);
                }
                System.out.println("El token es igual a :"+token);
            break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                while(!escape)
                {
                    server(nodo);
                    cliente(nodo);
                }
            break;
            default:
                System.out.println("Opcion no valida escoge entre 0 y 5");
                escogeNodo(nodo);
            break;
        }
    }
    public void cliente(int nodo)
    {
        while(!escape)
            try {
                clientSSL = (SSLSocketFactory) SSLSocketFactory.getDefault();
                clientConex = clientSSL.createSocket("localhost", 50000+temp);
                salida = new DataOutputStream(clientConex.getOutputStream());
                if(nodo == 0)
                    salida.writeInt(nodoCero());
                else
                    salida.writeInt(nodoN());
                salida.writeBoolean(escape);
                clientConex.close();
                Thread.sleep(100);
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {}
            }
    }
    public void server(int nodo)
    {
        try {
            servSSL = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            servidor = servSSL.createServerSocket(50000+nodo);
            servConex = servidor.accept();
            entrada = new DataInputStream(servConex.getInputStream());
            token = entrada.readInt();
            escape = entrada.readBoolean();
            servConex.close();
            servidor.close();
        } catch (Exception e) {}
    }
    public int nodoCero()
    {
        System.out.println("en proceso valor de token :"+token);
        if(token >= 500)
            escape = true;
        return token == 0 ? 0 : ++token;
    }
    public int nodoN()
    {
        System.out.println("en proceso valor de token :"+token);
        return ++token;
    }
    public static void main(String[] args) {
    	//ESENCIALES PARA INDICAR QUE SE UTILIZARA ENCRIPTACION ASINCRONA DEL CLIENTE
        //System.setProperty("Djavax.net.ssl.trustStore", "/home/brandon/keystoreCliente.jks");
        //System.setProperty("Djavax.net.ssl.trustStorePassword", "DAILEX980704");
        //ESENCIALES PARA INDICAR QUE SE UTILIZARA ENCRIPTACION ASINCRONA DEL SERVIDOR
        //System.setProperty("Djavax.net.ssl.keyStore", "/home/brandon/keystoreServidor.jks");
        //System.setProperty("Djavax.net.ssl.keyStorePassword", "DAILEX980704");
        token nuevo = new token();
    }
}
