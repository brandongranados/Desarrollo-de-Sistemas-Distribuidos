import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class calculoPI
{
    private ServerSocket servidor;
    private Socket cliente;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private double resNodo[];
    public static void main(String arg[])
    {
        Scanner leer = new Scanner(System.in);
        System.out.println("Nodo que deseas ejecutar entre 0-4");
        calculoPI nuevo = new calculoPI(leer.nextInt());
    }
    public calculoPI(int sel)
    {
        resNodo = new double[4];
        for (int f=0; f<resNodo.length; f++)
            resNodo[f] = 0;
        switch(sel)
        {
            case 0:
                nodo0(sel);
            break;
            case 1:
            case 2:
            case 3:
            case 4:
                calculoNodo(sel);
            break;
        }
    }
    public void nodo0(int num)
    {
        int cantNodos = 1;
        for(int i=num; cantNodos<5; i++)
        {
            try {
                if(i>4)
                    i=1;
                if(resNodo[i-1] == 0)
                {
                    cliente = new Socket("localhost", 5000+i);
                    entrada = new DataInputStream(cliente.getInputStream());
                    resNodo[i-1] = entrada.readDouble();
                    cantNodos++;
                    cliente.close();
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {}
            }
        }
        System.out.println("El valor de PI es : "+(resNodo[0]+resNodo[1]+resNodo[2]+resNodo[3]));
    }
    public void calculoNodo(int nodo)
    {
        try {
            servidor = new ServerSocket(5000+nodo);
            cliente = servidor.accept();
            salida = new DataOutputStream(cliente.getOutputStream());
            salida.writeDouble(calculo(nodo));
            cliente.close();
        } catch (Exception e) {}
    }
    public double calculo(int nodo)
    {
        double res = 0;
        if(nodo%2 == 0)
            for(int i=0; i<1000000; i++)
                res -= 4.0/(8*i+2*(nodo-2)+3);
        else
            for(int i=0; i<1000000; i++)
                res += 4.0/(8*i+2*(nodo-2)+3);
        return res;
    }
}