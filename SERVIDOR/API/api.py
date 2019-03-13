from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
import json
from flask_jsonpify import jsonify
from flask_cors import CORS
from random import randint

db_connect = create_engine('sqlite:///BD_nipon')
app = Flask(__name__)
CORS(app, suport_credentials=True)
api = Api(app)


class ModificarProducto(Resource):
    def get(self, codigo, descripcion, alto, largo, ancho, peso, marca, ubicacion, nombre, stock, ruta, categoria):
        conn = db_connect.connect()
        query = conn.execute("\
            update tipo_producto SET\
            descripcion="+"\""+descripcion+"\""+", \
            alto="+alto+", \
            largo="+largo+", \
            ancho="+ancho+", \
            peso="+peso+", \
            marca="+"\""+marca+"\""+",\
            ubicacion="+ubicacion+",\
            nombre_producto="+"\""+nombre+"\""+", \
            stock="+stock+", \
            imagen="+"\""+ruta+"\""+", \
            categoria="+"\""+categoria+"\""+" where codigo="+"\""+codigo+"\""+"")

class deleteProduct(Resource):
    def get(self,code):
        conn=db_connect.connect()
        query=conn.execute("delete from tipo_producto where codigo="+"\""+code+"\"")

class Informacion(Resource):
    def get(self,codigo):
        conn=db_connect.connect()
        query=conn.execute("select * from tipo_producto where codigo="+"\""+codigo+"\"")
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
#        print(result)
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

class CreadorOrdenes(Resource):
    def get(self):
        conn=db_connect.connect()
        query=conn.execute("select max(n_orden)+1 as max from orden")
        result = ({'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]})
        ncode=result['data'][0]['max']
        ncode=str(ncode)
        conn.execute("insert into orden values("+"\""+ncode+"\""+",0,0,0,0)")
        r=randint(5, 15)
        cant=randint(1,5)
        cant=str(cant)
        r=str(r)
        query2=conn.execute("select codigo from tipo_producto order by random() limit "+r)
        results = ({'data': [dict(zip(tuple (query2.keys()) ,i)) for i in query2    .cursor]})

        for i in range(0,int(r)):
            conn.execute("insert into lista_orden values("+"\""+ncode+"\""+","+"\""+results['data'][i]['codigo']+"\""+","+"\""+cant+"\""+")")
            cant=str(randint(1,5))
        return jsonify(results)

class AsignarOrden(Resource):
    def get(self,id_bodeguero):
        conn = db_connect.connect()
        query = conn.execute("select n_orden,asignada,realizada from orden where (asignada=0 and realizada=0)")
        result = ({'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]})
        result = result['data'][0]['n_orden']
        conn.execute("UPDATE orden SET asignada = 1,rut_bodeguero="+str(id_bodeguero)+" WHERE n_orden = "+str(result))
        return jsonify({'data': str(result)})

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
        for i in range(0,len(result['data'])):
            s = conn.execute("SELECT cantidad FROM lista_orden WHERE codigo_producto = \""+str(result['data'][i]['codigo'])+"\"")
            s = {'data': [dict(zip(tuple (s.keys()) ,i)) for i in s.cursor]}
            conn.execute("UPDATE tipo_producto SET stock = "+str(result['data'][i]['stock']-s['data'][0]['cantidad'])+" WHERE codigo = \"" +str(result['data'][i]['codigo'])+"\"")
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

class TerminarOrden(Resource):
    def get(self, id_orden):
        conn = db_connect.connect()
        conn.execute("UPDATE orden SET realizada=1 WHERE n_orden= "+str(id_orden))
        return jsonify({'data':'success'})


class CancelarOrden(Resource):
    def get(self, id_orden):
        conn = db_connect.connect()

        query = conn.execute("\
            select\
                codigo, \
                cantidad, \
                stock\
            from \
                tipo_producto \
                INNER JOIN lista_orden ON tipo_producto.codigo = lista_orden.codigo_producto\
                INNER JOIN orden ON orden.n_orden = lista_orden.n_orden\
                WHERE orden.n_orden=%d;"%int(id_orden))
        result = {'data': [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]}
        for i in range(0,len(result['data'])):
            s = conn.execute("SELECT cantidad FROM lista_orden WHERE codigo_producto = \""+str(result['data'][i]['codigo'])+"\"")
            s = {'data': [dict(zip(tuple (s.keys()) ,i)) for i in s.cursor]}
            conn.execute("UPDATE tipo_producto SET stock = "+str(result['data'][i]['stock']+s['data'][0]['cantidad'])+" WHERE codigo = \"" +str(result['data'][i]['codigo'])+"\"")

        conn.execute("UPDATE orden SET asignada=0, realizada=0 WHERE n_orden= "+str(id_orden))
        return jsonify({'data':'success'})



api.add_resource(ValidarUsuario, '/validate/<user>/<password>') #Route1
api.add_resource(AsignarOrden, '/asignar_orden/<id_bodeguero>') # Route_2
api.add_resource(ObtenerOrden, '/orden/<id_orden>') # Route_3
api.add_resource(ListaProductos, '/productos') #route 4
api.add_resource(ListaUsuarios, '/usuarios') #route 5
api.add_resource(AgregarProducto,'/agregarproducto/<codigo>/<descripcion>/<alto>/<largo>/<ancho>/<peso>/<marca>/<ubicacion>/<nombre>/<stock>/<ruta>/<categoria>')
api.add_resource(RegistrarUsuario, '/register/<user>/<password>') #route 5
api.add_resource(Informacion,'/informacion/<codigo>')
api.add_resource(CreadorOrdenes,'/verete')
api.add_resource(deleteProduct,'/delete/<code>')
api.add_resource(ModificarProducto,'/ModificarProducto/<codigo>/<descripcion>/<alto>/<largo>/<ancho>/<peso>/<marca>/<ubicacion>/<nombre>/<stock>/<ruta>/<categoria>')
api.add_resource(CancelarOrden,'/cancelar_orden/<id_orden>')
api.add_resource(TerminarOrden,'/terminar_orden/<id_orden>')


if __name__ == '__main__':
     #app.run(ssl_context='adhoc')
     app.run(host='0.0.0.0')
