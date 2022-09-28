function decompressHuffman(objson)
{
    var logBase2 = function(num)
    {
        if(num < 2)
            return "1";
        return logBase2(parseInt(num/2))+(num%2);
    }
    var numBinLength6 = function(num)
    {
        var numBin = logBase2(num);
        for(k=numBin.length; k < 6; k++)
            numBin = 0 + numBin;
        return numBin;
    }
    var decompressFromBaseBin = function()
    {
        var codeBin = "";
        for(i=entrada.info.length-1; i>=0; i--)
        {
            var ascii = entrada.info.charCodeAt(i);
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
    var recorreNodoRamas = function(puntero)
    {
        if(puntero.s != null)
            return puntero.s;
        recorridoCad++;
        if(rutaBin[recorridoCad] == '1')
            return recorreNodoRamas(puntero.nDer);
        else
            return recorreNodoRamas(puntero.nIzq);
    }
    var recorreArbol = function()
    {
        var info = "";
        while(recorridoCad<(rutaBin.length-1))
            info += recorreNodoRamas(raiz);
        rutaBin = "";
        return info;
    }
    this.decompressFromBase64 = function()
    {
        return infoCrud;
    }
    this.arbolToJSON = function()
    {
        return JSON.stringify(raiz);
    }

    var recorridoCad = -1;
    var entrada = JSON.parse(objson);
    var raiz = JSON.parse(entrada.arbol);
    var rutaBin = decompressFromBaseBin();
    var infoCrud = recorreArbol();
}