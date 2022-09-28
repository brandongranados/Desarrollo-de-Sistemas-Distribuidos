import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class serverMultiHilos {
    static String host[];
    static int puertos[], numNodos, nodo;
    static double relojLogico;
    static Object objeto;
    static int recursoPeticion, recursoPoder;
    static ArrayList<Integer> cola;
    public static class relojLogico extends Thread
    {
        public relojLogico()
        { 
            objeto = new Object();
            synchronized(objeto) { relojLogico = 0; }
        }
        public void run()
        {
            while(true)
                synchronized(objeto)
                {
                    long inicio = System.currentTimeMillis();
                    switch(nodo)
                    {
                        case 0:
                            relojLogico += 4;
                        break;
                        case 1:
                            relojLogico += 5;
                        break;
                        case 2:
                            relojLogico += 6;
                        break;
                    }
                    long fin = System.currentTimeMillis();
                    try {
                        Thread.sleep((1000-(fin-inicio))+(fin-inicio));
                    } catch (InterruptedException e) {}
                }
        }
    }
    public static class worker extends Thread
    {
        static boolean mensajeOK;
        private Socket workerSocket;
        private double tiempoRecibido;
        private int nodoRealizoPeticion, idRecursoSolicitado;
        public worker(Socket workerSocket)
        {
            this.workerSocket = workerSocket;
        }
        public void run()
        {
            try {
                DataInputStream entrada = new DataInputStream(workerSocket.getInputStream());
                DataOutputStream salida = new DataOutputStream(workerSocket.getOutputStream());
                synchronized(objeto)
                {
                    idRecursoSolicitado = entrada.readInt();
                    nodoRealizoPeticion = entrada.readInt();
                    tiempoRecibido = entrada.readDouble();
                    if(recursoPoder == -10)
                    {
                        mensajeOK = true;
                        if(cola.get(0) == nodoRealizoPeticion)
                            cola.remove(0);
                        else
                            mensajeOK = false;
                        if(cola.size() == 0)
                            recursoPoder = -1;
                    }
                    else if(nodo == nodoRealizoPeticion || nodo != nodoRealizoPeticion)
                        if(nodo == nodoRealizoPeticion)
                            mensajeOK = true;
                        else
                            mensajeOK = false;
                    else if(recursoPoder == idRecursoSolicitado)
                    {
                        cola.add(nodoRealizoPeticion);
                        mensajeOK = false;
                    }
                    else if(idRecursoSolicitado != recursoPeticion)
                        mensajeOK = true;
                    else if(idRecursoSolicitado == recursoPeticion)
                        if(tiempoRecibido < relojLogico)
                            mensajeOK = true;
                        else if(tiempoRecibido > relojLogico)
                        {
                            cola.add(nodoRealizoPeticion);
                            mensajeOK = false;
                        }
                        else //SON IGUALES
                            if(nodo < nodoRealizoPeticion)
                            {
                                cola.add(nodoRealizoPeticion);
                                mensajeOK = false;
                            }
                            else//NODO PETICION ES MAYOR A NODO ACTUAL
                                mensajeOK = true;
                    else
                        mensajeOK = false;
                    salida.writeBoolean(mensajeOK);
                }
                workerSocket.close();
            } catch (Exception e) {}
        }
    }
    public static class servidor extends Thread//CLASE PARA SERVIDOR
    {
        public void run()
        {
            try {
                ServerSocket socketServidor = new ServerSocket(puertos[nodo]);
                while(true)
                {
                    Socket esperaConexSocket = socketServidor.accept();
                    worker nuevoWorker = new worker(esperaConexSocket);
                    nuevoWorker.start();
                }
            } catch (IOException e) {}
        }        
    }
    public static boolean enviaMensajeCliente(String host, int puerto)
    {
        boolean respuesta;
        while(true)
            try {
                Socket conexion = new Socket(host, puerto);
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                synchronized(objeto)
                {
                    salida.writeInt(recursoPeticion);
                    salida.writeInt(nodo);
                    salida.writeDouble(relojLogico); 
                }
                respuesta = entrada.readBoolean();
                conexion.close();
                Thread.sleep(100);
                break;
            } catch (Exception e) 
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {}
            }
        return respuesta;
    }
    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        System.out.println("NUMERO DE NODO ACTUAL");
        nodo = leer.nextInt();//NODO ACTUAL
        System.out.println("cANTIDAD DE NODOS A ABRIR");
        numNodos = leer.nextInt();
        host = new String[numNodos];
        puertos = new int[numNodos];
        cola = new ArrayList<Integer>(numNodos);
        System.out.println("INGRESA LAS IP:PUERTO DE LOS NODOS EJEMPLO : 192.168.1.254:50000");
        for(int i=0; i<numNodos; i++)
            host[i] = leer.next();
        for(int i=0; i<numNodos; i++)
            puertos[i] = Integer.parseInt(host[i].substring(host[i].indexOf(":")+1, host[i].length()));
        System.out.println("INGRESA EL NUMERO DE NODO AL QUE DESEAS ADQUIRIR EL RECURSO");
        recursoPeticion = leer.nextInt();
        leer.close();
        //COMODIN PARA LIBERAR RECURSO INICIADO EN FALSO
        recursoPoder = -1;
        //INICIO DEL SERVIDOR
        servidor nuevoServidor = new servidor();
        nuevoServidor.start();
        //INICIO DE RELOJ LOGICO
        relojLogico relojNodo = new relojLogico();
        relojNodo.start();
        //CODIGO DE CLIENTE Y PETICIONES
        int cantOKrecibidos=0;
        while(true)
        {
            for(int i=0; i<numNodos; i++)
            if(enviaMensajeCliente(host[i].substring(0, host[i].indexOf(":")-1), puertos[i]))
                cantOKrecibidos++;
            synchronized(objeto)
            {
                if(cantOKrecibidos == numNodos)
                    recursoPoder = recursoPeticion;
                //CODIGO EN DONDE SE ADQUIERE EL RECURSO
                System.out.print("adquiri el recurso lo voy a liberar");
                //FIN DE ADQUISICION
                //LIBERACION DE RECURSO SACAR NODOS DEL ARRAYLIST AQUI
                recursoPoder = -10;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}   
        }
    }
    //REVISAR PARA CONCLUIR
}
