package com.example.springbatchmultirecord.batch.processors;

import com.example.springbatchmultirecord.models.Header;

public class FileProcessor {

  public static Header process(Header header) {
    var h = new Header();

    h.setIdentificador(header.getIdentificador());
    h.setAgencia(header.getAgencia());
    h.setBanco(header.getBanco());

    return h;
  }

}
