package com.example.poolapp.repository;

import com.example.poolapp.model.DayTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayTemplateRepository extends JpaRepository<DayTemplate, Long> {
}
