package com.example.yonunca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class YoNuncaAlocholico extends AppCompatActivity {
    private TextView txtFrase;
    private TextView txtAutor;
    private ImageButton next;
    private ImageButton back;
    private int fraseActual = 0;
    private ImageButton share;
    private List<Frases> frases = new ArrayList<Frases>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yo_nunca_alocholico);
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
                Intent volver = new Intent(YoNuncaAlocholico.this, MainActivity.class);
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
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String frase = BancoFrases.frases().get(fraseActual).getFrase();
                i.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
                String aux = "Yo nunca "+frase+"."+"\n Descarga la app:\n";
                aux = aux + "https://www.instagram.com/sergitest.02/";
                i.putExtra(Intent.EXTRA_TEXT,aux);
                startActivity(i);
            }
        });
    }

    private void siguienteFrase(){
        fraseActual++;
        if ( (fraseActual+1) > frases.size()){
            fraseActual = 0;
        }

        txtFrase.setText(BancoFrases.frases().get(fraseActual).getFrase());
        txtAutor.setText(BancoFrases.frases().get(fraseActual).getAutor());
    }
}