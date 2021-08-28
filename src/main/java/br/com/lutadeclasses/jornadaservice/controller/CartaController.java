package br.com.lutadeclasses.jornadaservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.jornadaservice.model.RequestNovaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.RequestNovaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.ResponseCartaDto;
import br.com.lutadeclasses.jornadaservice.service.CartaService;

@RestController
@RequestMapping(path = "/cartas")
public class CartaController extends BaseController {
    
    private CartaService service;

    public CartaController(CartaService cartaService) {
        this.service = cartaService;
    }

    @GetMapping()
    public List<ResponseCartaDto> listarCartas() {
        return service.listarCartas();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseCartaDto> buscarCarta(@PathVariable Integer id) {
        return ok(service.buscarCartaPorId(id));
    }
    
    @PostMapping()
    public ResponseEntity<ResponseCartaDto> criarCarta(@RequestBody RequestNovaCartaDto novaCarta) {
        return created(service.criarCarta(novaCarta));
    }

    @DeleteMapping(path = "/{id}")
    public void deletarCarta(@PathVariable Integer id) {
        service.deletarAlternativaNaCarta(id);
    }

    @PostMapping(path = "/{id}/alternativa")
    public ResponseEntity<ResponseCartaDto> adicionarAlternativaNaCarta(@PathVariable Integer id, @RequestBody RequestNovaAlternativaDto novaAlternativa) {
        return created(service.adicionarAlternativaNaCarta(id, novaAlternativa));
    }
    
    @DeleteMapping(path = "/{id}/alternativa")
    public void deletarAlternativaNaCarta(@PathVariable Integer id) {
        service.deletarAlternativaNaCarta(id);
    }
    
}
