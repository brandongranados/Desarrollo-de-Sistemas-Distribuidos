var cargaPagina = document.addEventListener("DOMContentLoaded", function () {
    var registro = document.forms[0].elements[0].elements["registro"];
    var inicioSesion = document.forms[1].elements[0].elements["inicio"];
    var formulario = document.forms;
    registro.addEventListener("click", registrarUsuario);
    inicioSesion.addEventListener("click", iniciarSesion);

    function iniciarSesion()
    {
        event.preventDefault();
        var correoUser = formulario[1].elements[0].elements['email'].value;
        var passwordUser = formulario[1].elements[0].elements['password'].value;
        var errorUser = "";
        //VALIDAR CREDENCIALES
        if(correoUser.length <= 0)
            errorUser += "ESCRIBA SU DIRECCION DE CORREO\n";
        if(errorUser.length <= 0)
        {
            var ob = {
                email : correoUser,
                password : passwordUser
            };
            server(JSON.stringify(ob), "login");
        }
        else
            alert(errorUser);
    }
    function registrarUsuario()
    {
        event.preventDefault();
        var nombre = formulario[0].elements[0].elements["nombre"].value;
        var email = formulario[0].elements[0].elements["email"].value;
        var password = formulario[0].elements[0].elements["password"].value;
        var error = "";
        var cant = 0;
        //VALIDACIONES DE FOMULARIO
        if(nombre.length <= 0)
            error += "EL NOMBRE NO PUEDE ESTAR VACIO\n";
        if(password.length < 8)
            error += "LA CONTRASEÑA DEBE DE SER MAYOR A 8 CARACTERES\n";
        for(a=0; a<email.length && cant <= 1; a++)
            if(email[a] == "@")
                cant ++;
        if(cant != 1)
            error += "LO QUE ESCRIBIO NO ES UN CORREO, INTENTELO DE NUEVO\n";
        if(error.length <= 0)
        {
            var obj = new Object();
            obj.nombre = nombre;
            obj.email = email;
            obj.password = password;
            server(JSON.stringify(obj), "registro");
        }
        else
            alert(error);
    }
    function server(json, servlet)
    {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:8080/antonio/"+servlet);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function () {
            xhr.status == 200 && xhr.readyState == 4 && servlet == "login" ? location.reload() : console.log("-");
            if(xhr.status == 200 && xhr.readyState == 4 && servlet == "registro")
            {
                var objetoGSON = JSON.parse(xhr.responseText); 
                alert(objetoGSON.detalle);
            }
            else 
                console.log("-");
            if(xhr.status == 400 && xhr.readyState == 4)
            {
                var objetoGSON = JSON.parse(xhr.responseText);
                alert(objetoGSON.detalle);
            }
        };
        xhr.send(json);
    }
});