package com.puce.gimnasio.service;

import com.puce.gimnasio.model.Maquina;
import com.puce.gimnasio.repository.MaquinaRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaquinaService {

    private MaquinaRepository maquinaRepository;

    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    //TRAER TODOS
    public List<Maquina> traerTodos() {
        return maquinaRepository.findAll();
    }

    //TRAER UNO
    public Optional<Maquina> traerUno(int id) {
        return maquinaRepository.findById(id);
    }

    //GUARDAR
    public Maquina guardar(Maquina maquina) {
        return maquinaRepository.save(maquina);
    }

    //EDITAR
    public Optional<Maquina> editar(int id, Maquina maquinaActualizada) {
        return maquinaRepository.findById(id)
                .map(maquinaExiste -> {
                    maquinaExiste.setNombre(maquinaActualizada.getNombre());
                    maquinaExiste.setMarca(maquinaActualizada.getMarca());
                    maquinaExiste.setAnioCompra(maquinaActualizada.getAnioCompra());
                    maquinaExiste.setEnMantenimiento(maquinaActualizada.isEnMantenimiento());

                    return maquinaRepository.save(maquinaExiste);
                });
    }

    //ELIMINAR
    public boolean eliminar(int id) {
        if (maquinaRepository.existsById(id)) {
            maquinaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    //ESPECIAL: OPERATIVAS
    public List<Maquina> operativas() {
        return maquinaRepository.findAll()
                .stream()
                .filter(maquina -> !maquina.isEnMantenimiento())
                .collect(Collectors.toList());
    }

    //ESPECIAL: ANTIGUAS
    public List<Maquina> antiguas(int anios) {
        int anioActual = Year.now().getValue();
        return maquinaRepository.findAll()
                .stream()
                .filter(maquina -> (anioActual - maquina.getAnioCompra()) > anios)
                .collect(Collectors.toList());
    }

    //ESPECIAL: MANTENIMIENTO (0 = no existe, 1 = ya en mantenimiento, 2 = enviada ok)
    public int mantenimiento(int id) {
        Optional<Maquina> maquinaOpt = maquinaRepository.findById(id);
        if (maquinaOpt.isEmpty()) {
            return 0;
        }
        Maquina maquina = maquinaOpt.get();
        if (maquina.isEnMantenimiento()) {
            return 1;
        }
        maquina.setEnMantenimiento(true);
        maquinaRepository.save(maquina);
        return 2;
    }
}