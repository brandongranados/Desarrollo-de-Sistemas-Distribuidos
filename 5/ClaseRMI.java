import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI
{
    protected ClaseRMI() throws RemoteException 
    {   
        super();
    }
    public double[][] multiplicaMatrices(double a[][], double b[][], int n) throws RemoteException
    {
        double c[][] = new double[n/4][n/4];
        for(int i=0; i<n/4; i++)
            for(int j=0; j<n/4; j++)
                for(int k=0; k<n; k++)
                    c[i][j] += a[i][k] * b[j][k];
        return c;
    }
}