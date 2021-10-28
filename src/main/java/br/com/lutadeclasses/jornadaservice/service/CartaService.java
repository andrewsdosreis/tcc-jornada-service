package br.com.lutadeclasses.jornadaservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.exception.AlternativaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.CartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.model.CustomMapper;
import br.com.lutadeclasses.jornadaservice.model.request.NovaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.CartaDto;
import br.com.lutadeclasses.jornadaservice.repository.AlternativaRepository;
import br.com.lutadeclasses.jornadaservice.repository.CartaRepository;

@Service
public class CartaService {
    
    private CartaRepository cartaRepository;
    private AlternativaRepository alternativaRepository;
    private CustomMapper customMapper;

    public CartaService(CartaRepository cartaRepository, AlternativaRepository alternativaRepository, CustomMapper customMapper) {
        this.cartaRepository = cartaRepository;
        this.alternativaRepository = alternativaRepository;
        this.customMapper = customMapper;
    }

    public List<CartaDto> listarCartas() {
        return cartaRepository.findAll()
                              .stream()
                              .map(carta -> customMapper.converterCartaComAlternativa(carta))
                              .collect(Collectors.toList());
    }

    public CartaDto buscarCartaPorId(Integer id) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        return customMapper.converterCartaComAlternativa(carta);
    }

    public CartaDto criarCarta(NovaCartaDto novaCartaDto) {
        var carta = montarCarta(novaCartaDto);
        carta.setAlternativas(CollectionUtils.emptyIfNull(novaCartaDto.getAlternativas())
                                             .stream()
                                             .map(novaAlternativa -> montarAlternativa(novaAlternativa, carta))
                                             .collect(Collectors.toList()));

        return customMapper.converterCartaComAlternativa(cartaRepository.save(carta));
    }

    public void deletarCarta(Integer id) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        cartaRepository.delete(carta);
    }

    public CartaDto adicionarAlternativaNaCarta(Integer id, NovaAlternativaDto novaAlternativaDto) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        var alternativa = montarAlternativa(novaAlternativaDto, carta);
        alternativaRepository.save(alternativa);
        return customMapper.converterCartaComAlternativa(carta);
    }

    public void deletarAlternativaNaCarta(Integer id) {
        var alternativa = alternativaRepository.findById(id).orElseThrow(() -> new AlternativaNaoEncontradaException(id));
        alternativaRepository.delete(alternativa);
    }

    private Carta montarCarta(NovaCartaDto novaCartaDto) {
        return new Carta(obj -> {
            obj.setDescricao(novaCartaDto.getDescricao());
            obj.setAtor(novaCartaDto.getAtor());
        });
    }

    private Alternativa montarAlternativa(NovaAlternativaDto novaAlternativaDto, Carta carta) {
        return new Alternativa(obj -> {
            obj.setCarta(carta);
            obj.setDescricao(novaAlternativaDto.getDescricao());
        });
    }

}
