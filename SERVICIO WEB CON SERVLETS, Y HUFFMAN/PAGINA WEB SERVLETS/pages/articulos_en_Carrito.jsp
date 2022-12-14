<%@page  contentType="text/html" language="java"%>
<%@page session="true"%>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="autor: Casiano Granados Brandon Antonio">
        <meta name="description" content="Fecha 2022">
        <meta name="description" content="tarea8">
        <title>art&iacute;culos en carrito</title>
    <%
        if(session.getAttribute("email") != null){
    %>
        <link rel="stylesheet" href="../css/header.css">
        <link rel="stylesheet" href="../css/carritoCompras.css">
        <script src="../js/carritoCompras.js"></script>
        <script src="../js/borraArtCarrrito.js"></script>
    <%
        }
    %>
    </head>
    <%
        if(session.getAttribute("email") != null){
    %>
    <body>
        <header>
            <span>busqueda de art&iacute;culos</span>
            <nav>
                <ul>
                    <li><a href="./capturaArticulos.html">pagina para capturar articulos</a></li>
                    <li><a href="./compraArticulos.html">regresar al menu anterior</a></li>
                </ul>
            </nav>
        </header>
        <main>
            <table>
                <caption class="msms-y-tittles">art&iacute;culos en el carrito</caption>
                <thead>
                    <tr class="msms-y-tittles">
                        <td class="anchoFoto">foto art&iacute;culos</td>
                        <td class="anchoDescrip">descripci&oacute;n</td>
                        <td class="anchoRest">cantidad</th>
                        <td class="anchoRest">precio</td>
                        <td class="anchoRest">costo</td>
                        <td class="tdelimina" colspan="2"><img src="./img/basura.png" alt="basura"id="eliminaImagen"></td>
                    </tr>
                </thead>
                <tbody>
                    <tr id="vacio">
                        <td colspan="6" class="msms-y-tittles"><p>sin art&iacute;culos en carrito</p></td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="4" class="msms-y-tittles">total compra</td>
                    </tr>
                </tfoot>
            </table>
        </main>
    </body>
    <%
        }
        else{
    %>
    <body>
        <h1>pagina no disponible</h1>
    </body>
    <%
        }
    %>
    </html>