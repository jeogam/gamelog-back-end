package br.com.ifba.gamelog.features.avaliacao.service;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import br.com.ifba.gamelog.features.avaliacao.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService implements AvaliacaoIService {

    private final AvaliacaoRepository avaliacaoRepository;

    @Override
    public List<Avaliacao> findAll() {
        return avaliacaoRepository.findAll();
    }

    @Override
    public List<Avaliacao> findByJogo(Long jogoId) {
        return avaliacaoRepository.findByJogoId(jogoId);
    }

    @Override
    public Avaliacao save(Avaliacao avaliacao) {
        if (avaliacao.getId() != null) {
            throw new RuntimeException("Avaliação já existe. Use update.");
        }
        // Define data de criação automaticamente
        avaliacao.setCriadoEm(LocalDateTime.now());
        return avaliacaoRepository.save(avaliacao);
    }

    @Override
    public Avaliacao update(Avaliacao avaliacao) {
        if (avaliacao.getId() == null || !avaliacaoRepository.existsById(avaliacao.getId())) {
            throw new RuntimeException("Avaliação não encontrada.");
        }
        // Mantém a data de criação original se necessário, ou atualiza
        return avaliacaoRepository.save(avaliacao);
    }

    @Override
    public void delete(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new RuntimeException("Avaliação não encontrada.");
        }
        avaliacaoRepository.deleteById(id);
    }
}