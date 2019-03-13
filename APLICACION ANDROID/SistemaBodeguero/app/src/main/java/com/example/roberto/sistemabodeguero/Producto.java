package com.example.roberto.sistemabodeguero;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Clase con todas las características de los productos.
 */
public class Producto {
    public Boolean primera = true;
    private String codigo;
    private String nombreProducto;
    private String descripcion;
    private String marca;
    private String descripcionUbicacion;
    private float alto;
    private float ancho;
    private float largo;
    private float peso;
    private int cantidad;
    private int stock;
    private Punto punto;
    private String imagen;
    private Bitmap bitmap;
    private boolean validado;




    /**
     * Constructor que inicaliza todos los atributos.
     * @param codigo Codigo del Producto.
     * @param nombreProducto Nombre del Producto.
     * @param cantidad  Cantidad requerida.
     * @param descripcion Descripcion del Producto.
     * @param marca Marca del Producto.
     * @param descripcionUbicacion Descripcion de la ubicacion.
     * @param alto Alto.
     * @param ancho Ancho.
     * @param largo Largo.
     * @param peso Peso.
     * @param x Posicion en X en el mapa.
     * @param y Posicion en Y en el mapa.
     * @param stock Stock del Producto.
     */
    public Producto(String codigo
            , String nombreProducto
            , int cantidad
            , String descripcion
            , String marca
            , String descripcionUbicacion
            , float alto
            , float ancho
            , float largo
            , float peso
            , int x
            , int y
            , int stock
            , final String imagen
            ){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://sistemanipon.ddns.net/image/"+ imagen).getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                    bitmap = null;
                }
            }
        });

        this.setCodigo(codigo);
        this.setNombreProducto(nombreProducto);
        this.setCantidad(cantidad);
        this.setDescripcion(descripcion);
        this.setMarca(marca);
        this.setDescripcionUbicacion(descripcionUbicacion);
        this.setAlto(alto);
        this.setAncho(ancho);
        this.setLargo(largo);
        this.setPeso(peso);
        punto = new Punto(x,y);
        this.setStock(stock);
        this.setImagen(imagen);
        validado = false;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcionUbicacion() {
        return descripcionUbicacion;
    }

    public void setDescripcionUbicacion(String descripcionUbicacion) {
        this.descripcionUbicacion = descripcionUbicacion;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public float getLargo() {
        return largo;
    }

    public void setLargo(float largo) {
        this.largo = largo;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public Punto getPunto(){
        return this.punto;
    }
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public boolean isValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }
}
