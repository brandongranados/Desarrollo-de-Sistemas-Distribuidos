public class multMatrizCache {
    static int n =  1000;
    static int[][] a = new int[n][n];
    static int[][] b = new int[n][n];
    static int[][] c = new int[n][n];
    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
            {
                a[i][j]=2*i-j;
                b[i][j]=i+2*j;
                c[i][j]=0;
            }
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
            {
                int x = b[i][j];
                b[i][j]=b[j][i];
                b[i][j]=x;
            }
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                for(int k=0; k<n; k++)
                    c[i][j]+=a[i][k]*b[j][k];
        long t2 = System.currentTimeMillis();
        System.out.println("tiempo :"+(t2-t1)+"ms");
    }
}
