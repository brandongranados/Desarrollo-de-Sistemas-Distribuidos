import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;

public class clienteMulticast {
    private MulticastSocket socketCliente;
    private byte[] info;
    private byte[] recDouble;
    private ByteBuffer num;
    public static byte[] recibeMsm(MulticastSocket socket, int longMsm) throws IOException
    {
        byte[] buffer = new byte[longMsm];
        DatagramPacket paqueteRec = new DatagramPacket(buffer, longMsm);
        socket.receive(paqueteRec);
        return paqueteRec.getData();
    }
    public clienteMulticast()
    {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            info = new byte[4];
            socketCliente = new MulticastSocket(50000);
            InetSocketAddress grupoClientes = new InetSocketAddress(InetAddress.getByName("230.0.0.0"), 50000);
            NetworkInterface nicInterfaz = NetworkInterface.getByName("em1");
            socketCliente.joinGroup(grupoClientes, nicInterfaz);
            info = recibeMsm(socketCliente, 4);
            System.out.println(new String(info,"UTF-8"));
            recDouble = recibeMsm(socketCliente, 5*8);
            num = ByteBuffer.wrap(recDouble);
            for (int i=0; i<5; i++)
                System.out.println(num.getDouble());
            socketCliente.leaveGroup(grupoClientes, nicInterfaz);
            socketCliente.close();
        } catch (IOException e) {}
    }
    public static void main(String[] args) {
        clienteMulticast nuevo = new clienteMulticast();
    }
}
