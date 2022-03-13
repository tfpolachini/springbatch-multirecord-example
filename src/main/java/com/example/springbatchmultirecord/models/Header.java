package com.example.springbatchmultirecord.models;

public class Header extends BaseModel {

  private String banco;
  private String agencia;

  public String getBanco() {
    return banco;
  }

  public void setBanco(String banco) {
    this.banco = banco;
  }

  public String getAgencia() {
    return agencia;
  }

  public void setAgencia(String agencia) {
    this.agencia = agencia;
  }

}
