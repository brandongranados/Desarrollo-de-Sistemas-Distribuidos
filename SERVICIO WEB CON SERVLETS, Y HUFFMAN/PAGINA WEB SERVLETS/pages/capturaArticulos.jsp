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
        <link rel="stylesheet" href="../css/capturaArtciulos.css">
        <script src="../js/capturaArt.js"></script>
        <script></script>
    <%
        }
    %>
    </head>
    <%
        if(session.getAttribute("email") != null){
    %>
    <body>
        <header>
            <span>captura de art&iacute;culos</span>
            <nav>
                <ul>
                    <li><a href="./capturaArticulos.jsp">pagina para capturar articulos</a></li>
                    <li><a href="./compraArticulos.jsp">pagina para buscar y comprar articulos</a></li>
                </ul>
            </nav>
        </header>
        <main>
            <form>
                <input type="text" name="nombre" maxlength="25" placeholder="nombre articulo" required>
                <textarea name="descripcion" cols="20" rows="10" maxlength="200" required placeholder="ingresa la descripci&oacute;n de tu art&iacute;culo"></textarea>
                <section>
                    <label>precio</label>
                    <input type="number" name="precio" step="0.01" min="0" required>
                </section>
                <section>
                    <label>cantidad en almacen</label>
                    <input type="number" name="cantAmacen" min="0" required>
                </section>
                <img src="" id="img">
                <input type="file" name="foto" accept="image/*">
                <button type="submit" name="submit">enviar</button>
            </form>
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