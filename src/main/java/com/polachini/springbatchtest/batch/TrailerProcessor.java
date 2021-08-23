package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.models.Trailer;
import org.springframework.batch.item.ItemProcessor;

public class TrailerProcessor implements ItemProcessor<Trailer, Trailer> {

  @Override
  public Trailer process(Trailer trailer) throws Exception {
    var t = new Trailer();

    t.setIdentificador(trailer.getIdentificador());
    t.setClientes(trailer.getClientes());

    return t;
  }
}
