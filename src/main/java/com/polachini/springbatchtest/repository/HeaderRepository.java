package com.polachini.springbatchtest.repository;

import com.polachini.springbatchtest.entities.HeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeaderRepository extends JpaRepository<HeaderEntity, Integer> {

}
