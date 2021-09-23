package com.polachini.springbatchtest.repository;

import com.polachini.springbatchtest.entities.DetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DetailRepository extends PagingAndSortingRepository<DetailEntity, Integer>, JpaRepository<DetailEntity, Integer> {

    Page<DetailEntity> findByIdentificador(String identificador, Pageable pageable);
}
