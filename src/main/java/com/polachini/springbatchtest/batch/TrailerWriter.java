package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.entities.TrailerEntity;
import com.polachini.springbatchtest.models.Trailer;
import com.polachini.springbatchtest.repository.TrailerRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.batch.item.ItemWriter;

public class TrailerWriter implements ItemWriter<Trailer> {

  private final TrailerRepository trailerRepository;

  public TrailerWriter(TrailerRepository trailerRepository) {
    this.trailerRepository = trailerRepository;
  }

  @Override
  public void write(List<? extends Trailer> trailers) throws Exception {
    var entities = trailers.stream()
        .map(this::toTrailerEntity)
        .collect(Collectors.toList());

    trailerRepository.saveAll(entities);
  }

  private TrailerEntity toTrailerEntity(Trailer trailer) {
    var entity = new TrailerEntity();

    entity.setIdentificador(trailer.getIdentificador());
    entity.setClientes(trailer.getClientes());

    return entity;
  }
}
