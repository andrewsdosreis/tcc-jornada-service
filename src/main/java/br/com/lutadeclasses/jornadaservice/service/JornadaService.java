package br.com.lutadeclasses.jornadaservice.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;
import br.com.lutadeclasses.jornadaservice.exception.notfound.CartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.model.CustomJornadaMapper;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;
import br.com.lutadeclasses.jornadaservice.repository.CartaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaCartaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaRepository;

@Service
public class JornadaService {

    private JornadaRepository jornadaRepository;
    private JornadaCartaRepository jornadaCartaRepository;
    private CartaRepository cartaRepository;
    private ObjectMapper objectMapper;
    private CustomJornadaMapper customJornadaMapper;

    public JornadaService(JornadaRepository jornadaRepository, JornadaCartaRepository jornadaCartaRepository,
            CartaRepository cartaRepository, ObjectMapper objectMapper, CustomJornadaMapper customMapper) {
        this.jornadaRepository = jornadaRepository;
        this.jornadaCartaRepository = jornadaCartaRepository;
        this.cartaRepository = cartaRepository;
        this.objectMapper = objectMapper;
        this.customJornadaMapper = customMapper;
    }

    public List<JornadaDto> listarJornadas() {
        return jornadaRepository.findAll()
                                .stream()
                                .map(jornada -> objectMapper.convertValue(jornada, JornadaDto.class))
                                .collect(Collectors.toList());
    }

    public JornadaDto buscarJornadaPorId(Integer id) {
        var jornada = jornadaRepository.findById(id).orElseThrow(() -> new JornadaNaoEncontradaException(id));
        return objectMapper.convertValue(jornada, JornadaDto.class);
    }
    
    public JornadaDto criarJornada(NovaJornadaDto novaJornada) {
        var jornada = jornadaRepository.save(new Jornada(obj -> obj.setTitulo(novaJornada.getTitulo())));
        return objectMapper.convertValue(jornada, JornadaDto.class);
    }
    
    public JornadaDto adicionarCartaEmUmaJornada(Integer jornadaId, NovaJornadaCartaDto novaJornadaCarta) {
        var jornada = jornadaRepository.findById(jornadaId).orElseThrow(() -> new JornadaNaoEncontradaException(jornadaId));
        var carta = cartaRepository.findById(novaJornadaCarta.getCartaId()).orElseThrow(() -> new CartaNaoEncontradaException(novaJornadaCarta.getCartaId()));
        var jornadaCarta = new JornadaCarta(obj -> {
            obj.setJornada(jornada);
            obj.setCarta(carta);
        });
        jornadaCartaRepository.save(jornadaCarta);
        return customJornadaMapper.converterJornadaComCartas(jornada);
    }

}
