package com.puce.gimnasio.controller;

import com.puce.gimnasio.model.Maquina;
import com.puce.gimnasio.service.MaquinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maquina")
public class MaquinaController {

    private MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    @GetMapping
    public List<Maquina> verTodos() {
        return maquinaService.traerTodos();
    }

    //TRAER UNO
    @GetMapping("/{id}")
    public ResponseEntity<Maquina> traerUno(@PathVariable int id) {
        return maquinaService.traerUno(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //GUARDAR
    @PostMapping
    public ResponseEntity<Maquina> guardar(@RequestBody Maquina maquina) {
        Maquina nuevaMaquina = maquinaService.guardar(maquina);
        return new ResponseEntity<>(nuevaMaquina, HttpStatus.CREATED);
    }

    //EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<Maquina> editar(@PathVariable int id, @RequestBody Maquina maquina) {
        return maquinaService.editar(id, maquina)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Maquina> eliminar(@PathVariable int id) {
        if (maquinaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //OPERATIVAS
    @GetMapping("/operativas")
    public List<Maquina> operativas() {
        return maquinaService.operativas();
    }

    //ANTIGUAS
    @GetMapping("/antiguas")
    public List<Maquina> antiguas(@RequestParam int anios) {
        return maquinaService.antiguas(anios);
    }

    //MANTENIMIENTO
    @PutMapping("/{id}/mantenimiento")
    public ResponseEntity<Void> mantenimiento(@PathVariable int id) {
        int resultado = maquinaService.mantenimiento(id);
        if (resultado == 0) {
            return ResponseEntity.notFound().build();
        } else if (resultado == 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}