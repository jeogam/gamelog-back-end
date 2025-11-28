package br.com.ifba.gamelog.features.lista.controller;

import br.com.ifba.gamelog.features.lista.model.ListaPersonalizada;
import br.com.ifba.gamelog.features.lista.service.ListaPersonalizadaIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/listas")
@RequiredArgsConstructor
public class ListaPersonalizadaController implements ListaPersonalizadaIController {

    private final ListaPersonalizadaIService listaService;

    @Override
    @GetMapping(path = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ListaPersonalizada>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(listaService.findAll());
    }

    @Override
    @GetMapping(path = "/usuario/{usuarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ListaPersonalizada>> findByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(listaService.findByUsuario(usuarioId));
    }

    @Override
    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListaPersonalizada> save(@RequestBody ListaPersonalizada lista) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(listaService.save(lista));
    }

    @Override
    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody ListaPersonalizada lista) {
        listaService.update(lista);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        listaService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}