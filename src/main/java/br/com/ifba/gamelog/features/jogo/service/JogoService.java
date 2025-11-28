package br.com.ifba.gamelog.features.jogo.service;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.repository.JogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JogoService implements JogoIService {

    private final JogoRepository jogoRepository;

    @Override
    public List<Jogo> findAll() {
        return jogoRepository.findAll();
    }

    @Override
    public Jogo findById(Long id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
    }

    @Override
    public List<Jogo> findByTitulo(String titulo) {
        return jogoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    public Jogo save(Jogo jogo) {
        if (jogo.getId() != null) {
            throw new RuntimeException("Jogo já existente (possui ID). Use o método update.");
        }
        return jogoRepository.save(jogo);
    }

    @Override
    public Jogo update(Jogo jogo) {
        if (jogo.getId() == null || !jogoRepository.existsById(jogo.getId())) {
            throw new RuntimeException("Jogo não encontrado para atualização.");
        }
        return jogoRepository.save(jogo);
    }

    @Override
    public void delete(Long id) {
        if (!jogoRepository.existsById(id)) {
            throw new RuntimeException("Jogo não encontrado para exclusão.");
        }
        jogoRepository.deleteById(id);
    }
}