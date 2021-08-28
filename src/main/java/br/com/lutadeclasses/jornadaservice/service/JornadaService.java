package br.com.lutadeclasses.jornadaservice.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;
import br.com.lutadeclasses.jornadaservice.exception.CartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.JornadaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.model.CustomMapper;
import br.com.lutadeclasses.jornadaservice.model.RequestNovaJornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.RequestNovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.ResponseJornadaDto;
import br.com.lutadeclasses.jornadaservice.repository.CartaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaCartaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaRepository;

@Service
public class JornadaService {

    private JornadaRepository jornadaRepository;
    private JornadaCartaRepository jornadaCartaRepository;
    private CartaRepository cartaRepository;
    private ObjectMapper objectMapper;
    private CustomMapper customMapper;

    public JornadaService(JornadaRepository jornadaRepository, JornadaCartaRepository jornadaCartaRepository,
            CartaRepository cartaRepository, ObjectMapper objectMapper, CustomMapper customMapper) {
        this.jornadaRepository = jornadaRepository;
        this.jornadaCartaRepository = jornadaCartaRepository;
        this.cartaRepository = cartaRepository;
        this.objectMapper = objectMapper;
        this.customMapper = customMapper;
    }

    public List<ResponseJornadaDto> listarJornadas() {
        return jornadaRepository.findAll()
                                .stream()
                                .map(jornada -> objectMapper.convertValue(jornada, ResponseJornadaDto.class))
                                .collect(Collectors.toList());
    }

    public ResponseJornadaDto buscarJornadaPorId(Integer id) {
        var jornada = jornadaRepository.findById(id).orElseThrow(() -> new JornadaNaoEncontradaException(id));
        return objectMapper.convertValue(jornada, ResponseJornadaDto.class);
    }
    
    public ResponseJornadaDto criarJornada(RequestNovaJornadaDto novaJornada) {
        var jornada = jornadaRepository.save(new Jornada(obj -> obj.setTitulo(novaJornada.getTitulo())));
        return objectMapper.convertValue(jornada, ResponseJornadaDto.class);
    }
    
    public ResponseJornadaDto adicionarCartaEmUmaJornada(Integer jornadaId, RequestNovaJornadaCartaDto novaJornadaCarta) {
        var jornada = jornadaRepository.findById(jornadaId).orElseThrow(() -> new JornadaNaoEncontradaException(jornadaId));
        var carta = cartaRepository.findById(novaJornadaCarta.getCartaId()).orElseThrow(() -> new CartaNaoEncontradaException(novaJornadaCarta.getCartaId()));
        var jornadaCarta = new JornadaCarta(obj -> {
            obj.setPontoInicial(novaJornadaCarta.getPontoInicial());
            obj.setJornada(jornada);
            obj.setCarta(carta);
        });
        jornadaCartaRepository.save(jornadaCarta);
        return customMapper.converterJornadaComCartas(jornada);
    }

}
