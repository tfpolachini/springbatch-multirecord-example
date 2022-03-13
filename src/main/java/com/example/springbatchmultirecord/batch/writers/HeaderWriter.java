package com.example.springbatchmultirecord.batch.writers;

import com.example.springbatchmultirecord.entities.HeaderEntity;
import com.example.springbatchmultirecord.models.Header;
import com.example.springbatchmultirecord.repository.HeaderRepository;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.stream.Collectors;

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
