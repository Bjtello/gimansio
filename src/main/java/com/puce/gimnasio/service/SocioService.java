package com.puce.gimnasio.service;

import com.puce.gimnasio.model.Socio;
import com.puce.gimnasio.repository.SocioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocioService {

    private SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    //TRAER TODOS
    public List<Socio> traerTodos() {
        return socioRepository.findAll();
    }

    //TRAER UNO
    public Optional<Socio> traerUno(int id) {
        return socioRepository.findById(id);
    }

    //GUARDAR
    public Socio guardar(Socio socio) {
        return socioRepository.save(socio);
    }

    //EDITAR
    public Optional<Socio> editar(int id, Socio socioActualizado) {
        return socioRepository.findById(id)
                .map(socioExiste -> {
                    socioExiste.setNombre(socioActualizado.getNombre());
                    socioExiste.setPlan(socioActualizado.getPlan());
                    socioExiste.setMensualidad(socioActualizado.getMensualidad());
                    socioExiste.setActivo(socioActualizado.isActivo());

                    return socioRepository.save(socioExiste);
                });
    }

    //ELIMINAR
    public boolean eliminar(int id) {
        if (socioRepository.existsById(id)) {
            socioRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    //ESPECIAL: INGRESOS DE ACTIVOS
    public double calcularIngresos() {
        return socioRepository.findAll()
                .stream()
                .filter(socio -> socio.isActivo())
                .mapToDouble(socio -> socio.getMensualidad())
                .sum();
    }

    //ESPECIAL: DESACTIVAR (0 = no existe, 1 = ya inactivo, 2 = desactivado ok)
    public int desactivar(int id) {
        Optional<Socio> socioOpt = socioRepository.findById(id);
        if (socioOpt.isEmpty()) {
            return 0;
        }
        Socio socio = socioOpt.get();
        if (!socio.isActivo()) {
            return 1;
        }
        socio.setActivo(false);
        socioRepository.save(socio);
        return 2;
    }
}