package clientServer;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class servidor1000 {
	private ServerSocket servidor;
	private Socket conexion;
	private DataInputStream entrada;
	public static void main(String arg[])
	{
		servidor1000 nuevoServidor1000 =  new servidor1000();
	}
	public servidor1000()
	{
		try {
			servidor = new ServerSocket(5000);
			conexion = servidor.accept();
			entrada = new DataInputStream(conexion.getInputStream());
			long incio = System.currentTimeMillis();
			for(int j=0; j<1000; j++)
				System.out.println(entrada.readDouble());
			long fin = System.currentTimeMillis();
			System.out.println(fin-incio);
			conexion.close();
		} catch (Exception e) {}
	}
}
