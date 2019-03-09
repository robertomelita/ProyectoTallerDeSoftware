UR='http://sistemanipon.ddns.net:5000'

 // window.location="webApp.html";
  //alert("ha")
function cargaproducto(){
    const url=UR+'/productos';
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, false);
    xhr.send()
    return xhr
}

function cargauser(){
    const url=UR+'/usuarios';
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, false);
    xhr.send()
    return xhr
}

function Registrar(){
    var user=document.getElementById('newuser').value
    var pass=document.getElementById('newpass').value
    const url=UR+'/register/'+user+'/'+pass;
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, false);
    xhr.send()
    onLoad2()
    return xhr
}

function cargadata(codigo){
    const url=UR+'/informacion/'+codigo
    var xxx = new XMLHttpRequest();
    xxx.open("GET", url, false);
    xxx.send()
    return xxx
}



function RegistrarProducto(){
    var code=document.getElementById('item_codigo').value
    var descripcion=document.getElementById('item_descripcion').value
    var alto=document.getElementById('item_alto').value
    var ancho=document.getElementById('item_ancho').value
    var largo=document.getElementById('item_largo').value
    var peso=document.getElementById('item_peso').value
    var marca=document.getElementById('item_marca').value
    var ubicacion=document.getElementById('item_x').value
    var nombre=document.getElementById('item_nombre').value
    var stock=document.getElementById('item_stock').value
    var ruta=document.getElementById('item_ruta').value
    var categoria=document.getElementById('item_categoria').value
    const urll=UR+'/agregarproducto/'+code+'/'+descripcion+'/'+alto+'/'+largo+'/'+ancho+'/'+peso+'/'+marca+'/'+ubicacion+'/'+nombre+'/'+stock+'/'+ruta+'/'+categoria
    var kl = new XMLHttpRequest();
    console.log(urll)
    kl.open('GET', urll, false);
    kl.send();
    onLoadProduct2()
    
}