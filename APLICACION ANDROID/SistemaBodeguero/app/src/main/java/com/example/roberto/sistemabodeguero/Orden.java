package com.example.roberto.sistemabodeguero;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Contiene la lista de productos y se comunica con la API para obtenerlos.
 */
public class Orden {

    private ArrayList<Producto> listaProductos;
    private RequestQueue cola;


    /**
     * Constructor de la Clase, obtiene los datos desde la API.
     * @param codigoOrden Codigo de orden para obtener los Productos asociados.
     * @param url Url de la API.
     * @param callback  Callback para manejar procesos asincronos.
     */
    public Orden(String codigoOrden, String url, final ServerCallback callback){
        listaProductos = new ArrayList<>();
        cola = Volley.newRequestQueue( MainActivity.context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"orden/"+codigoOrden, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray respuesta = response.getJSONArray("data");
                    Log.e("#",respuesta.toString());
                    for (int i = 0; i < respuesta.length(); i++) {
                        JSONObject r = respuesta.getJSONObject(i);
                        //Random ran = new Random();
                        listaProductos.add(new Producto(
                                r.getString("codigo"),
                                r.getString("nombre_producto"),
                                r.getInt("cantidad"),
                                r.getString("descripcion"),
                                r.getString("marca"),
                                r.getString("descripcion_ubicacion"),
                                (float)r.getDouble("alto"),
                                (float)r.getDouble("ancho"),
                                (float)r.getDouble("largo"),
                                (float) r.getDouble("peso"),
                                //ran.nextInt(500),
                                //ran.nextInt(500),
                                r.getInt("x"),
                                r.getInt("y"),
                                //ran.nextInt(50)
                                r.getInt("stock"),
                                r.getString("imagen")
                                ));
                        callback.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        cola.add(request);
    }

    /**
     * @return Lista de Productos de la Orden.
     */
    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }
}
