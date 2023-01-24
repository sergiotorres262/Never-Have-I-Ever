package com.example.yonunca;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Yonunca extends AppCompatActivity {
    private static final String TAG = "MiActivity";
    private TextView txtFrase;
    private TextView txtAutor;
    private ImageButton next;
    private ImageButton back;
    private int fraseActual = 0;
    private ImageButton share;
    private ImageView imgfrase;
    private List<Frases> frases = new ArrayList<Frases>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yonunca);
        imgfrase = findViewById(R.id.imagenfrase);
        txtAutor = findViewById(R.id.autor);
        txtFrase = findViewById(R.id.frase);
        next = findViewById(R.id.next);
        frases = BancoFrases.frases();
        back = findViewById(R.id.back);
        share = findViewById(R.id.share);
        ImageButton redes = findViewById(R.id.redes);


        txtFrase.setText(BancoFrases.frases().get(0).getFrase());
        txtAutor.setText(BancoFrases.frases().get(0).getAutor());


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteFrase();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent volver = new Intent(Yonunca.this, MainActivity.class);
                startActivity(volver);
            }
        });

        redes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.twitter.com/Sergitest_");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (!directorio.exists()) {
                    // Crea el directorio y todos los directorios padre que no existan
                    directorio.mkdirs();
                }
                if(!CheckPermissions()){
                    return;
                }else{
                    sendScreenShot();
                }


            }
        });


    }

    private void sendScreenShot() {
        // Obtén una referencia al RelativeLayout
        RelativeLayout layout = findViewById(R.id.vistaEntera);

        if (layout != null) {
            // Habilita el cache de dibujo del layout
            layout.setDrawingCacheEnabled(true);

            // Obtiene la imagen del cache de dibujo
            Bitmap imagen = layout.getDrawingCache();

            if (imagen != null) {
                // Crea una copia de la imagen
                Bitmap imagenCopia = imagen.copy(Bitmap.Config.ARGB_8888, false);

                // Deshabilita el cache de dibujo
                layout.setDrawingCacheEnabled(false);

                // Obtén el directorio público de imágenes
                File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                // Obtiene la fecha y hora actual
                Date date = new Date();

                // Convierte la fecha y hora a una cadena de texto
                String dateString = date.toString();

                // Reemplaza los espacios con guiones bajos
                String fileName = dateString.replace(" ", "_");

                // Elimina los caracteres no alfanuméricos
                fileName = fileName.replaceAll("[^a-zA-Z0-9]", "");

                // Concatena la cadena a "YoNunca_" para formar el nombre de archivo
                fileName = "YoNunca_" + fileName;

                // Crea el archivo con el nombre de archivo único
                File archivo = new File(directorio, fileName + ".png");

                // Graba la captura de pantalla en el archivo
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(archivo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (fos != null) {
                    imagenCopia.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Obtén un Uri seguro para el archivo
                    Uri uri = FileProvider.getUriForFile(Yonunca.this, "com.example.yonunca.Provider", archivo);

                    // Crea un Intent para compartir la captura de pantalla
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/png");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    String frase = BancoFrases.frases().get(fraseActual).getFrase();
                    String aux = "Yo nunca "+frase+"."+"\n Descarga la app:\n";
                    aux = aux + "https://www.instagram.com/sergitest.02/";
                    intent.putExtra(Intent.EXTRA_TEXT,aux);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Inicia la actividad de compartir
                    startActivity(Intent.createChooser(intent, "Compartir captura de pantalla"));
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendScreenShot();
        }else{
            Toast.makeText(this,"Permiso denegado",Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean CheckPermissions() {
        int perms = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perms == PackageManager.PERMISSION_GRANTED) {
            // Ya tienes el permiso, retorna true
            return true;
        } else {
            // No tienes el permiso, solicita los permisos y retorna false
            ActivityCompat.requestPermissions(Yonunca.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }


    private void siguienteFrase() {
        fraseActual++;
        if ((fraseActual + 1) > frases.size()) {
            fraseActual = 0;
        }

        txtFrase.setText(BancoFrases.frases().get(fraseActual).getFrase());
        txtAutor.setText(BancoFrases.frases().get(fraseActual).getAutor());
    }



}