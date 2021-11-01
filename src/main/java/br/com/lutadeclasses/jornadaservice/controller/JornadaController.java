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

import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;
import br.com.lutadeclasses.jornadaservice.service.JornadaService;

@RestController
@RequestMapping(path = "/jornada")
public class JornadaController extends BaseController {
    
    private JornadaService jornadaService;

    public JornadaController(JornadaService service) {
        this.jornadaService = service;
    }

    @GetMapping
    public List<JornadaDto> listarJornadas() {
        return jornadaService.listarJornadas();
    }

    @GetMapping(path = "/{jornadaId}")
    public ResponseEntity<JornadaDto> buscarJornadaPorId(@PathVariable Integer jornadaId) {
        return ok(jornadaService.buscarJornadaPorId(jornadaId));
    }
    
    @PostMapping
    public ResponseEntity<JornadaDto> criarjornada(@RequestBody NovaJornadaDto novaJornadaDto) {
        return created(jornadaService.criarJornada(novaJornadaDto));
    }
    
    @PostMapping(path = "/{jornadaId}")
    public ResponseEntity<JornadaDto> adicionarCartaNaJornada(@PathVariable Integer jornadaId, @RequestBody @Valid NovaJornadaCartaDto novaJornadaCartaDto) {
        return created(jornadaService.adicionarCartaNaJornada(jornadaId, novaJornadaCartaDto));
    }

    @PostMapping(path = "/{jornadaId}/carta/{cartaId}")
    public ResponseEntity<JornadaCartaDto> definirProximaCarta(@PathVariable Integer jornadaId, @PathVariable Integer cartaId, @RequestBody @Valid NovaJornadaAlternativaDto novaJornadaAlternativaDto) {
        return created(jornadaService.definirProximaCarta(jornadaId, cartaId, novaJornadaAlternativaDto));
    }

    @DeleteMapping(path = "/{jornadaId}/carta/{cartaId}")
    public void deletarCartaDaJornada(@PathVariable Integer jornadaId, @PathVariable Integer cartaId) {
        jornadaService.deletarCartaDaJornada(jornadaId, cartaId);
    }

}
