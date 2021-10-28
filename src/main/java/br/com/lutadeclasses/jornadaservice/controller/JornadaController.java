package br.com.lutadeclasses.jornadaservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;
import br.com.lutadeclasses.jornadaservice.service.JornadaService;

@RestController
@RequestMapping(path = "/jornadas")
public class JornadaController extends BaseController {
    
    private JornadaService service;

    public JornadaController(JornadaService service) {
        this.service = service;
    }

    @GetMapping()
    public List<JornadaDto> listarJornadas() {
        return service.listarJornadas();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<JornadaDto> buscarJornadaPorId(@PathVariable Integer id) {
        return ok(service.buscarJornadaPorId(id));
    }
    
    @PostMapping()
    public ResponseEntity<JornadaDto> criarjornada(@RequestBody NovaJornadaDto novaJornada) {
        return created(service.criarJornada(novaJornada));
    }
    
    @PostMapping(path = "/{id}/carta")
    public ResponseEntity<JornadaDto> adicionarCartaNaJornada(@PathVariable Integer id, @RequestBody NovaJornadaCartaDto novaJornadaCarta) {
        return created(service.adicionarCartaEmUmaJornada(id, novaJornadaCarta));
    }
    
}
