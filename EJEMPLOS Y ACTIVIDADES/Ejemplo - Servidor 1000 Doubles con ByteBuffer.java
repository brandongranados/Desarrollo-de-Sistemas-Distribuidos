import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class servidor {
    private ServerSocket servidor;
	private Socket conexion;
	private DataInputStream entrada;
	private ByteBuffer b;
    private byte[] a;
    private long inicio, fin;
	public static void main(String arg[])
	{
		servidor nuevoServidor =  new servidor();
	}
	static void msmRead(DataInputStream f, byte[] b, int pos, int longit) throws IOException
	{
		while(longit > 0)
		{
			int n = f.read(b, pos, longit);
			pos += n;
			longit -= n;
		}
	}
	public servidor()
	{
		try {
			servidor = new ServerSocket(5000);
			conexion = servidor.accept();
			entrada = new DataInputStream(conexion.getInputStream());
            a = new byte[8*1000];
            inicio = System.currentTimeMillis();
            msmRead(entrada, a, 0, 8000);
            fin = System.currentTimeMillis();
            b = ByteBuffer.wrap(a);
            for(int j=0; j<1000; j++)
                System.out.println(b.getDouble());
            System.out.println(fin-inicio);
			conexion.close();
		} catch (Exception e) {}
	}
}
