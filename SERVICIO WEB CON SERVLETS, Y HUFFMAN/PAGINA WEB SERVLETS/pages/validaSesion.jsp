<%@page  contentType="text/html" language="java"%>
<%@page session="true"%>
 <%
    if(session.getAttribute("usuario") != null)
    {
        out.write("<li><a href=\"./pages/capturaArticulos.html\">pagina para capturar articulos</a></li>");
        out.write("<li><a href=\"./pages/compraArticulos.html\">pagina para buscar y comprar articulos</a></li>");
        out.write("<li><a href=\"./pages/user.html\">"+session.getAttribute("usuario")+"</a></li>");
    }
    else
    {
        out.write("<li><a href=\"./pages/login.html\">iniciar sesi&oacute;n</a></li>");
    }
%>