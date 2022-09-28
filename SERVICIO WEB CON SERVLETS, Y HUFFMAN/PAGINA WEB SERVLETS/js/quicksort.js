function quicksort()
{
    this.ordena = function(pivote, sublista)
    {
        if(sublista.length < 2)
            return sublista;
        var listMenores = new Array();
        var puntPivote = sublista.splice(pivote, 1)[0];
        for(i=sublista.length-1; i>=0; i--)
            if(sublista[i].p < puntPivote.p)
                listMenores.push(sublista.splice(i, 1)[0]);
        return this.ordena(parseInt(sublista.length/2), sublista).concat(puntPivote).concat(this.ordena(parseInt(listMenores.length/2), listMenores));
    };
}