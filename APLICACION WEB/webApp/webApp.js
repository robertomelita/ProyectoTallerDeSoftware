var paginaActual = 0;
var botones = ["listaProductos","registrarProducto","eliminarProducto","usuarios"]

function listaProductos() {
    if(paginaActual!=0){
        document.getElementById(botones[paginaActual]).className = "teal item"
        document.getElementById("listaProductos").className = "teal active item"
        paginaActual = 0
        document.getElementById("objetivo").src = "listaProductos/listaProductos.html";
    }
}

function registrarProducto() {
    if(paginaActual!=1){
        document.getElementById(botones[paginaActual]).className = "teal item"
        document.getElementById("registrarProducto").className = "teal active item"
        paginaActual = 1
        document.getElementById("objetivo").src = "registrarProducto/registrarProducto.html";
    }
}

function eliminarProducto() {
    if(paginaActual!=2){
        document.getElementById(botones[paginaActual]).className = "teal item"
        document.getElementById("eliminarProducto").className = "teal active item"
        paginaActual = 2
        document.getElementById("objetivo").src = "eliminarProducto/eliminarProducto.html";
    }
}

function usuarios() {
    if(paginaActual!=3){
        document.getElementById(botones[paginaActual]).className = "teal item"
        document.getElementById("usuarios").className = "teal active item"
        paginaActual = 3
        document.getElementById("objetivo").src = "usuarios/usuarios.html";
    }
}