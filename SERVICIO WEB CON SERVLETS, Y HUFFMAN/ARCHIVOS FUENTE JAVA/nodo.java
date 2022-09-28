public class nodo {
    public nodo nodoIzq; 
    public nodo nodoDer; 
    public String codBinario; 
    public char simbolo; 
    public int peso; 
    public nodo() 
    { 
        nodoIzq = null; 
        nodoDer = null; 
        codBinario = null; 
        simbolo = '\0';
        peso = 0; 
    }
}