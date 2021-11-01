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

import br.com.lutadeclasses.jornadaservice.model.request.NovaAcaoDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.CartaDto;
import br.com.lutadeclasses.jornadaservice.service.CartaService;

@RestController
@RequestMapping(path = "/carta")
public class CartaController extends BaseController {
    
    private CartaService cartaService;

    public CartaController(CartaService cartaService) {
        this.cartaService = cartaService;
    }

    @GetMapping
    public List<CartaDto> listarCartas() {
        return cartaService.listarCartas();
    }

    @GetMapping(path = "/{cartaId}")
    public ResponseEntity<CartaDto> buscarCarta(@PathVariable Integer cartaId) {
        return ok(cartaService.buscarCartaPorId(cartaId));
    }
    
    @PostMapping
    public ResponseEntity<CartaDto> criarCarta(@RequestBody @Valid NovaCartaDto novaCarta) {
        return created(cartaService.criarCarta(novaCarta));
    }

    @DeleteMapping(path = "/{cartaId}")
    public void deletarCarta(@PathVariable Integer cartaId) {
        cartaService.deletarCarta(cartaId);
    }

    @PostMapping(path = "/{cartaId}/alternativa")
    public ResponseEntity<CartaDto> adicionarAlternativaNaCarta(@PathVariable Integer cartaId, @RequestBody @Valid NovaAlternativaDto novaAlternativaDto) {
        return created(cartaService.adicionarAlternativaNaCarta(cartaId, novaAlternativaDto));
    }
    
    @DeleteMapping(path = "/{cartaId}/alternativa/{alternativaId}")
    public void deletarAlternativaNaCarta(@PathVariable Integer cartaId, @PathVariable Integer alternativaId) {
        cartaService.deletarAlternativaNaCarta(cartaId, alternativaId);
    }

    @PostMapping(path = "/{cartaId}/alternativa/{alternativaId}")
    public ResponseEntity<CartaDto> adicionarAcaoNaAlternativa(@PathVariable Integer cartaId, @PathVariable Integer alternativaId, @RequestBody @Valid NovaAcaoDto novaAcaoDto) {
        return created(cartaService.adicionarAcaoNaAlternativa(cartaId, alternativaId, novaAcaoDto));
    }
    
    @DeleteMapping(path = "/{cartaId}/alternativa/{alternativaId}/acao/{acaoId}")
    public void deletarAlternativaNaCarta(@PathVariable Integer cartaId, @PathVariable Integer alternativaId, @PathVariable Integer acaoId) {
        cartaService.deletarAcaoNaAlternativa(cartaId, alternativaId, acaoId);
    }

}
