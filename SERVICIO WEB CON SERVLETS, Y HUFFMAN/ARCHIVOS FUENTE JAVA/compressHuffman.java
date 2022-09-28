import java.util.ArrayList;

import javax.print.DocFlavor.STRING;

import com.google.gson.Gson;

public class compressHuffman{
    private quicksort ordenador;
    private nodo raiz; 
    private String infoCrud; 
    private ArrayList<nodo> listNodo, listClone;
    private Gson obj;
    private respCompressJSON salida;
    public compressHuffman(String infoCrud) 
    { 
        listNodo = new ArrayList<nodo>();
        listClone = new ArrayList<nodo>();
        ordenador = new quicksort();
        salida = new respCompressJSON();
        obj = new Gson();
        salida.startInfo = "";
        this.infoCrud = infoCrud;
        creaNodosRepeat();
        listNodo = ordenador.ordena(listNodo.size()/2, listNodo);
        listClone.addAll(listNodo);
        raiz = creaArbolHuffman(); 
        creaCodBinArray();
        char hola[] = new char[5];
        hola[i] = 'ipn';



        String dos = String.valueOf(hola)+String.valueOf(hola2);
        char mas[] =  dos.toCharArray();
    } 
    private void creaNodosRepeat() 
    { 
        String temp = infoCrud; 
        do 
        { 
            nodo newNodo = new nodo(); 
            int tam = temp.length(); 
            newNodo.simbolo = temp.charAt(0); 
            temp = temp.replaceAll(String.valueOf(newNodo.simbolo), "");
            newNodo.peso = tam-temp.length();
            listNodo.add(newNodo); 
        }while(temp.length() != 0);       
        return; 
    } 
    private nodo creaArbolHuffman() 
    { 
        if(listClone.size() < 2)
            return listClone.get(0);
        nodo puntero = new nodo();
        int tam = listClone.size()-1;
        boolean agregarList = false;
        puntero.nodoIzq = listClone.get(tam);
        puntero.nodoDer = listClone.get(tam-1);
        listClone.remove(tam);
        listClone.remove(tam-1);
        puntero.peso = puntero.nodoIzq.peso + puntero.nodoDer.peso;
        tam = listClone.size();
        for(int i=0; i<tam; i++)
            if(puntero.peso > listClone.get(i).peso)
            {
                listClone.add(i, puntero);
                agregarList = true;
                break;
            }
        if(tam == 0 || !agregarList)
            listClone.add(puntero);
        return creaArbolHuffman();
        
    } 
    private boolean recorreNodoIzq(nodo puntero, String cadenaBin)
    {
        cadenaBin += "0";
        if(puntero.simbolo != '\0')
        {
            puntero.codBinario = cadenaBin;
            return true;
        }
        else
            return recorreNodoIzq(puntero.nodoIzq, cadenaBin)&&recorreNodoDer(puntero.nodoDer, cadenaBin);
    }
    private boolean recorreNodoDer(nodo puntero, String cedenaBin)
    {
        cedenaBin += "1";
        if(puntero.simbolo != '\0')
        {
            puntero.codBinario = cedenaBin;
            return true;
        }
        else
            return recorreNodoIzq(puntero.nodoIzq, cedenaBin)&&recorreNodoDer(puntero.nodoDer, cedenaBin);
    }
    private boolean creaCodBinArray() 
    { 
        if(raiz.nodoIzq != null && raiz.nodoDer != null)
            return recorreNodoIzq(raiz.nodoIzq, "")&&recorreNodoDer(raiz.nodoDer, "");
        else if(raiz.nodoIzq != null && raiz.nodoDer == null)
            return recorreNodoIzq(raiz.nodoIzq, "");
        else if(raiz.nodoIzq == null && raiz.nodoDer != null)
            return recorreNodoDer(raiz.nodoDer, "");
        else
            return false;
    }
    public String arbolToJSON()
    {
        return obj.toJson(raiz);
    }
    public String compressToBase64()
    {
        String codeBin = "", arraySalida = "";
        int potencia = 0, numTemp = 0, startNum = 0;
        for(int i=0; i<infoCrud.length(); i++)
            for(int j=0; j<listNodo.size(); j++)
                if(infoCrud.charAt(i) == listNodo.get(j).simbolo)
                {
                    codeBin += listNodo.get(j).codBinario;
                    break;
                }
        for(int k=codeBin.length()-1; k>=0; k--)
        {
            if( (codeBin.length() - startNum) <= 6 && numTemp == 0)
            {
                salida.startInfo = codeBin.charAt(k) + salida.startInfo;
                potencia = 0;
            }
            else if(codeBin.charAt(k) == '1')
                numTemp += Math.pow(2, potencia);
            if(potencia == 5)
            {
                if(numTemp < 26)
                    arraySalida = ((char)(numTemp+65)) + arraySalida;
                else if(numTemp >= 26 && numTemp < 52)
                    arraySalida = ((char)(numTemp+71)) + arraySalida;
                else if(numTemp >= 52 && numTemp < 62)
                    arraySalida = ((char)(numTemp-4)) + arraySalida;
                else if(numTemp == 62)
                    arraySalida = "+" + arraySalida;
                else
                    arraySalida = "/" + arraySalida;
                potencia = -1;
                numTemp = 0;
            }
            potencia++;
            startNum++;
        }
        return arraySalida;
    }
    public String infoCompressJSON()
    {
        salida.arbol = arbolToJSON();
        salida.info = compressToBase64();
        return obj.toJson(salida);
    }
}