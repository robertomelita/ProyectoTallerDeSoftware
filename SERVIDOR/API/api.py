from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
from flask_jsonpify import jsonify
from flask_cors import CORS

db_connect = create_engine('sqlite:///database.db')
app = Flask(__name__)
CORS(app, suport_credentials=True)
api = Api(app)


class AsignarOrden(Resource):
    def get(self):
        '''
        conn = db_connect.connect()
        query = conn.execute("select trackid, name, composer, unitprice from tracks;")
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
        return jsonify(result)
        '''
        result = {'data': '1'  }
        return jsonify(result)

class ObtenerPedido(Resource):
    def get(self, id_pedido):
        conn = db_connect.connect()
        query = conn.execute("\
            select\
                codigo, \
                nombre_producto, \
                tipo_producto.descripcion as descripcion,\
                alto, \
                ancho, \
                largo, \
                peso, \
                marca, \
                ubicacion.x as x, \
                ubicacion.y as y, \
                ubicacion.descripcion as descripcion_ubicacion, \
                cantidad \
            from \
                tipo_producto \
                INNER JOIN ubicacion ON tipo_producto.ubicacion = ubicacion.ID\
                INNER JOIN lista_pedido ON tipo_producto.codigo = lista_pedido.codigo_producto\
                INNER JOIN pedido ON pedido.n_pedido = lista_pedido.n_pedido\
                WHERE pedido.n_pedido=%d;"%int(id_pedido))
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
        return jsonify(result)

api.add_resource(AsignarOrden, '/asignar_orden') # Route_2
api.add_resource(ObtenerPedido, '/pedido/<id_pedido>') # Route_3

if __name__ == '__main__':
     app.run(host='0.0.0.0',port=int(5001))
