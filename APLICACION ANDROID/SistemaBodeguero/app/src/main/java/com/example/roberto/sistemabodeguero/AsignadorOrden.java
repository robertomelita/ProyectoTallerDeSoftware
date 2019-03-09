package com.example.roberto.sistemabodeguero;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que se comunica con la API para obtener una orden.
 */
public class AsignadorOrden {
    private RequestQueue cola;
    private Context context;


    /**
     * @param context Context de la Main Activity (this).
     */
    public AsignadorOrden(Context context){
        this.context = context;
        cola = Volley.newRequestQueue(this.context);
    }

    /**
     * Metodo asicrono que se comunica con la API para obtener un codigo de orden.
     * @param url url de la API.
     * @param callback Callback para manejar procesos asincronos.
     */
    public void asignarOrden(final String url,final ServerCallback callback){
//        final String[] codigoOrden = new String[1];
//        final Orden[] orden = new Orden[1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"asignar_orden", null, new Response.Listener<JSONObject>() {
            String codigoOrden="";
            @Override
            public void onResponse(JSONObject response) {
                try {
                    codigoOrden = response.getString("data");
                    MainActivity.codigoOrden = codigoOrden;
                    callback.onSuccess();
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

       // return orden[0];
    }
}
