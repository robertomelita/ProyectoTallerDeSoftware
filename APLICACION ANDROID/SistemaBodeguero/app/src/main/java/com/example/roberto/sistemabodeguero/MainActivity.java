package com.example.roberto.sistemabodeguero;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Actividad Principal, es la primera clase en ser llamada, se comporta como controlador.
 */
public class MainActivity extends AppCompatActivity {
    public static Context context;
    public static String codigoOrden = "";
    public static Orden orden;

    private ExpandableListView expandableListView;
    private ExpandableAdapter adapter;
    private AsignadorOrden asignadorOrden;

    private Button botonObtenerOrden;
    public static RelativeLayout rl;

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
        asignadorOrden = new AsignadorOrden(this);
        context = this;
        botonObtenerOrden = findViewById(R.id.boton_obtener_orden);
        expandableListView = findViewById(R.id.expandable_list);
        botonObtenerOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(estado == 0){
                    onClickSolicitarOrden();
                }else if(estado == 1){
                    estado = 0;
                    expandableListView.removeAllViewsInLayout();
                    for (int i = 0; i < adapter.listaCategorias.size(); i++) {
                        adapter.listaCategorias.get(i).getPunto().ocultarPunto();

                    }
                    botonObtenerOrden.setText(R.string.solicitar_orden);
                }
            }
        });
    }

    /**
     * Se acciona cuando hace click en el boton solicitar orden.
     */
    public void onClickSolicitarOrden() {
        asignadorOrden.asignarOrden(url, new ServerCallback(){
            @Override
            public void onSuccess() {
                orden = new Orden(codigoOrden, url, new ServerCallback(){
                    @Override
                    public void onSuccess() {
                        llenarExpandableListView();
                    }
                });
            }
        });
    }


    /**
     * Llena la ExplandableListView con los productos obtenidos de la orden.
     */
    public void llenarExpandableListView(){
        adapter = new ExpandableAdapter(this, orden.getListaProductos());
        expandableListView.setAdapter(adapter);
        botonObtenerOrden.setText(R.string.terminar_orden);
        estado = 1;
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

