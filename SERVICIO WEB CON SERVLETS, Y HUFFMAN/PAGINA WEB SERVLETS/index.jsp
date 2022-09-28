<%@page  contentType="text/html" language="java"%>
<%@page session="true"%>
<%
    boolean validaSesion = false;
    if(session.getAttribute("email") != null)
    {validaSesion=true;}
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Casiano Granados Brandon Antonio">
    <meta name="Fecha" content="2022">
    <link rel="stylesheet" href="./css/header.css">
    <link rel="stylesheet" href="./css/index.css">
    <%
        if(!validaSesion)
        {
            out.write("<script src=\"./js/formIndex.js\"></script>");
        }
        else
        {
            out.write("<script src=\"./js/logout.js\"></script>");
        }
    %>
    <title>tarea 8</title>
</head>
<body>
    <header>
        <span>menu principal</span>
        <nav>
            <ul><%
                if(validaSesion)
                { %>
                    <%@ include file="./header.jsp" %>
               <% }
            %></ul>
        </nav>
    </header>
    <main>
        <%
            if(validaSesion)
            {
                out.write("<h1>");
                out.write("bienvenido"+session.getAttribute("nombre"));
                out.write("</h1>");
            }
            else
            {
                out.write("<section>");
                out.write("<form>");
                out.write("<fieldset>");
                out.write("<legend>registro</legend>");
                out.write("<input type=\"text\" name=\"nombre\" placeholder=\"escribe tu nombre de usuario\" maxlength=\"60\" required>");
                out.write("<input type=\"email\" name=\"email\" placeholder=\"example@example.com\" maxlength=\"100\">");
                out.write("<input type=\"password\" name=\"password\" maxlength=\"50\" minlength=\"8\" placeholder=\"password\">");
                out.write("<input type=\"submit\" name=\"registro\"value=\"registrar\">");
                out.write("</fieldset>");
                out.write("</form>");
                out.write("</section>");
                out.write("<section>");
                out.write("<form>");
                out.write("<fieldset>");
                out.write("<legend>inciar sesi&oacute;n</legend>");
                out.write("<input type=\"email\" name=\"email\" placeholder=\"example@example.com\" maxlength=\"100\">");
                out.write("<input type=\"password\" name=\"password\" maxlength=\"50\" minlength=\"8\" placeholder=\"password\">");
                out.write("<input type=\"submit\" name=\"inicio\"value=\"entrar\">");
                out.write("</fieldset>");
                out.write("</form>");
                out.write("</section>");
            }
        %>
    </main>
</body>
</html>