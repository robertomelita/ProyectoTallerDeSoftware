package com.example.roberto.sistemabodeguero;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Creada para el ExpandableList View
 */
public class BlankFragment extends Fragment {


    /**
     * Se requiere vacia
     */
    public BlankFragment() {
        // Required empty public constructor
    }


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

}
