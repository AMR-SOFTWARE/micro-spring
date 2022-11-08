package com.co.amr.mcsvusuario.repository;

import com.co.amr.mcsvusuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT u Usuario u WHERE u.active = true")
    List<Usuario> findAll();

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT u Usuario u WHERE u.id = :id AND u.active = true")
    Optional<Usuario> findById(Long id);

    @Transactional(readOnly = true)
    boolean existsByEmail(String email);
}
