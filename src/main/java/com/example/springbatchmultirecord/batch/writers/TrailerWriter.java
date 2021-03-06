package com.example.springbatchmultirecord.batch.writers;

import com.example.springbatchmultirecord.entities.TrailerEntity;
import com.example.springbatchmultirecord.models.Trailer;
import com.example.springbatchmultirecord.repository.TrailerRepository;

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
