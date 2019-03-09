from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
import json
from flask_jsonpify import jsonify
from flask_cors import CORS

db_connect = create_engine('sqlite:///BD_nipon')
app = Flask(__name__)
CORS(app, suport_credentials=True)
api = Api(app)
'''
@app.route("/")
def holamundo():
    return render_template("index.html")

class WebApp(Resource):
    def get(self):
        return render_template("index.html")
'''
class Informacion(Resource):
    def get(self,codigo):
        conn=db_connect.connect()
        query=conn.execute("select * from tipo_producto where codigo="+"\""+codigo+"\"")
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
        print(result)
        return jsonify(result)


class RegistrarUsuario(Resource):
    def get(self,user,password):
        conn=db_connect.connect()
        conn.execute("insert into login(Usuario,Password)values("+"\""+user+"\""+","+"\""+password+"\""+")")
        return "success"


class ListaUsuarios(Resource):
    def get(self):
        conn=db_connect.connect()
        query=conn.execute("select Usuario from login")
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
        return jsonify(result)

class ListaProductos(Resource):
    def get(self):
        conn=db_connect.connect()
        query=conn.execute("select nombre_producto,codigo from tipo_producto")
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
        return jsonify(result)

class ValidarUsuario(Resource):
    def get(self,user,password):
        conn=db_connect.connect()
        query=conn.execute("select Usuario from login where Usuario="+"\""+user+"\""+ " AND password=" + "\""+password+"\"" )
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
        if len(result['data'])!=0:
            return jsonify({'data':'true'})
        else:
            return jsonify({'data':'false'})

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

class ObtenerOrden(Resource):
    def get(self, id_orden):
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
                cantidad, \
                imagen,\
                stock\
            from \
                tipo_producto \
                INNER JOIN ubicacion ON tipo_producto.ubicacion = ubicacion.ID\
                INNER JOIN lista_orden ON tipo_producto.codigo = lista_orden.codigo_producto\
                INNER JOIN orden ON orden.n_orden = lista_orden.n_orden\
                WHERE orden.n_orden=%d;"%int(id_orden))
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
        return jsonify(result)

class AgregarProducto(Resource):
    def get(self, codigo, descripcion, alto, largo, ancho, peso, marca, ubicacion, nombre, stock, ruta, categoria):
        conn = db_connect.connect()
        query = conn.execute("\
            insert into tipo_producto values(\
            "+"\""+codigo+"\""+", \
            "+"\""+descripcion+"\""+", \
            "+alto+", \
            "+largo+", \
            "+ancho+", \
            "+peso+", \
            "+"\""+marca+"\""+",\
            "+ubicacion+",\
            "+"\""+nombre+"\""+", \
            "+stock+", \
            "+"\""+ruta+"\""+", \
            "+"\""+categoria+"\""+")")

api.add_resource(ValidarUsuario, '/validate/<user>/<password>') #Route1
api.add_resource(AsignarOrden, '/asignar_orden') # Route_2

api.add_resource(ObtenerOrden, '/orden/<id_orden>') # Route_3
api.add_resource(ListaProductos, '/productos') #route 4
api.add_resource(ListaUsuarios, '/usuarios') #route 5
api.add_resource(AgregarProducto,'/agregarproducto/<codigo>/<descripcion>/<alto>/<largo>/<ancho>/<peso>/<marca>/<ubicacion>/<nombre>/<stock>/<ruta>/<categoria>')
api.add_resource(RegistrarUsuario, '/register/<user>/<password>') #route 5
api.add_resource(Informacion,'/informacion/<codigo>')


if __name__ == '__main__':
     #app.run(ssl_context='adhoc')
     app.run(host='0.0.0.0')
