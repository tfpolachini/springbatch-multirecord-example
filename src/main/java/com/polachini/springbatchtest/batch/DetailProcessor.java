package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.models.Detail;
import org.springframework.batch.item.ItemProcessor;

public class DetailProcessor implements ItemProcessor<Detail, Detail> {

  @Override
  public Detail process(Detail detail) throws Exception {
    var d = new Detail();

    d.setIdentificador(detail.getIdentificador());
    d.setNome(detail.getNome());
    d.setIdade(detail.getIdade());

    return d;
  }
}
