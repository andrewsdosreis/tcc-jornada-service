package br.com.lutadeclasses.jornadaservice.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.exception.AlternativaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.CartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.model.CustomMapper;
import br.com.lutadeclasses.jornadaservice.model.RequestNovaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.RequestNovaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.ResponseCartaDto;
import br.com.lutadeclasses.jornadaservice.repository.AlternativaRepository;
import br.com.lutadeclasses.jornadaservice.repository.CartaRepository;

@Service
public class CartaService {
    
    private CartaRepository cartaRepository;
    private AlternativaRepository alternativaRepository;
    private CustomMapper customMapper;
    private ObjectMapper mapper;

    public CartaService(CartaRepository cartaRepository, AlternativaRepository alternativaRepository, CustomMapper customMapper, ObjectMapper mapper) {
        this.cartaRepository = cartaRepository;
        this.alternativaRepository = alternativaRepository;
        this.customMapper = customMapper;
        this.mapper = mapper;
    }

    public List<ResponseCartaDto> listarCartas() {
        return cartaRepository.findAll()
                              .stream()
                              .map(carta -> customMapper.converterCartaComAlternativa(carta))
                              .collect(Collectors.toList());
    }

    public ResponseCartaDto buscarCartaPorId(Integer id) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        return customMapper.converterCartaComAlternativa(carta);
    }

    public ResponseCartaDto criarCarta(RequestNovaCartaDto novaCarta) {
        var carta = cartaRepository.save(new Carta(obj -> obj.setDescricao(novaCarta.getDescricao())));
        return mapper.convertValue(carta, ResponseCartaDto.class);
    }

    public void deletarCarta(Integer id) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        alternativaRepository.deleteAll(carta.getAlternativas());
        cartaRepository.delete(carta);
    }

    public ResponseCartaDto adicionarAlternativaNaCarta(Integer id, RequestNovaAlternativaDto novaAlternativa) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        var alternativa = new Alternativa(obj -> {
            obj.setCarta(carta);
            obj.setDescricao(novaAlternativa.getDescricao());
        });
        alternativaRepository.save(alternativa);
        return customMapper.converterCartaComAlternativa(carta);
    }

    public void deletarAlternativaNaCarta(Integer id) {
        var alternativa = alternativaRepository.findById(id).orElseThrow(() -> new AlternativaNaoEncontradaException(id));
        alternativaRepository.delete(alternativa);
    }
    
}
