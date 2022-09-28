import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class servidorMulticast
{
    private ByteBuffer bufferDouble;
    public static void enviaMsm (byte[] contMsm, String ip, int port) throws IOException
    {
        DatagramSocket socket = new DatagramSocket();
        InetAddress grupMulticast = InetAddress.getByName(ip);
        DatagramPacket paqueteDatos = new DatagramPacket(contMsm, contMsm.length, grupMulticast, port);
        socket.send(paqueteDatos);
        socket.close();
    }
    public servidorMulticast()
    {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            enviaMsm("hola".getBytes(), "230.0.0.0", 50000);
            bufferDouble = ByteBuffer.allocate(5*8);
            bufferDouble.putDouble(1.1);
            bufferDouble.putDouble(1.2);
            bufferDouble.putDouble(1.3);
            bufferDouble.putDouble(1.4);
            bufferDouble.putDouble(1.5);
            enviaMsm(bufferDouble.array(), "230.0.0.0", 50000);
        } catch (IOException e) {}
    }
    public static void main(String[] args) {
        servidorMulticast nuevo = new servidorMulticast();
    }
}