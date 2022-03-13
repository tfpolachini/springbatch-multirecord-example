package com.example.springbatchmultirecord.models;

public class Detail extends BaseModel {

  private String nome;
  private String idade;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getIdade() {
    return idade;
  }

  public void setIdade(String idade) {
    this.idade = idade;
  }

}