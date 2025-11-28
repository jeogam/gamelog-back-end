package br.com.ifba.gamelog.features.avaliacao.controller;

import br.com.ifba.gamelog.features.avaliacao.model.Avaliacao;
import br.com.ifba.gamelog.features.avaliacao.service.AvaliacaoIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController implements AvaliacaoIController {

    private final AvaliacaoIService avaliacaoService;

    @Override
    @GetMapping(path = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Avaliacao>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(avaliacaoService.findAll());
    }

    @Override
    @GetMapping(path = "/jogo/{jogoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Avaliacao>> findByJogo(@PathVariable Long jogoId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(avaliacaoService.findByJogo(jogoId));
    }

    @Override
    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Avaliacao> save(@RequestBody Avaliacao avaliacao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(avaliacaoService.save(avaliacao));
    }

    @Override
    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody Avaliacao avaliacao) {
        avaliacaoService.update(avaliacao);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        avaliacaoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}