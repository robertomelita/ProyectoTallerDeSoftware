UR='http://sistemanipon.ddns.net:5000'
ucode=""
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
    ucode=codigo
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

function ModificarProducto(){
    if (document.getElementById('item_descripcion1').value==""){
        var descripcion1=document.getElementById('item_descripcion1').placeholder
    }
    else{
        var descripcion1=document.getElementById('item_descripcion1').value
    }
    
    if (document.getElementById('item_alto1').value==""){
        var alto1=document.getElementById('item_alto1').placeholder
    }
    else{
        var alto1=document.getElementById('item_alto1').value
    }
    if (document.getElementById('item_ancho1').value==""){
        var ancho1=document.getElementById('item_ancho1').placeholder
    }
    else{
        var ancho1=document.getElementById('item_ancho1').value
    }
    if (document.getElementById('item_largo1').value==""){
        var largo1=document.getElementById('item_largo1').placeholder
    }
    else{
        var largo1=document.getElementById('item_largo1').value
    }
    if (document.getElementById('item_peso1').value==""){
        var peso1=document.getElementById('item_peso1').placeholder
    }
    else{
        var peso1=document.getElementById('item_largo1').value
    }

    if (document.getElementById('item_marca1').value==""){
        var marca1=document.getElementById('item_marca1').placeholder
    }
    else{
        var marca1=document.getElementById('item_marca1').value
    }

    if (document.getElementById('item_nombre1').value==""){
        var nombre1=document.getElementById('item_nombre1').placeholder
    }
    else{
        var nombre1=document.getElementById('item_nombre1').value
    }
    
    var ubicacion1=0

    if (document.getElementById('item_stock1').value==""){
        var stock1=document.getElementById('item_stock1').placeholder
    }
    else{
        var stock1=document.getElementById('item_stock1').value
    }
    
    if (document.getElementById('item_ruta1').value==""){
        var ruta1=document.getElementById('item_ruta1').placeholder
    }
    else{
        var ruta1=document.getElementById('item_ruta1').value
    }

    if (document.getElementById('item_categoria1').value==""){
        var categoria1=document.getElementById('item_categoria1').placeholder
    }
    else{
        var categoria1=document.getElementById('item_categoria1').value
    }
   
    const urll=UR+'/ModificarProducto/'+ucode+'/'+descripcion1+'/'+alto1+'/'+largo1+'/'+ancho1+'/'+peso1+'/'+marca1+'/'+ubicacion1+'/'+nombre1+'/'+stock1+'/'+ruta1+'/'+categoria1
    var kl = new XMLHttpRequest();
    console.log(urll)
    kl.open('GET', urll, false);
    kl.send();
    onLoadProduct2()
    
}