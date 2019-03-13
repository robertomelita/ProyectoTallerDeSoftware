package com.example.roberto.sistemabodeguero;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Adapter para la lista deslizable.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {

    public ArrayList<Producto> listaCategorias;
    public Map<Producto,ArrayList<Producto>> listChild;
    private Context context;
    private Button botonValidar;
    private ManejadorMapa callback;

    /**
     * @param context contexto del ActivityMain
     * @param listaProductos Lista de los productos Obtenida anteriormente
     */
    public ExpandableAdapter( Context context, ArrayList<Producto> listaProductos, ManejadorMapa callback) {

        this.callback = callback;
        this.context = context;
        this.listaCategorias = listaProductos;
        this.listChild = new HashMap<>();

        ArrayList<Producto> aux;

        for (int i = 0; i < listaProductos.size(); i++) {
            aux = new ArrayList<>();
            aux.add(listaProductos.get(i));
            listChild.put(listaProductos.get(i),aux);
            listaCategorias.get(i).getPunto().mostrarPunto();
        }
    }

    /**
     * @return Número de grupos productos
     */
    @Override
    public int getGroupCount() {

        return listaCategorias.size();
    }

    /**
     * Obtiene de productos dentro el grupo (metodo redundante pero necesario)
     * @param groupPosition Posicion del producto dentro del HashMap.
     * @return Numero del hijos dentro del Array especificado.
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    /**
     * @param groupPosition Posicion del producto dentro del HashMap.
     * @return Posicion del grupo Prodcuto en el HashMap.
     */
    @Override
    public Object getGroup(int groupPosition) {
        return listaCategorias.get(groupPosition);
    }

    /**
     * @param groupPosition Posicion del producto dentro del HashMap.
     * @param childPosition Posicion del producto dentro del grupo Producto.
     * @return Prodcuto buscado.
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listaCategorias.get(groupPosition)).get(0);
    }

    /**
     * Por defecto.
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    /**
     * Por defecto.
     * @param groupPosition Posicion del producto dentro del HashMap.
     * @param childPosition Posicion del producto dentro del grupo Producto.
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    /**
     * Por defecto.
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Se llama cada vez que uno de los productos sin expandir se muestra en pantalla.
     * @param groupPosition Posicion del producto dentro del HashMap.
     * @param isExpanded Muestra si el View esta expandido.
     * @param convertView View a modificar.
     * @param parent View Padre.
     * @return View modificado.
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(!listaCategorias.get(groupPosition).isValidado()) convertView = LayoutInflater.from(context).inflate(R.layout.producto_minimizado,null);
        else convertView = LayoutInflater.from(context).inflate(R.layout.producto_minimizado_validado,null);

        callback.onChange();

        TextView nombreProducto = convertView.findViewById(R.id.nombre_producto);
        TextView cantidadDeseada = convertView.findViewById(R.id.cantidad_deseada);
        nombreProducto.setText(listaCategorias.get(groupPosition).getNombreProducto());
        cantidadDeseada.setText(String.valueOf(listaCategorias.get(groupPosition).getCantidad()));
        /*if(isExpanded) listaCategorias.get(groupPosition).getPunto().mostrarPunto();*/
        return convertView;
    }

    /**
     * Se llama cada vez que uno de las descripciones de los productos se muestra en pantalla, como por ejemplo cuando son presionados los padres.
     * @param groupPosition Posicion del producto dentro del HashMap.
     * @param childPosition Posicion del producto dentro del grupo Producto.
     * @param isLastChild Muestra si es el ultimo Producto.
     * @param convertView View a modificar.
     * @param parent View Padre.
     * @return View modificado.
     */
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(!listaCategorias.get(groupPosition).isValidado()) convertView = LayoutInflater.from(context).inflate(R.layout.producto_maximizado_sin_validar,null);
        else convertView = LayoutInflater.from(context).inflate(R.layout.producto_maximizado_validado,null);

        callback.onChange();

        botonValidar = convertView.findViewById(R.id.boton_validar);
        TextView localizacion = convertView.findViewById(R.id.localizacion);
        TextView peso = convertView.findViewById(R.id.peso);
        TextView tamano = convertView.findViewById(R.id.tamano);
        TextView stock = convertView.findViewById(R.id.stock);

        botonValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EscanerQR.class).putExtra("codigo",listaCategorias.get(groupPosition).getCodigo());
                intent.putExtra("posicion",String.valueOf(groupPosition));
                context.startActivity(intent);
            }
        });

        String l = "Localizacion: ".concat(listaCategorias.get(groupPosition).getDescripcionUbicacion());
        localizacion.setText(l);
        String p = "Peso: ".concat(String.valueOf(listaCategorias.get(groupPosition).getPeso()));
        peso.setText(p);
        String t = "Dimensiones: "+String.valueOf(listaCategorias.get(groupPosition).getAlto())+"cm X "+
                String.valueOf(listaCategorias.get(groupPosition).getAncho())+"cm X "+
                String.valueOf(listaCategorias.get(groupPosition).getLargo());
        tamano.setText(t);
        String s = "Stock: "+String.valueOf(listaCategorias.get(groupPosition).getStock());
        stock.setText(s);
        ImageView imageView = convertView.findViewById(R.id.imagen_producto);


        if(listaCategorias.get(groupPosition).getBitmap()!=null)imageView.setImageBitmap(listaCategorias.get(groupPosition).getBitmap());
/*
        for (int i = 0; i < listaCategorias.size(); i++) {
            if (i != groupPosition) listaCategorias.get(i).getPunto().ocultarPunto();
        }
*/
        return convertView;
    }

    /**
     * Por Defecto
     * @param groupPosition Posicion del producto dentro del HashMap.
     * @param childPosition Posicion del producto dentro del grupo Producto.
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void vaciarExpandableListView(){
        for (int i = 0; i < listaCategorias.size(); i++) {
            listaCategorias.get(i).getPunto().ocultarPunto();
        }
        listaCategorias = new ArrayList<>();
        listChild = new HashMap<>();
    }
}
