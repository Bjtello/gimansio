package com.puce.gimnasio.controller;

import com.puce.gimnasio.model.Socio;
import com.puce.gimnasio.service.SocioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socio")
public class SocioController {

    private SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping
    public List<Socio> verTodos() {
        return socioService.traerTodos();
    }

    //TRAER UNO
    @GetMapping("/{id}")
    public ResponseEntity<Socio> traerUno(@PathVariable int id) {
        return socioService.traerUno(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //GUARDAR
    @PostMapping
    public ResponseEntity<Socio> guardar(@RequestBody Socio socio) {
        Socio nuevoSocio = socioService.guardar(socio);
        return new ResponseEntity<>(nuevoSocio, HttpStatus.CREATED);
    }

    //EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<Socio> editar(@PathVariable int id, @RequestBody Socio socio) {
        return socioService.editar(id, socio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Socio> eliminar(@PathVariable int id) {
        if (socioService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //INGRESOS
    @GetMapping("/ingresos")
    public ResponseEntity<Double> ingresos() {
        return ResponseEntity.ok(socioService.calcularIngresos());
    }

    //DESACTIVAR
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable int id) {
        int resultado = socioService.desactivar(id);
        if (resultado == 0) {
            return ResponseEntity.notFound().build();
        } else if (resultado == 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}