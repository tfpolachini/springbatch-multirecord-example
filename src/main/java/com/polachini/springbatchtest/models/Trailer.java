package com.polachini.springbatchtest.models;

public class Trailer extends BaseModel {

  private String identificador;
  private Integer clientes;

  public String getIdentificador() {
    return identificador;
  }

  public void setIdentificador(String identificador) {
    this.identificador = identificador;
  }

  public Integer getClientes() {
    return clientes;
  }

  public void setClientes(Integer clientes) {
    this.clientes = clientes;
  }

}
