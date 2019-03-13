package com.example.roberto.sistemabodeguero;

import android.content.Context;
import android.widget.Toast;

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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"asignar_orden/190260860", null, new Response.Listener<JSONObject>() {
            String codigoOrden="";
            @Override
            public void onResponse(JSONObject response) {
                try {
                    codigoOrden = response.getString("data");
                    MainActivity.codigoOrden = codigoOrden;
                    callback.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "ERROR! NO SE PUDO CONECTAR",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR! NO SE PUDO CONECTAR",Toast.LENGTH_LONG).show();

            }
        });
        cola.add(request);
    }
}
