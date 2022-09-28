import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class prueba {
    public static char[] num()
    {
        char temp[] = new char[3];
        for(int i=0; i<3; i++)
        {
            int ascii = (int)Math.floor(Math.random()*(122-97)+97);
            temp[i] = (char) ascii;
        }
        return temp;
    }
    public static void main(String[] args) {
        int n = 3;
        char cadena[] = new char[n*4];
        for(int f=0; f<n*4; f++)
        {
            char temp[] = prueba.num();
            int k=0;
            int m = 0;
            k+=f;
            while(m<temp.length)
            {
                cadena[k]=temp[m];
                k++;
                m++;
            }
            f+=temp.length-1;
        }
    }
}
