package br.com.ifba.gamelog.features.jogo.controller;

import br.com.ifba.gamelog.features.jogo.model.Jogo;
import br.com.ifba.gamelog.features.jogo.service.JogoIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/jogos")
@RequiredArgsConstructor
public class JogoController implements JogoIController {

    private final JogoIService jogoService;

    @Override
    @GetMapping(path = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Jogo>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(jogoService.findAll());
    }

    @Override
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Jogo>> findByTitulo(@RequestParam String titulo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(jogoService.findByTitulo(titulo));
    }

    @Override
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Jogo> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(jogoService.findById(id));
    }

    @Override
    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Jogo> save(@RequestBody Jogo jogo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jogoService.save(jogo));
    }

    @Override
    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody Jogo jogo) {
        jogoService.update(jogo);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jogoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}