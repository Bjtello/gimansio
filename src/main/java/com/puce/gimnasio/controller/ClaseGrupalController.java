package com.puce.gimnasio.controller;

import com.puce.gimnasio.model.ClaseGrupal;
import com.puce.gimnasio.service.ClaseGrupalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clase")
public class ClaseGrupalController {

    private ClaseGrupalService claseService;

    public ClaseGrupalController(ClaseGrupalService claseService) {
        this.claseService = claseService;
    }

    @GetMapping
    public List<ClaseGrupal> verTodos() {
        return claseService.traerTodos();
    }

    //TRAER UNO
    @GetMapping("/{id}")
    public ResponseEntity<ClaseGrupal> traerUno(@PathVariable int id) {
        return claseService.traerUno(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //GUARDAR
    @PostMapping
    public ResponseEntity<ClaseGrupal> guardar(@RequestBody ClaseGrupal clase) {
        ClaseGrupal nuevaClase = claseService.guardar(clase);
        return new ResponseEntity<>(nuevaClase, HttpStatus.CREATED);
    }

    //EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<ClaseGrupal> editar(@PathVariable int id, @RequestBody ClaseGrupal clase) {
        return claseService.editar(id, clase)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<ClaseGrupal> eliminar(@PathVariable int id) {
        if (claseService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //INSCRIBIR
    @PutMapping("/{id}/inscribir")
    public ResponseEntity<Void> inscribir(@PathVariable int id) {
        int resultado = claseService.inscribir(id);
        if (resultado == 0) {
            return ResponseEntity.notFound().build();
        } else if (resultado == 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    //CANCELAR INSCRIPCION
    @PutMapping("/{id}/cancelar-inscripcion")
    public ResponseEntity<Void> cancelarInscripcion(@PathVariable int id) {
        if (claseService.cancelarInscripcion(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //CON CUPOS
    @GetMapping("/con-cupos")
    public List<ClaseGrupal> conCupos() {
        return claseService.conCupos();
    }
}