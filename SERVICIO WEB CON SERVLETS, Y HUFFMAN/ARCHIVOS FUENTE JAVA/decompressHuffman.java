import com.google.gson.Gson;

public class decompressHuffman {
    private respCompressJSON entrada;
    private nodo raiz;
    private Gson obj;
    private String infoCrud, rutaBin;
    private int recorridoCad;
    private String logBase2(int num)
    {
        if(num < 2)
            return "1";
        return logBase2(num/2)+(num%2);
    }
    private String numBinLength6(int num)
    {
        String numBin = logBase2(num);
        for(int k=numBin.length(); k < 6; k++)
            numBin = 0 + numBin;
        return numBin;
    }
    private String decompressFromBaseBin()
    {
        String codeBin = "";
        for(int i = entrada.info.length()-1; i>=0; i--)
        {
            int ascii = entrada.info.charAt(i);
            if(ascii > 64 && ascii < 91)
                codeBin = numBinLength6(ascii-65) + codeBin;
            else if(ascii > 96 && ascii < 123)
                codeBin = numBinLength6(ascii-71) + codeBin;
            else if(ascii > 47 && ascii < 58)
                codeBin = numBinLength6(ascii+4) + codeBin;
            else if(ascii == 43)
                codeBin = "111110" + codeBin;
            else
                codeBin = "111111" + codeBin;
        }
        return entrada.startInfo+codeBin;
    }
    private String recorreArbol()
    {
        String info = "";
        while(recorridoCad<rutaBin.length()-1)
            info += recorreNodoRamas(raiz);
        rutaBin = "";
        return info;
    }
    private String recorreNodoRamas(nodo puntero)
    {
        if(puntero.simbolo != '\0')
            return String.valueOf(puntero.simbolo);
        recorridoCad++;
        if(rutaBin.charAt(recorridoCad) == '1')
            return recorreNodoRamas(puntero.nodoDer);
        else
            return recorreNodoRamas(puntero.nodoIzq);
    }
    public String decompressFromBase64()
    {
        return infoCrud;
    }
    public String arbolToJSON()
    {
        return obj.toJson(raiz);
    }
    public decompressHuffman(String JSON)
    {
        obj = new Gson();
        recorridoCad = -1;
        entrada = (respCompressJSON) obj.fromJson(JSON, respCompressJSON.class);
        raiz = (nodo) obj.fromJson(entrada.arbol, nodo.class);
        rutaBin = decompressFromBaseBin();
        infoCrud = recorreArbol();
    }
    public static void main(String[] args) {
        compressHuffman uno = new compressHuffman("BRANDON ANTONIO CASIANO GRANADOS");
        decompressHuffman dos = new decompressHuffman(uno.infoCompressJSON());
        System.out.println(dos.decompressFromBase64());
    }
}
