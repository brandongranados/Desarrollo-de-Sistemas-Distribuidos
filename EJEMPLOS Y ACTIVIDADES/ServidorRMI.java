import java.rmi.Naming;

public class ServidorRMI {
    public static void main(String[] args) throws Exception
    {
        String url = "rmi://localhost/prueba";
        ClaseRMI obj = new ClaseRMI();
        //REGISTRA LA INSTANCIA EN EL RMIREGISTRY
        Naming.rebind(url, obj);
    }
}
