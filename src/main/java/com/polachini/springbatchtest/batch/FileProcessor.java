package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.models.Header;

public class FileProcessor {

  public static Header process(Header header) {
    var h = new Header();

    h.setIdentificador(header.getIdentificador());
    h.setAgencia(header.getAgencia());
    h.setBanco(header.getBanco());

    return h;
  }

}
