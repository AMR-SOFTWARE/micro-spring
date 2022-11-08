package com.co.mar.mcsvcurso.service;

import com.co.mar.mcsvcurso.model.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> findAll();

    Optional<Curso> findById(Long id);

    Curso save(Curso curso);

}
