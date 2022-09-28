import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClienteRMI {

    private double a[][], b[][], c[][], x[][];
    private int n;
    static double a1[][], a2[][], a3[][], a4[][];
    static double b1[][], b2[][], b3[][], b4[][];
    static double c1[][], c2[][], c3[][], c4[][], c5[][], c6[][], c7[][], c8[][];
    static double c9[][], c10[][], c11[][], c12[][], c13[][], c14[][], c15[][], c16[][];
    private String ip[];
    private Scanner leer;
    private worker nodo1, nodo2, nodo3, nodo4;
    public static double[][] separaMatriz(double a[][], int inicio, int n)
    {
        double m[][] = new double[n/4][n];
        for(int i=0; i<n/4; i++)
            for(int j=0; j<n; j++)
                m[i][j] = a[i+inicio][j];
        return m;
    }
    public static void acomodaMatriz(double c[][], double a[][], int renglon, int columna, int n)
    {
        for(int i=0; i<n/4; i++)
            for(int j=0; j<n/4; j++)
                c[i+renglon][j+columna] = a[i][j];
    }
    public ClienteRMI(int n)
    {
        this.n = n;
        leer = new Scanner(System.in);
        ip = new String[4];
        a = new double[n][n];
        b = new double[n][n];
        c = new double[n][n];
        x = new double[n][n];
        a1 = new double[n/4][n];
        b1 = new double[n/4][n];
        a2 = new double[n/4][n];
        b2 = new double[n/4][n];
        a3 = new double[n/4][n];
        b3 = new double[n/4][n];
        a4 = new double[n/4][n];
        b4 = new double[n/4][n];
        for(int i=0; i<this.n; i++)
            for(int j=0; j<this.n; j++)
            {
                a[i][j] = i+2*j;
                b[i][j] = 3*i-j;
            }
        for(int i=0; i<this.n; i++)
            for(int j=0; j<this.n; j++)
                x[j][i] = b[i][j];
        b = x;
        System.out.println("ingresa las ip");
        for(int i=0; i<4; i++)
        {
            System.out.println("ingresa la ip del nodo :"+(i+1));
            ip[i] = leer.nextLine();
        }
        try {
            conectar();
            System.out.println("checksum cuando  n ="+n+"es igual a :"+checksum());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void conectar() throws Exception 
    {
        a1 = separaMatriz(a, 0, n);
        a2 = separaMatriz(a, (n/4), n);
        a3 = separaMatriz(a, (n/2), n);
        a4 = separaMatriz(a, ((n*3)/4), n);
        b1 = separaMatriz(b, 0, n);
        b2 = separaMatriz(b, (n/4), n);
        b3 = separaMatriz(b, (n/2), n);
        b4 = separaMatriz(b, ((n*3)/4), n);
        //CREAMOS LOS OBJETOS DE LOS HILOS
        nodo1 = new worker(n, ip[0], 1);
        nodo2 = new worker(n, ip[1], 2);
        nodo3 = new worker(n, ip[2], 3);
        nodo4 = new worker(n, ip[3], 4);
        //INICIA LA EJECUCION DE LOS HILOS
        nodo1.start();
        nodo2.start();
        nodo3.start();
        nodo4.start();
        //ESPERA A QUE LOS HILOS TERMINE PARA QUE EL CLIENTE CONTINUE SU EJECUCION
        nodo1.join();
        nodo2.join();
        nodo3.join();
        nodo4.join();
        //ACOMODA MATRIZ
        acomodaMatriz(c, c1, 0, 0, n);
        acomodaMatriz(c, c2, 0, (n/4), n);
        acomodaMatriz(c, c3, 0, (n/2), n);
        acomodaMatriz(c, c4, 0, ((3*n)/4), n);
        acomodaMatriz(c, c5, (n/4), 0, n);
        acomodaMatriz(c, c6, (n/4), (n/4), n);
        acomodaMatriz(c, c7, (n/4), (n/2), n);
        acomodaMatriz(c, c8, (n/4), ((3*n)/4), n);
        acomodaMatriz(c, c9, (n/2), 0, n);
        acomodaMatriz(c, c10, (n/2), (n/4), n);
        acomodaMatriz(c, c11, (n/2), (n/2), n);
        acomodaMatriz(c, c12, (n/2), ((3*n)/4), n);
        acomodaMatriz(c, c13, ((3*n)/4), 0, n);
        acomodaMatriz(c, c14, ((3*n)/4), (n/4), n);
        acomodaMatriz(c, c15, ((3*n)/4), (n/2), n);
        acomodaMatriz(c, c16, ((3*n)/4), ((3*n)/4), n);
    }
    public double checksum()
    {
        double s = 0;
        if(n == 8)
        {
            System.out.println("MATRIZ A");
            for(int i = 0; i<n; i++)
            {
                for(int j = 0; j < n; j++)
                    System.out.printf("%f \t", a[i][j]);
                System.out.printf("\n");
            }
            System.out.println("MATRIZ B");
            for(int i = 0; i<n; i++)
            {
                for(int j = 0; j < n; j++)
                    System.out.printf("%f \t", b[i][j]);
                System.out.printf("\n");
            }
            System.out.println("MATRIZ C");
            for(int i = 0; i<n; i++)
            {
                for(int j = 0; j < n; j++)
                    System.out.printf("%f \t", c[i][j]);
                System.out.printf("\n");
            }
        }
        for(int i = 0; i<c[0].length; i++)
            for(int j = 0; j < c[0].length; j++)
                s += c[i][j];
        return s;
    }
    public static class worker extends Thread
    {
        private InterfaceRMI r;
        private int n, nodo;
        public worker(int n, String ip, int nodo) throws Exception 
        {
            this.n = n;
            this.nodo = nodo;
            r = (InterfaceRMI) Naming.lookup("rmi://"+ip+"/server");
        }
        public void run()
        {
            synchronized(new Object())
            {
                try {
                    switch(nodo)
                    {
                        case 1:
                            //CALCULO DE C1
                            c1 = r.multiplicaMatrices(a1, b1, n);
                            //CALCULO DE C2
                            c2 = r.multiplicaMatrices(a1, b2, n);
                            //CALCULO DE C3
                            c3 = r.multiplicaMatrices(a1, b3, n);
                            //CALCULO DE C4
                            c4 = r.multiplicaMatrices(a1, b4, n);
                        break;
                        case 2:
                            //CALCULO DE C5
                            c5 = r.multiplicaMatrices(a2, b1, n);
                            //CALCULO DE C6
                            c6 = r.multiplicaMatrices(a2, b2, n);
                            //CALCULO DE C7
                            c7 = r.multiplicaMatrices(a2, b3, n);
                            //CALCULO DE C8
                            c8 = r.multiplicaMatrices(a2, b4, n);
                        break;
                        case 3:
                            //CALCULO DE C9
                            c9 = r.multiplicaMatrices(a3, b1, n);
                            //CALCULO DE C10
                            c10 = r.multiplicaMatrices(a3, b2, n);
                            //CALCULO DE C11
                            c11 = r.multiplicaMatrices(a3, b3, n);
                            //CALCULO DE C12
                            c12 = r.multiplicaMatrices(a3, b4, n);
                        break;
                        case 4:
                            //CALCULO DE C13
                            c13 = r.multiplicaMatrices(a4, b1, n);
                            //CALCULO DE C14
                            c14 = r.multiplicaMatrices(a4, b2, n);
                            //CALCULO DE C15
                            c15 = r.multiplicaMatrices(a4, b3, n);
                            //CALCULO DE C16
                            c16 = r.multiplicaMatrices(a4, b4, n);
                        break;
                    }
                } catch (RemoteException e) {}
            }
        }
    }
    public static void main(String[] args) throws Exception 
    {
        ClienteRMI nuevo = new ClienteRMI(Integer.parseInt(args[0]));
    }
}
