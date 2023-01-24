package com.example.yonunca;

public class Frases {
    private String frase;
    private String autor;
    public Frases(String frase, String autor) {
        this.frase = frase;
        this.autor = autor;
    }

    public String getFrase() {
        return frase;
    }
    public String getAutor() {
        return autor;
    }


    public void setFrase(String frase) {
        this.frase = frase;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
