var ant=""
function onLoad() {
	response = cargauser();
	var json = JSON.parse(response.responseText);
	var str='<div class="ui middle aligned selection list" style="height:100%">';

	for (var i = 0; i < json["data"].length; i++) {
		str= str + '<div class="item">\
    <img class="ui avatar image" src="../image/image.jpg">\
    <div class="content">\
      <div class="header">'+json["data"][i]["Usuario"]+'</div>\
    </div>\
  </div>'
	}
	str=str+"</div>"
	//str=str+"<div> <button class="ui button">Agregar Usuario</button> </div>" 
	document.getElementById('body').innerHTML = str;

}
function onLoad2() {
	str='<div><button class="ui button" onclick="adduser()">Añadir usuario</button></div><div id="body"> </div>'
	document.getElementById('b').innerHTML = str;
	onLoad()

}

function adduser(){
	
	ant=document.getElementById('b').innerHTML;
	str='<button class="ui button" onclick="Atras()">Atras</button><br><br><div class="ui input"><input type="text" id="newuser" placeholder="Nombre usuario nuevo..."></div><div class="ui input"><input type="password" id="newpass" placeholder="Contraseña.."></div><button class="ui button" onclick="Registrar()">Registrar</button>' 
	document.getElementById('b').innerHTML = str;
}

function Atras(){
	document.getElementById('b').innerHTML = ant;
}
