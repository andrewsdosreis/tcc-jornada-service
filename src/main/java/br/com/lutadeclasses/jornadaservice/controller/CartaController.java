package br.com.lutadeclasses.jornadaservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.jornadaservice.model.request.NovaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.CartaDto;
import br.com.lutadeclasses.jornadaservice.service.CartaService;

@RestController
@RequestMapping(path = "/cartas")
public class CartaController extends BaseController {
    
    private CartaService cartaService;

    public CartaController(CartaService cartaService) {
        this.cartaService = cartaService;
    }

    @GetMapping()
    public List<CartaDto> listarCartas() {
        return cartaService.listarCartas();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CartaDto> buscarCarta(@PathVariable Integer id) {
        return ok(cartaService.buscarCartaPorId(id));
    }
    
    @PostMapping()
    public ResponseEntity<CartaDto> criarCarta(@RequestBody @Valid NovaCartaDto novaCarta) {
        return created(cartaService.criarCarta(novaCarta));
    }

    @DeleteMapping(path = "/{id}")
    public void deletarCarta(@PathVariable Integer id) {
        cartaService.deletarCarta(id);
    }

    @PostMapping(path = "/{id}/alternativa")
    public ResponseEntity<CartaDto> adicionarAlternativaNaCarta(@PathVariable Integer id, @RequestBody NovaAlternativaDto novaAlternativa) {
        return created(cartaService.adicionarAlternativaNaCarta(id, novaAlternativa));
    }
    
    @DeleteMapping(path = "/{id}/alternativa")
    public void deletarAlternativaNaCarta(@PathVariable Integer id) {
        cartaService.deletarAlternativaNaCarta(id);
    }
    
}
