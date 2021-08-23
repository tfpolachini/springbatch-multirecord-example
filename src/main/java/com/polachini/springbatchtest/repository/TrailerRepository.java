package com.polachini.springbatchtest.repository;

import com.polachini.springbatchtest.entities.TrailerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrailerRepository extends JpaRepository<TrailerEntity, Integer> {

}
