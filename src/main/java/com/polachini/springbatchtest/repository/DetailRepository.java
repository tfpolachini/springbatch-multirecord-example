package com.polachini.springbatchtest.repository;

import com.polachini.springbatchtest.entities.DetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailRepository extends JpaRepository<DetailEntity, Integer> {

}
