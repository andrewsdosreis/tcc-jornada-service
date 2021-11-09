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

import br.com.lutadeclasses.jornadaservice.converter.JornadaConverter;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaCartaDerrotaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;
import br.com.lutadeclasses.jornadaservice.service.CartaService;
import br.com.lutadeclasses.jornadaservice.service.JornadaService;

@RestController
@RequestMapping(path = "/v1/jornada")
public class JornadaController extends BaseController {
    
    private JornadaService jornadaService;
    private CartaService cartaService;

    public JornadaController(JornadaService jornadaService, CartaService cartaService) {
        this.jornadaService = jornadaService;
        this.cartaService = cartaService;
    }

    @GetMapping
    public List<JornadaDto> listarJornadas() {
        return jornadaService.listarJornadas();
    }

    @GetMapping(path = "/{jornadaId}")
    public ResponseEntity<JornadaDto> buscarJornadaPorId(@PathVariable Integer jornadaId) {
        var jornada = jornadaService.buscarJornadaPorId(jornadaId);
        return ok(JornadaConverter.converterJornadaComCartas(jornada));
    }
    
    @PostMapping
    public ResponseEntity<JornadaDto> criarjornada(@RequestBody NovaJornadaDto novaJornadaDto) {
        var jornada = jornadaService.criarJornada(novaJornadaDto);
        return created(JornadaConverter.converterJornadaComCartas(jornada));
    }
    
    @PostMapping(path = "/{jornadaId}/carta")
    public ResponseEntity<JornadaDto> adicionarCartaNaJornada(@PathVariable Integer jornadaId, @RequestBody @Valid NovaJornadaCartaDto novaJornadaCartaDto) {
        var carta = cartaService.buscarCarta(novaJornadaCartaDto.getCartaId());
        var jornada = jornadaService.adicionarCartaNaJornada(jornadaId, carta);
        return created(JornadaConverter.converterJornadaComCartas(jornada));
    }

    @PostMapping(path = "/{jornadaId}/carta/{cartaId}")
    public ResponseEntity<JornadaCartaDto> definirProximaCarta(@PathVariable Integer jornadaId, @PathVariable Integer cartaId, @RequestBody @Valid NovaJornadaAlternativaDto novaJornadaAlternativaDto) {
        var jornadaCarta = jornadaService.definirProximaCarta(jornadaId, cartaId, novaJornadaAlternativaDto);
        return created(JornadaConverter.converterJornadaCartaComAlternativas(jornadaCarta));
    }

    @DeleteMapping(path = "/{jornadaId}/carta/{cartaId}")
    public void deletarCartaDaJornada(@PathVariable Integer jornadaId, @PathVariable Integer cartaId) {
        jornadaService.deletarCartaDaJornada(jornadaId, cartaId);
    }

    @PostMapping(path = "/{jornadaId}/derrota")
    public ResponseEntity<JornadaCartaDto> adicionarJornadaCartaDerrota(@PathVariable Integer jornadaId, @RequestBody @Valid NovaJornadaCartaDerrotaDto novaJornadaCartaDerrotaDto) {
        var barra = cartaService.buscarBarra(novaJornadaCartaDerrotaDto.getBarra());
        var carta = cartaService.buscarCarta(novaJornadaCartaDerrotaDto.getCartaId());
        var jornadaCarta = jornadaService.adicionarJornadaCartaDerrota(jornadaId, carta, barra);
        return created(JornadaConverter.converterJornadaCartaComAlternativas(jornadaCarta));
    }

}
