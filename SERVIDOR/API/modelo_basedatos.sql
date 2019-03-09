CREATE TABLE login(
	Usuario VARCHAR[15] PRIMARY KEY,
	Password VARCHAR[15]

);
INSERT INTO login(Usuario,Password) VALUES ("admin","nipon");

CREATE TABLE bodeguero (
	RUT VARCHAR[15] PRIMARY KEY, 
	nombre TEXT, 
	apellido TEXT, 
	nacimiento DATE
);
CREATE TABLE ubicacion(
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	x INTEGER,
	y INTEGER,
	descripcion TEXT
);
CREATE TABLE cliente(
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	nombre_local TEXT,
	direccion TEXT
);

CREATE TABLE tipo_producto(
	codigo VARCHAR[40] PRIMARY KEY,
	descripcion TEXT,
	alto FLOAT,
	ancho FLOAT,
	largo FLOAT,
	peso FLOAT,
	marca TEXT,
	ubicacion INTEGER,
	nombre_producto TEXT,
	stock INTEGER,
        imagen TEXT,
        categoria TEXT,
	FOREIGN KEY (ubicacion) REFERENCES ubicacion(ID)
);
CREATE TABLE orden(
	n_orden INTEGER PRIMARY KEY,
	rut_bodeguero VARCHAR[15],
	id_cliente INTEGER,
	asignada BOOLEAN DEFAULT 0 CHECK (asignada IN (0,1)),
	realizada BOOLEAN DEFAULT 0 CHECK (realizada IN (0,1)),
	FOREIGN KEY (rut_bodeguero) REFERENCES bodeguero(RUT),
	FOREIGN KEY (id_cliente) REFERENCES cliente(ID)
	
);
CREATE TABLE lista_orden(
	n_orden INTEGER,
	codigo_producto VARCHAR[40],
	cantidad INTEGER,
	PRIMARY KEY(n_orden, codigo_producto),
	FOREIGN KEY(n_orden) REFERENCES orden(n_orden) ON DELETE CASCADE ON UPDATE NO ACTION,
	FOREIGN KEY (codigo_producto) REFERENCES tipo_producto(codigo) ON DELETE CASCADE ON UPDATE NO ACTION
);

QUERY:
select 
	codigo, 
	nombre_producto, 
	alto, 
	ancho, 
	largo, 
	peso, 
	marca, 
	ubicacion.x as x, 
	ubicacion.y as y, 
	tipo_producto.descripcion as descripcion, 
	ubicacion.descripcion as descripcion_ubicacion,
	cantidad
from 
	tipo_producto 
	INNER JOIN ubicacion ON tipo_producto.ubicacion = ubicacion.ID
	INNER JOIN lista_pedido ON tipo_producto.codigo = lista_pedido.codigo_producto
	INNER JOIN pedido ON pedido.n_pedido = lista_pedido.n_pedido
	WHERE pedido.n_pedido= [INSERTAR NUMERO DE PEDIDO]
;
