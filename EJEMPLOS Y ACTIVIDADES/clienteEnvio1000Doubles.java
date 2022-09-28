package clientServer;

import java.io.DataOutputStream;
import java.net.Socket;

public class cliente1000{
	private Socket conexion;
	private DataOutputStream salida;
	public static void main(String arg[])
	{
		cliente1000 nuevoCliente1000 = new cliente1000();
	}
	public cliente1000()
	{
		for(;;)
			try {
				conexion = new Socket("localhost", 5000);
				salida = new DataOutputStream(conexion.getOutputStream());
				long incio = System.currentTimeMillis();
				for(int i=0; i<1000; i++)
					salida.writeDouble((double)(i+1));
				long fin = System.currentTimeMillis();
				System.out.println(fin-incio);
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
