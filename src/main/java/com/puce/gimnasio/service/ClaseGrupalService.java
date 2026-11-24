package com.puce.gimnasio.service;

import com.puce.gimnasio.model.ClaseGrupal;
import com.puce.gimnasio.repository.ClaseGrupalRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClaseGrupalService {

    private ClaseGrupalRepository claseRepository;

    public ClaseGrupalService(ClaseGrupalRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    //TRAER TODOS
    public List<ClaseGrupal> traerTodos() {
        return claseRepository.findAll();
    }

    //TRAER UNO
    public Optional<ClaseGrupal> traerUno(int id) {
        return claseRepository.findById(id);
    }

    //GUARDAR
    public ClaseGrupal guardar(ClaseGrupal clase) {
        return claseRepository.save(clase);
    }

    //EDITAR
    public Optional<ClaseGrupal> editar(int id, ClaseGrupal claseActualizada) {
        return claseRepository.findById(id)
                .map(claseExiste -> {
                    claseExiste.setNombre(claseActualizada.getNombre());
                    claseExiste.setInstructor(claseActualizada.getInstructor());
                    claseExiste.setHorario(claseActualizada.getHorario());
                    claseExiste.setCupos(claseActualizada.getCupos());

                    return claseRepository.save(claseExiste);
                });
    }

    //ELIMINAR
    public boolean eliminar(int id) {
        if (claseRepository.existsById(id)) {
            claseRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    //ESPECIAL: INSCRIBIR (0 = no existe, 1 = sin cupos, 2 = inscrito ok)
    public int inscribir(int id) {
        Optional<ClaseGrupal> claseOpt = claseRepository.findById(id);
        if (claseOpt.isEmpty()) {
            return 0;
        }
        ClaseGrupal clase = claseOpt.get();
        if (clase.getCupos() <= 0) {
            return 1;
        }
        clase.setCupos(clase.getCupos() - 1);
        claseRepository.save(clase);
        return 2;
    }

    //ESPECIAL: CANCELAR INSCRIPCION
    public boolean cancelarInscripcion(int id) {
        Optional<ClaseGrupal> claseOpt = claseRepository.findById(id);
        if (claseOpt.isEmpty()) {
            return false;
        }
        ClaseGrupal clase = claseOpt.get();
        clase.setCupos(clase.getCupos() + 1);
        claseRepository.save(clase);
        return true;
    }

    //ESPECIAL: CON CUPOS ORDENADAS DE MAS A MENOS
    public List<ClaseGrupal> conCupos() {
        return claseRepository.findAll()
                .stream()
                .filter(clase -> clase.getCupos() > 0)
                .sorted(Comparator.comparingInt(ClaseGrupal::getCupos).reversed())
                .collect(Collectors.toList());
    }
}