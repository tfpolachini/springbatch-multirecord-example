package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.entities.DetailEntity;
import com.polachini.springbatchtest.models.Detail;
import com.polachini.springbatchtest.repository.DetailRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.batch.item.ItemWriter;

public class DetailWriter implements ItemWriter<Detail> {

  private final DetailRepository detailRepository;

  public DetailWriter(DetailRepository detailRepository) {
    this.detailRepository = detailRepository;
  }

  @Override
  public void write(List<? extends Detail> details) throws Exception {
    var entities = details.stream()
        .map(this::toDetailEntity)
        .collect(Collectors.toList());

    detailRepository.saveAll(entities);
  }

  private DetailEntity toDetailEntity(Detail detail) {
    var entity = new DetailEntity();

    entity.setIdentificador(detail.getIdentificador());
    entity.setNome(detail.getNome());
    entity.setIdade(detail.getIdade());

    return entity;
  }
}
