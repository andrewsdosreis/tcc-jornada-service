package br.com.lutadeclasses.jornadaservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.jornadaservice.model.RequestNovaJornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.RequestNovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.ResponseJornadaDto;
import br.com.lutadeclasses.jornadaservice.service.JornadaService;

@RestController
@RequestMapping(path = "/jornadas")
public class JornadaController extends BaseController {
    
    private JornadaService service;

    public JornadaController(JornadaService service) {
        this.service = service;
    }

    @GetMapping()
    public List<ResponseJornadaDto> listarJornadas() {
        return service.listarJornadas();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseJornadaDto> buscarJornadaPorId(@PathVariable Integer id) {
        return ok(service.buscarJornadaPorId(id));
    }
    
    @PostMapping()
    public ResponseEntity<ResponseJornadaDto> criarjornada(@RequestBody RequestNovaJornadaDto novaJornada) {
        return created(service.criarJornada(novaJornada));
    }
    
    @PostMapping(path = "/{id}/carta")
    public ResponseEntity<ResponseJornadaDto> adicionarCartaNaJornada(@PathVariable Integer id, @RequestBody RequestNovaJornadaCartaDto novaJornadaCarta) {
        return created(service.adicionarCartaEmUmaJornada(id, novaJornadaCarta));
    }
    
}
