package com.co.mar.mcsvcurso.controller;

import com.co.mar.mcsvcurso.model.Curso;
import com.co.mar.mcsvcurso.service.CursoService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> getAllCurso() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> getCursoById(@PathVariable Long id) {
        Optional<Curso> curso = service.findById(id);

        if (curso.isPresent()) {
            return ResponseEntity.ok(curso.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody Curso curso, BindingResult result) {

        if (result.hasErrors()) {
            return validFields(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(curso));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {

        Optional<Curso> cursoDB = service.findById(id);

        if (cursoDB.isPresent()) {
            if (result.hasErrors()) {
                return validFields(result);
            }

            Curso cursoUpdate = cursoDB.get().toBuilder()
                .nombre(curso.getNombre()).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoUpdate));
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Optional<Curso> cursoDB = service.findById(id);

        if (cursoDB.isPresent()) {

            Curso cursoDelete = cursoDB.get().toBuilder().active(Boolean.FALSE).build();

            service.save(cursoDelete);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

    private static ResponseEntity<Object> validFields(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> errors.put(
            err.getField(), "El campo ".concat(err.getField().concat(" ").concat(err.getDefaultMessage()))
        ));

        return ResponseEntity.badRequest().body(errors);
    }

}
