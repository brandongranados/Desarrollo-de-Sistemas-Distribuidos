import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class cliente {
    private Socket conexion;
	private DataOutputStream salida;
	private ByteBuffer b;
    private long inicio, fin;
    public static void main(String arg[])
	{
		cliente nuevoCliente = new cliente();
	}
	public cliente()
	{
		for(;;)
			try {
				conexion = new Socket("localhost", 5000);
				salida = new DataOutputStream(conexion.getOutputStream());
				b = ByteBuffer.allocate(1000*8);
                inicio = System.currentTimeMillis();
				for(int i=0; i<1000; i++)
                    b.putDouble((double)(i+1));
                salida.write(b.array());
                fin = System.currentTimeMillis();
                System.out.println(fin-inicio);
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
