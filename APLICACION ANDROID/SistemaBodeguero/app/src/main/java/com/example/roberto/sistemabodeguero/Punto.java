package com.example.roberto.sistemabodeguero;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Clase que maneja la visualizacion del producto en el mapa.
 */
public class Punto{
    private ImageView punto;
    private int x;
    private int y;
    private Context context = MainActivity.context;

    /**
     * @param x Posicion en X en el mapa.
     * @param y Posicion en Y en el mapa.
     */
    public Punto(int x, int y) {
        this.x = x;
        this.y = y;
        punto = new ImageView(context);
    }

    /**
     * Muestra el Producto en el mapa.
     */
    public void mostrarPunto(){

        punto.setImageResource(R.drawable.rojo);
        punto.setLayoutParams(new LinearLayout.LayoutParams(45, 68));
        punto.setX(this.x);
        punto.setY(this.y);
        if(punto.getParent() != null) ((ViewGroup)punto.getParent()).removeView(punto);
        MainActivity.rl.addView(punto);
    }

    /**
     * Oculta el Producto en el mapa.
     */
    public void ocultarPunto(){
        MainActivity.rl.removeView(punto);
    }
}
