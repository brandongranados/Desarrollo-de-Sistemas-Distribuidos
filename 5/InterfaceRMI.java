import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote
{
    //public double checksum(int m[][]) throws RemoteException;
    public double[][] multiplicaMatrices(double a[][], double b[][], int n) throws RemoteException;
}
