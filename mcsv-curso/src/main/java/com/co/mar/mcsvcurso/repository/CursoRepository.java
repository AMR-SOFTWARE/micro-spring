package com.co.mar.mcsvcurso.repository;

import com.co.mar.mcsvcurso.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT c FROM Curso c WHERE c.active = true")
    List<Curso> findAll();

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT c FROM Curso c WHERE c.id = :id AND c.active = true")
    Optional<Curso> findById(Long id);
}
