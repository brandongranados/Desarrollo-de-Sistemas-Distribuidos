function compressHuffman(info)
{
    var creaNodosRepeat = function()
    {
        var temp = infoCrud;
        do
        {
            var newNodo = new nodo();
            var tam = temp.length;
            newNodo.s = temp.charAt(0);
            temp = temp.replaceAll(newNodo.s, "");
            newNodo.p = tam - temp.length;
            listNodo.push(newNodo);
        }while(temp.length != 0);
        return;
    }
    var recorreNodoIzq = function(puntero, cadenaBin)
    {
        cadenaBin += "0";
        if(puntero.s != null)
        {
            puntero.cBin= cadenaBin;
            return true;
        }
        else
            return recorreNodoIzq(puntero.nIzq, cadenaBin)&&recorreNodoDer(puntero.nDer, cadenaBin);
    }
    var recorreNodoDer = function(puntero, cadenaBin)
    {
        cadenaBin += "1";
        if(puntero.s != null)
        {
            puntero.cBin = cadenaBin;
            return true;
        }
        else
            return recorreNodoIzq(puntero.nIzq, cadenaBin)&&recorreNodoDer(puntero.nDer, cadenaBin);
    }
    var creaArbolHuffman = function()
    {
        if(listClone.length < 2)
            return listClone[0];
        var puntero = new nodo();
        var tam = listClone.length-1;
        var agregarList = false;
        puntero.nIzq = listClone.splice(tam, 1)[0];
        puntero.nDer = listClone.splice(tam-1, 1)[0];
        puntero.p = puntero.nIzq.p + puntero.nDer.p;
        tam = listClone.length;
        for(f=0; f<tam; f++)
            if(puntero.p > listClone[f].p)
            {
                listClone.splice(f, 0, puntero);
                agregarList = true;
                break;
            }
        if(tam == 0 || !agregarList)
            listClone.push(puntero);
        return creaArbolHuffman();
    }
    var creaCodBinArray = function()
    {
        if(raiz.nIzq != null && raiz.nDer != null)
            return recorreNodoIzq(raiz.nIzq, "")&&recorreNodoDer(raiz.nDer, "");
        else if(raiz.nIzq != null && raiz.nDer == null)
            return recorreNodoIzq(raiz.nIzq, "");
        else if(raiz.nIzq == null && raiz.nDer != null)
            return recorreNodoDer(raiz.nDer, "");
        else
            return false;
    }
    this.arbolToJSON = function()
    {
        return JSON.stringify(raiz);
    }
    this.compressToBase64 = function()
    {
        var codeBin = "";
        var arraySalida = "";
        var potencia = 0;
        var numTemp = 0;
        var startNum = 0;
        for(k=0; k<infoCrud.length; k++)
            for(m=0; m<listNodo.length; m++)
                if(infoCrud.charAt(k) == listNodo[m].s)
                {
                    codeBin += listNodo[m].cBin;
                    break;
                }
        for(o=codeBin.length-1; o>=0; o--)
        {
            if( (codeBin.length - startNum) <= 6 && numTemp == 0 )
            {
                salida.startInfo = codeBin.charAt(o) + salida.startInfo;
                potencia = 0;
            }
            else if(codeBin.charAt(o) == '1')
                numTemp += Math.pow(2, potencia);
            if(potencia == 5)
            {
                if(numTemp < 26)
                    arraySalida = String.fromCharCode(numTemp+65) + arraySalida;
                else if(numTemp >= 26 && numTemp < 52)
                    arraySalida = String.fromCharCode(numTemp+71) + arraySalida;
                else if(numTemp >= 52 && numTemp < 62)
                    arraySalida = String.fromCharCode(numTemp-4) + arraySalida;
                else if(numTemp == 62)
                    arraySalida = "+" + arraySalida;
                else
                    arraySalida = "/" + arraySalida;
                potencia = -1;
                numTemp = 0;
            }
            potencia++;
            startNum ++;
        }
        return arraySalida;
    }
    this.infoCompressJSON = function()
    {
        salida.arbol = this.arbolToJSON();
        salida.info = this.compressToBase64();
        return JSON.stringify(salida);
    }

    var listNodo = new Array();
    var listClone = new Array();
    var ordenador = new quicksort();
    var salida = new respCompressJSON();
    var infoCrud = info;
    creaNodosRepeat();
    listNodo = ordenador.ordena(listNodo.length/2, listNodo);
    listClone = listNodo.slice();
    var raiz = creaArbolHuffman();
    creaCodBinArray();
}