package com.example.roberto.sistemabodeguero;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Clase que hace el escaneo de los códigos QR
 */
public class EscanerQR extends AppCompatActivity {
    private String codigoProducto;
    private String codigoEscaneado;
    private Context context;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private String tokenanterior;
    private TextView textView;
    private TextView cuadroColor;

    /** Método por defecto de una AppCompatActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner_qr);

        context = this;
        codigoProducto = getIntent().getStringExtra("codigo");
        cameraView = findViewById(R.id.camera_view);
        textView = findViewById(R.id.codigo_escaneado);
        cuadroColor = findViewById(R.id.cuadro_color);
        textView.setText(codigoProducto);

        escanear();
    }

    /**
     * Método que escanea un codigo QR, compara si es el codigo de la orden y se devuelve al ActivityMain.
     * Si no lo es pide que se ingrese uno nuevo.
     */
    public void escanear() {
        // creo el detector qr
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(context)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        // creo la camara
        cameraSource = new CameraSource
                .Builder(context, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        // listener de ciclo de vida de la camara
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de Android que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                     /*   if (shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA)) ;
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);*/
                    }
                    return;
                } else {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // preparo el detector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0) {

                    // obtenemos el token
                    codigoEscaneado = barcodes.valueAt(0).displayValue;

                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!codigoEscaneado.equals(tokenanterior)) {/*
                        */
                        // guardamos el ultimo token proceado
                        tokenanterior = codigoEscaneado;
                        Log.i("token", codigoEscaneado);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // Stuff that updates the UI
                                if(codigoProducto.equals(codigoEscaneado)){
                                    cuadroColor.setBackgroundColor(Color.GREEN);
                                    Toast.makeText(context, "CODIGO VALIDADO",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("message_return", "This data is returned when user click button in target activity.");
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                                else {
                                    cuadroColor.setBackgroundColor(Color.RED);
                                    Toast.makeText(context, "ERROR, NO PERTENECE AL PRODUCTO",Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        tokenanterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });
    }
}
