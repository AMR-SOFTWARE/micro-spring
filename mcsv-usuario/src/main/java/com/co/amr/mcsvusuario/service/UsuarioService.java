package com.co.amr.mcsvusuario.service;

import com.co.amr.mcsvusuario.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Usuario save(Usuario usuario);

    boolean existsByEmail(String email);

}
