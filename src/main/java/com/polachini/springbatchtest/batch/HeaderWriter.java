package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.entities.HeaderEntity;
import com.polachini.springbatchtest.models.Header;
import com.polachini.springbatchtest.repository.HeaderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.batch.item.ItemWriter;

public class HeaderWriter implements ItemWriter<Header> {

  private final HeaderRepository headerRepository;

  public HeaderWriter(HeaderRepository headerRepository) {
    this.headerRepository = headerRepository;
  }

  @Override
  public void write(List<? extends Header> headers) throws Exception {
    var entities = headers.stream()
        .map(this::toHeaderEntity)
        .collect(Collectors.toList());

    headerRepository.saveAll(entities);
  }

  private HeaderEntity toHeaderEntity(Header header) {
    HeaderEntity headerEntity = new HeaderEntity();

    headerEntity.setIdentificador(header.getIdentificador());
    headerEntity.setBanco(header.getBanco());
    headerEntity.setAgencia(header.getAgencia());

    return headerEntity;
  }
}
