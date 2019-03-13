package com.example.roberto.sistemabodeguero;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

/**
 * Actividad Principal, es la primera clase en ser llamada, se comporta como controlador.
 */
public class MainActivity extends AppCompatActivity {
    public static Context context;
    public static String codigoOrden = "";
    public static Orden orden;
    public static int activo;
    public static boolean validado = false;

    private ExpandableListView expandableListView;
    private ExpandableAdapter adapter;
    private AsignadorOrden asignadorOrden;

    private Button botonObtenerOrden;
    private Button botonCancelar;
    public static RelativeLayout rl;
    private LinearLayout linearLayout;
    private TextView ordenPantalla;
    private ImageView mapa;

    private String url = "http://sistemanipon.ddns.net:5000/";
    private int estado = 0; //0 -> Disponible, 1 -> Trabajando en una orden


    /**
     * Se crea cuando se inicia la aplicacion, se asemeja a un constructor.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = findViewById(R.id.mapa);
        linearLayout = findViewById(R.id.lineal_mapa);
        asignadorOrden = new AsignadorOrden(this);
        context = this;
        botonObtenerOrden = findViewById(R.id.boton_obtener_orden);
        expandableListView = findViewById(R.id.expandable_list);
        botonCancelar = findViewById(R.id.boton_cancelar);
        botonCancelar.setEnabled(false);
        mapa = findViewById(R.id.imagen_mapa);
        ordenPantalla = findViewById(R.id.n_orden);
        try {
            cambiarMapa(BitmapFactory.decodeStream((InputStream) new URL("http://sistemanipon.ddns.net/image/mapa-definitivo.png").getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Atención");
                alertDialog.setMessage("¿Realmente desea CANCELAR la orden?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "SI",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                preguntarCancelar();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        botonObtenerOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(estado == 0){
                    onClickSolicitarOrden();
                }else if(estado == 1){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Atención");
                    alertDialog.setMessage("¿Realmente desea TERMINAR la orden?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "SI",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    preguntarTerminar();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }


    private void preguntarCancelar(){
        RequestQueue cola = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"cancelar_orden/"+codigoOrden, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("data").equals("success")) {
                        cancelarOrden();
                    }
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

    private void preguntarTerminar(){
        RequestQueue cola = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"terminar_orden/"+codigoOrden, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("data").equals("success")) {
                        terminarOrden();
                    }
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

    private void cancelarOrden() {
        estado = 0;
        adapter.vaciarExpandableListView();
        adapter.notifyDataSetChanged();
        botonObtenerOrden.setText(R.string.solicitar_orden);
        botonCancelar.setEnabled(false);
        ordenPantalla.setText("");
        codigoOrden="";
    }

    /**
     * Se acciona cuando hace click en el boton solicitar orden.
     */
    public void onClickSolicitarOrden() {
        asignadorOrden.asignarOrden(url, new ServerCallback(){
            @Override
            public void onSuccess() {
                ordenPantalla.setText(codigoOrden);
                orden = new Orden(codigoOrden, url, new ServerCallback(){
                    @Override
                    public void onSuccess() {
                        llenarExpandableListView();
                    }
                });
            }
        });
        botonCancelar.setEnabled(true);
    }

    public void terminarOrden(){
        estado = 0;
        adapter.vaciarExpandableListView();
        adapter.notifyDataSetChanged();
        botonObtenerOrden.setText(R.string.solicitar_orden);
        botonCancelar.setEnabled(false);
        ordenPantalla.setText("");
        codigoOrden="";
    }

    @Override
    public void onResume() {
        super.onResume();
        if(validado){
            adapter.listaCategorias.get(activo).setValidado(true);
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * Llena la ExplandableListView con los productos obtenidos de la orden.
     */
    public void llenarExpandableListView(){
        adapter = new ExpandableAdapter(this, orden.getListaProductos(), new ManejadorMapa() {
            @Override
            public void onChange() {
                int cont=0;
                for (int i = 0; i < adapter.listaCategorias.size(); i++) {
                    if(expandableListView.isGroupExpanded(i)) adapter.listaCategorias.get(i).getPunto().mostrarPunto();
                    else {
                        adapter.listaCategorias.get(i).getPunto().ocultarPunto();
                        cont++;

                    }
                }
                if (cont == adapter.listaCategorias.size()) {
                    for (int i = 0; i < adapter.listaCategorias.size(); i++) {
                        adapter.listaCategorias.get(i).getPunto().mostrarPunto();

                    }
                }
            }
        });
        expandableListView.setAdapter(adapter);
        botonObtenerOrden.setText(R.string.terminar_orden);
        estado = 1;
        ImageView shh = findViewById(R.id.imagen_mapa);
        shh.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,18));
    }

    public void cambiarMapa(Bitmap bitmap){
        mapa.setImageBitmap(bitmap);
    }
    

    /**
     * Usado para el Full Screen
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    /**
     * Usado para el Full Screen
     */
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
/*    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }*/
}

