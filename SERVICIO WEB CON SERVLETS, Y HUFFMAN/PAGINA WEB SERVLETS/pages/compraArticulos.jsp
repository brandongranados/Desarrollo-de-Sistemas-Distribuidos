<%@page  contentType="text/html" language="java"%>
<%@page session="true"%>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="author" content="Casiano Granados Brandon Antonio">
        <meta name="Fecha" content="2022">
        <title>tarea 8</title>
    <%
        if(session.getAttribute("email") != null){
    %>
        <link rel="stylesheet" href="../css/header.css">
        <link rel="stylesheet" href="../css/compraArticulos.css">
        <script src="../js/compraArt.js"></script>
        <script src="../js/compArtAddCar.js"></script>
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
                    <li><a href="./capturaArticulos.jsp">pagina para capturar articulos</a></li>
                    <li><a href="./compraArticulos.jsp">pagina para buscar y comprar articulos</a></li>
                    <li><a href="./articulos_en_Carrito.jsp">carrito de compra</a></li>
                </ul>
            </nav>
            <form>
                <label>descripci&oacute;n</label>
                <input type="search" name="busqueda" required>
                <button type="submit" name="buscar">buscar</button>
            </form>
        </header>
        <main>
            <span id="msmRes">sin resultados</span>
            <section></section>
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