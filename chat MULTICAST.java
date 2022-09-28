import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Scanner;

public class chat
{
    public static byte[] recibeMensaje(MulticastSocket socket, int longitudMensaje) throws IOException
    {
        byte[] buffer = new byte[longitudMensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }
    public static class worker extends Thread
    {
        private MulticastSocket conex;
        private InetSocketAddress grupoClientes;
        private NetworkInterface nic;
        private String mensaje;
        public void run()
        {
            //EN UN CICLO INFINITO SE RECIBIRAN SE RECIBIRAN LOS MENSAJES ENVIADOS AL GRUPO 230.0.0.0 A
            //TRAVES DEL PUERTO 50000 Y SE DESPLEGARAN EN LA PANTALLA
            System.setProperty("java.net.preferIPv4Stack", "true");
            while(true)
                try {
                    conex = new MulticastSocket(50000);
                    grupoClientes = new InetSocketAddress(InetAddress.getByName("230.0.0.0"), 50000);
                    nic =  NetworkInterface.getByName("chat-vnet/default");
                    conex.joinGroup(grupoClientes, nic);
                    mensaje = new String(recibeMensaje(conex, 1000), "windows-1252");//revisar longitud de mensaje
                    System.out.println(mensaje);
                    mensaje = null;
                    conex.leaveGroup(grupoClientes, nic);
                    conex.close();
                    Thread.sleep(100);
                } catch (IOException e) 
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {}
                } 
                catch (InterruptedException e) {}
        }
    }
    public static void main(String[] args) 
    {
        worker nuevo = new worker();
        nuevo.start();

        String nombre = args[0];
        //EN UN CICLO INFINITO SE LEERA CADA MENSAJE DEL TECLADO Y SE ENVIARA EL MENSAJE AL
        //GRUPO 230.0.0.0 A TRAVES DEL PUERTO 50000
        Scanner leer = new Scanner(System.in);
        System.out.println("escribe TUS mensajeS");
        while(true)
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress grupoMulticast = InetAddress.getByName("230.0.0.0");
                String lectura = nombre+" :"+leer.nextLine();
                DatagramPacket paqueteDatos = new DatagramPacket(lectura.getBytes(), lectura.length(), grupoMulticast, 50000);
                socket.send(paqueteDatos);
                socket.close();
                Thread.sleep(100);
            } catch (IOException e) 
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {}
            } 
        catch (InterruptedException e) {}
    }
}