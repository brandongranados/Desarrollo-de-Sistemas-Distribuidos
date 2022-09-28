import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class cliente {
	private Socket conexion;
	private DataOutputStream salida;
	private DataInputStream entrada;
	private byte[] buffer;
	private ByteBuffer b;
	public static void main(String arg[])
	{
		cliente nuevoCliente = new cliente();
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
	public cliente()
	{
		for(;;)
			try {
				conexion = new Socket("localhost", 5000);
				salida = new DataOutputStream(conexion.getOutputStream());
				entrada = new DataInputStream(conexion.getInputStream());
				buffer =  new byte[4];
				b = ByteBuffer.allocate(5*8);
				b.putDouble(1.1);
				b.putDouble(1.2);
				b.putDouble(1.3);
				b.putDouble(1.4);
				b.putDouble(1.5);
				byte a[] = b.array();
				salida.writeInt(123);
				salida.writeDouble(1234567890.1234567890);
				salida.write("hola".getBytes());
				salida.write(a);
				msmRead(entrada, buffer, 0, 4);
				System.out.println(new String(buffer, "UTF-8"));
				Thread.sleep(1000);
				conexion.close();
				break;
			} catch (Exception e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {}
			}
	}
}
