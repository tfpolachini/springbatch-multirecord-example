package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.models.Header;
import org.springframework.batch.item.ItemProcessor;

public class HeaderProcessor implements ItemProcessor<Header, Header> {

  @Override
  public Header process(Header header) throws Exception {
    var h = new Header();

    h.setIdentificador(header.getIdentificador());
    h.setAgencia(header.getAgencia());
    h.setBanco(header.getBanco());

    return h;
  }
}
