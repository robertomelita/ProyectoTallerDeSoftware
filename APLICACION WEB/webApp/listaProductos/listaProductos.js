var ant=""

function onLoadProduct() {
	response = cargaproducto();
	var json = JSON.parse(response.responseText);
	var str='<div class="ui middle aligned selection list">';

	for (var i = 0; i < json["data"].length; i++) {
		str= str + '<div class="item" onclick="test(\''+json["data"][i]["codigo"]+'\')">\
    <img class="ui avatar image" src="../image/image2.jpg">\
    <div class="content">\
      <div class="header">'+json["data"][i]["nombre_producto"]+'</div>\
    </div>\
  </div>'
	}
	str=str+"</div>"
	document.getElementById('body').innerHTML = str;

}

function onLoadProduct2() {
	str='<div ><button class="ui button" onclick="addProduct()">Añadir Producto</button></div> <div style="width: 100%; height: 95%; border: 1px solid black; overflow: hidden;" > <div style="width: 60%; height: 800px; float: left; border: 1px solid darkcyan; overflow: auto;" id="body"> </div> <div style="border: 1px solid green; overflow: hidden; width: 40%;" id="objetivo3"></div></div>'
	document.getElementById('bodyProduct').innerHTML = str;
	onLoadProduct()

}
//

function test(codigo){
	var respuesta=cargadata(codigo);
	var mjson = JSON.parse(respuesta.responseText);	
	str='<div><img src=../image/'+mjson["data"][0]["imagen"]+'></div><br><p> Nombre: '+mjson["data"][0]["nombre_producto"]+'</p><br><p> Marca: '+mjson["data"][0]["marca"]+'</p><br><p> Codigo: '+mjson["data"][0]["codigo"]+'</p><br><p> Stock: '+mjson["data"][0]["stock"]+'</p><br><p> Categoria: '+mjson["data"][0]["categoria"]+'</p><br><p> Descripcion: '+mjson["data"][0]["descripcion"]+'</p>'
	document.getElementById('objetivo3').innerHTML=str
}

function addProduct(){
	
	ant=document.getElementById('bodyProduct').innerHTML;
	str='<div><button class="circular ui icon button" onclick="Atras()"><i class="arrow alternate circle left icon"></i></button></div><br><div class="ui input"><input type="text" id="item_nombre" placeholder="Nombre Producto nuevo"></div><div class="ui input"><input type="text" id="item_codigo" placeholder="Inserte Codigo"></div><br><br><div class="ui input"><input type="text" id="item_alto" placeholder="Inserte altura"></div><div class="ui input"><input type="text" id="item_largo" placeholder="Inserte largo"></div><div class="ui input"><input type="text" id="item_ancho" placeholder="Inserte ancho"></div><br><br><div class="ui input"><input type="text" id="item_peso" placeholder="Inserte peso"></div><div class="ui input"><input type="text" id="item_marca" placeholder="Inserte marca"></div><div class="ui input"><input type="text" id="item_categoria" placeholder="Inserte categoria"></div><br><br><div class="ui input"><input type="text" id="item_stock" placeholder="Inserte stock"><input type="text" id="item_ruta" placeholder="Inserte ruta de la imagen"></div><br><br><div class="ui input"><input type="text" id="item_x" placeholder="Haga clic en el mapa"></div><div class="ui input"><input type="text" id="item_y" placeholder="Haga clic en el mapa"></div><br><br><div class="twelve wide stretched column"><img width=50%; height=400px; id="imajen" onclick="cl(event)" src="../image/MAPA2.png"></img><br><br><div class="ui input"><input type="text" id="item_descripcion" placeholder="Agregue Descripcion" style="width: 300px;"></div><dic><button class="ui button" onclick="RegistrarProducto()">Registrar</button></div>' 
	document.getElementById('bodyProduct').innerHTML = str;
}

function Atras(){
	document.getElementById('bodyProduct').innerHTML = ant;
}

function cl(event){
	var x = event.clientX
	var y = event.clientY-341
	document.getElementById('item_x').value=x
	document.getElementById('item_y').value=y
}

