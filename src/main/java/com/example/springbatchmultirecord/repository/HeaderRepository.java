package com.example.springbatchmultirecord.repository;

import com.example.springbatchmultirecord.entities.HeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeaderRepository extends JpaRepository<HeaderEntity, Integer> {

}
