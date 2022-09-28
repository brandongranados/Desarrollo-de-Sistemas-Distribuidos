import java.util.ArrayList;

public class quicksort {
    public ArrayList<nodo> ordena(int pivote, ArrayList<nodo> sublista)
    {
        if(sublista.size() < 2)
            return sublista;
        ArrayList<nodo> listMenores = new ArrayList<nodo>();
        nodo puntPivote = sublista.get(pivote);
        sublista.remove(pivote);
        for(int i=sublista.size()-1; i>=0; i--)
            if(sublista.get(i).peso < puntPivote.peso)
            {
                listMenores.add(sublista.get(i));
                sublista.remove(i);
            }
        ArrayList<nodo> retorno = ordena(sublista.size()/2, sublista);
        retorno.add(puntPivote);
        retorno.addAll(ordena(listMenores.size()/2, listMenores));
        return retorno;
    }
}
