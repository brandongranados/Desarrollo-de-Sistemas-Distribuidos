var cargaPagina = document.addEventListener("DOMContentLoaded", function () {
    var logout = document.getElementById("logout");

    logout.addEventListener("click", cierraSesion);

    function cierraSesion()
    {
        event.preventDefault();
        var ajax = new XMLHttpRequest();
        ajax.open("GET", "http://localhost:8080/antonio/logout");
        ajax.onreadystatechange = function () {
            ajax.status == 200 && ajax.readyState == 4 ? location.reload() : console.log("");
        };
        ajax.send();
    }
});