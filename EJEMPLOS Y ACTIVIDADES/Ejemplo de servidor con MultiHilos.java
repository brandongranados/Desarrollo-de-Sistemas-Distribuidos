import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class servidor2 {
    private ServerSocket servidor;
    public static class worker extends Thread
    {
        private Socket conexion;
        private DataOutputStream salida;
	    private DataInputStream entrada;
        private byte[] buffer, a;
	    private ByteBuffer b;
        public worker(Socket conexion)
        {
            this.conexion = conexion;
        }
        public static void msmRead(DataInputStream f, byte[] b, int pos, int longit) throws IOException
        {
            while(longit > 0)
            {
                int n = f.read(b, pos, longit);
                pos += n;
                longit -= n;
            }
        }
        public void run()
        {
            try {
                salida = new DataOutputStream(conexion.getOutputStream());
                entrada = new DataInputStream(conexion.getInputStream());
                int n = entrada.readInt();
                System.out.println(n);
                double x = entrada.readDouble();
                System.out.println(x);
                buffer = new byte[4];
                msmRead(entrada, buffer, 0, 4);
                System.out.println(new String(buffer, "UTF-8"));
                salida.write("HOLA".getBytes());
                a = new byte[5*8];
                msmRead(entrada, a, 0, 4);
                b = ByteBuffer.wrap(a);
                for (int i=0; i<5; i++)
                    System.out.println(b.getDouble());
                conexion.close();
            } catch (IOException e) {}
        }
    }
    public servidor2() throws IOException
    {
        this.servidor = new ServerSocket(5000);
        while(true)
        {
            Socket conexion = this.servidor.accept();
            worker w = new worker(conexion);
            w.start();
        }
    }
    public static void main(String arg[]) throws IOException
    {
        servidor2 nuevoServidor2 = new servidor2();
    }
}
