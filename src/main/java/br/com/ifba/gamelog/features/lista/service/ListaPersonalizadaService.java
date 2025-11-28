package br.com.ifba.gamelog.features.lista.service;

import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.features.lista.repository.ListaPersonalizadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListaPersonalizadaService implements ListaPersonalizadaIService {

    private final ListaPersonalizadaRepository listaRepository;

    @Override
    public List<ListaPersonalizada> findAll() {
        return listaRepository.findAll();
    }

    @Override
    public List<ListaPersonalizada> findByUsuario(Long usuarioId) {
        return listaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public ListaPersonalizada save(ListaPersonalizada lista) {
        if (lista.getId() != null) {
            throw new RuntimeException("Lista já existente. Use o método update.");
        }
        return listaRepository.save(lista);
    }

    @Override
    public ListaPersonalizada update(ListaPersonalizada lista) {
        if (lista.getId() == null || !listaRepository.existsById(lista.getId())) {
            throw new RuntimeException("Lista não encontrada para atualização.");
        }
        return listaRepository.save(lista);
    }

    @Override
    public void delete(Long id) {
        if (!listaRepository.existsById(id)) {
            throw new RuntimeException("Lista não encontrada para exclusão.");
        }
        listaRepository.deleteById(id);
    }
}