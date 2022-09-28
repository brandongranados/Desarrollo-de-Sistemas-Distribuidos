import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocketFactory;

public class servidorSSL {
    private SSLServerSocketFactory serverFactory;
    private ServerSocket servidor;
    public static class hilosServidor extends Thread
    {
        private Socket hiloConexion;
        private DataInputStream entrada;
        private int longArchivo;
        private String nomArchivo;
        private byte[] contArchivo;
        public hilosServidor(Socket hiloConexion)
        {
            this.hiloConexion = hiloConexion;
        }
        public void run()
        {
            try {
                entrada = new DataInputStream(this.hiloConexion.getInputStream());
                nomArchivo = entrada.readUTF();
                longArchivo = entrada.readInt();
                contArchivo= new byte[longArchivo];
                msmRead(entrada, contArchivo, 0, longArchivo);
                escribeArchivo(nomArchivo, contArchivo);
                this.hiloConexion.close();
            } catch (Exception e) {}
        }
    }
    public static void main(String[] args) throws IOException{
        servidorSSL nuevo = new servidorSSL();
    }
    public servidorSSL() throws IOException
    {
        //System.getProperty("Djavax.net.ssl.keyStore", "/home/brandon/keystoreServidor.jks");
        //System.getProperty("Djavax.net.ssl.keyStorePassword", "DAILEX980704");
        serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        servidor =  serverFactory.createServerSocket(55003);
        Socket uno = servidor.accept();
        hilosServidor nuevoHilo = new hilosServidor(uno);
        nuevoHilo.start();
    }
    public static void msmRead(DataInputStream ent, byte[] g, int pos, int longitud) throws IOException
    {
        while(longitud > 0)
        {
            int tam = ent.read(g, pos, longitud);
            System.out.println(g);
            pos += tam;
            longitud -= tam;    
        }
    }
    public static void escribeArchivo (String arch, byte[] cont)
    {
        System.out.println("ESCRIBIO"); 
        try {
            FileOutputStream fichero = new FileOutputStream(arch);
            fichero.write(cont);
            fichero.close();
        }catch(Exception e){}
    }
}
