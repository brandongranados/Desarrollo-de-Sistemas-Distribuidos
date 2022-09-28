import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class matriz
{
    private double a[][], b[][], c[][];
    private int nodo;
    private Scanner leer;
    private ServerSocket serverConex;
    private Socket conex;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private String host[];
    public static void main(String[] args) {
        matriz nuevo = new matriz();
    }
    public matriz()
    {
        leer = new Scanner(System.in);
        host = new String[3];
        a = new double[1000][1000]; 
        b = new double[1000][1000]; 
        c = new double[1000][1000];
        System.out.println("Ingrese el numero de este nodo");
        nodo = leer.nextInt();
        System.out.println("INGRESA LOS HOST DE LOS NODOS A LOS QUE NOS DEBEMOS DE CONECTAR");
        for(int i=0; i<3; i++)
        {
            System.out.println("INGRESA EL HOST :"+(i+1));
            host[i] = leer.next();
        }
        for(int i=0; i<1000; i++)
            for(int j=0; j<1000; j++)
                c[i][j] = 0;
        if(nodo == 0)
            cliente();
        else
            servidor();
    }
    public void nodoCero()
    {
        //LLENADO DE MATRICES
        for(int i=0; i<1000; i++)
            for(int j=0; j<1000; j++)
            {
                a[i][j] = i+5*j;
                b[i][j] = 5*i-j;
            }
        //TRANSPUESTA DE B
        for(int i=0; i<1000; i++)
            for(int j=0; j<1000; j++)
            {
                double x = b[i][j];
                b[i][j]=b[j][i];
                b[j][i]=x;
            }
    }
    public void cliente()
    {
        int i=0; 
        int cantidad = 0;
        nodoCero();
        //OPERACION DE C4= A2 X B2
        while(true)
        {
            i++;
            try {
                int inc1=0, inc2=0;
                conex = new Socket(host[i-1], 50000+i);
                salida = new DataOutputStream(conex.getOutputStream());
                entrada = new DataInputStream(conex.getInputStream());
                cantidad+=1;
                if(i == 1)
                {
                    inc1 = 0;
                    inc2 = 0;
                }
                else if(i == 2)
                {
                    inc1 = 0;
                    inc2 = 500;
                }
                else
                {
                    inc1 = 500;
                    inc2 = 0;
                }
                for(int j=0; j<500; j++)
                    for(int k=0; k<1000; k++)
                    {
                        salida.writeDouble(a[j+inc1][k]);
                        salida.writeDouble(b[j+inc2][k]);
                    }
                for(int j=0+inc1; j<500+inc1; j++)
                    for(int k=0+inc2; k<500+inc2; k++)
                        c[j][k] = entrada.readDouble();
                Thread.sleep(10);
                conex.close();
            } catch (Exception e) 
            {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e1) {}
            }
            if(i == 3)
                i = 0;
            if(cantidad == 3)
                break;
        }
        multiplicaMatriz(500, 500, 0, 1000, 1000, 1000);
        //DESPLIEGUE DE A, B Y C CUANDO N = 8
        System.out.println("DESPLIEGUE DE A, B Y C CUANDO N = 8");
        imprime(8);
        System.out.println("DESPLIEGUE DE CHECKSUM C CUANDO N = 8");
        System.out.println(checksum(8));
        //DESPLIEGUE DE CHECKSUM C CUANDO N = 1000
        System.out.println("DESPLIEGUE DE CHECKSUM C CUANDO N = 1000");
        System.out.println(checksum(1000));
    }
    public void servidor()
    {
        int inc1=0, inc2=0;
        try {
            serverConex = new ServerSocket(50000+nodo);
            conex = serverConex.accept();
            entrada = new DataInputStream(conex.getInputStream());
            salida = new DataOutputStream(conex.getOutputStream());
            if(nodo == 1)
            {
                inc1 = 0;
                inc2 = 0;
            }
            else if(nodo == 2)
            {
                inc1 = 0;
                inc2 = 500;
            }
            else
            {
                inc1 = 500;
                inc2 = 0;
            }
            for(int j=0; j<500; j++)
                for(int k=0; k<1000; k++)
                {
                    a[j+inc1][k] = entrada.readDouble();//RECIBE A2
                    b[j+inc2][k] = entrada.readDouble();//RECIBE B1
                }
            multiplicaMatriz(inc1, inc2, 0, inc1+500, inc2+500, 1000);
            for(int j=0+inc1; j<500+inc1; j++)
                for(int k=0+inc2; k<500+inc2; k++)
                    salida.writeDouble(c[j][k]);
            conex.close();
            serverConex.close();
        } catch (Exception e) {}
    }
    public void multiplicaMatriz(int uno, int dos, int tres, int uno1, int dos2, int tres3)
    {
        for(int i=uno; i<uno1; i++)//RENGLONES
            for(int j=dos; j<dos2; j++)//COLUMNAS
                for(int k=tres; k<tres3; k++)//COLUMNAS
                    c[i][j] += a[i][k]*b[j][k];
    }
    public void imprime(int chec)
    {
        System.out.println("DESPLIEGUE DE A");
        for(int i=0; i<chec; i++)//RENGLONES
            for(int j=0; j<chec; j++)//COLUMNAS
                System.out.println(a[i][j]);
        System.out.println("DESPLIEGUE DE B");
        for(int i=0; i<chec; i++)//RENGLONES
            for(int j=0; j<chec; j++)//COLUMNAS
                System.out.println(b[i][j]);
        System.out.println("DESPLIEGUE DE C");
        for(int i=0; i<chec; i++)//RENGLONES
            for(int j=0; j<chec; j++)//COLUMNAS
                System.out.println(c[i][j]);
    }
    public double checksum(int val)
    {
        double retorno=0;
        for(int i=0; i<val; i++)
            for(int j=0; j<val; j++)
                retorno += c[i][j];
        return retorno;
    }
}