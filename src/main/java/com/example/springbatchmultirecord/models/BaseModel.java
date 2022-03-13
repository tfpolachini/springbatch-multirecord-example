package com.example.springbatchmultirecord.models;

public abstract class BaseModel {

    private String identificador;

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
