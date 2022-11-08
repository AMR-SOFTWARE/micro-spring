package com.co.amr.mcsvusuario.controller;

import com.co.amr.mcsvusuario.model.Usuario;
import com.co.amr.mcsvusuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuario() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = service.findById(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return validFields(result);
        }

        if (service.existsByEmail(usuario.getEmail())) {
            return errorEmail();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody Usuario usuario, BindingResult result,
         @PathVariable Long id) {
        Optional<Usuario> usuarioDB = service.findById(id);

        if (usuarioDB.isPresent()) {
            if (result.hasErrors()) {
                return validFields(result);
            }

            if (!usuario.getEmail().equalsIgnoreCase(usuarioDB.get().getEmail())
                && service.existsByEmail(usuario.getEmail())) {
                return errorEmail();
            }

            Usuario usuarioUpdate = usuarioDB.get().toBuilder()
                    .nombre(usuario.getNombre())
                    .password(usuario.getPassword())
                    .email(usuario.getEmail()).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioUpdate));
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Usuario> usuarioDB = service.findById(id);

        if (usuarioDB.isPresent()) {

            Usuario usuarioDelete = usuarioDB.get().toBuilder()
                    .active(Boolean.FALSE).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDelete));
        }

        return ResponseEntity.notFound().build();

    }

    private static ResponseEntity<Object> errorEmail() {
        return ResponseEntity.badRequest().body(
                Collections.singletonMap("mensaje", "El email ingresado ya esta registrado!")
        );
    }

    private static ResponseEntity<Object> validFields(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> errors.put(
                err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()))
        );
        return ResponseEntity.badRequest().body(errors);
    }

}
