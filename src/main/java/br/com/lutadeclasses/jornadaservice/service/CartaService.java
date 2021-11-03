package br.com.lutadeclasses.jornadaservice.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import br.com.lutadeclasses.jornadaservice.converter.CartaConverter;
import br.com.lutadeclasses.jornadaservice.entity.Acao;
import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Barra;
import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.exception.notfound.AcaoNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.BarraNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.CartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.validation.AlternativaComEstaDescricaoNaCartaJaExisteException;
import br.com.lutadeclasses.jornadaservice.exception.validation.CartaComEstaDescricaoJaExisteException;
import br.com.lutadeclasses.jornadaservice.model.request.NovaAcaoDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.CartaDto;
import br.com.lutadeclasses.jornadaservice.repository.AcaoRepository;
import br.com.lutadeclasses.jornadaservice.repository.AlternativaRepository;
import br.com.lutadeclasses.jornadaservice.repository.BarraRepository;
import br.com.lutadeclasses.jornadaservice.repository.CartaRepository;

@Service
public class CartaService {
    private List<Barra> barras;
    
    private CartaRepository cartaRepository;
    private AlternativaRepository alternativaRepository;
    private AcaoRepository acaoRepository;
    private BarraRepository barraRepository;

    public CartaService(CartaRepository cartaRepository, AlternativaRepository alternativaRepository, AcaoRepository acaoRepository, BarraRepository barraRepository) {
        this.cartaRepository = cartaRepository;
        this.alternativaRepository = alternativaRepository;
        this.acaoRepository = acaoRepository;
        this.barraRepository = barraRepository;
    }

    public List<CartaDto> listarCartas() {
        return cartaRepository.findAll()
                              .stream()
                              .map(CartaConverter::converterCartaComAlternativas)
                              .collect(Collectors.toList());
    }

    public Carta buscarCarta(Integer id) {
        return cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
    }

    public Carta criarCarta(NovaCartaDto novaCartaDto) {
        var carta = montarCarta(novaCartaDto);
        return cartaRepository.save(carta);
    }

    public void deletarCarta(Integer id) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        cartaRepository.delete(carta);
    }

    public Carta adicionarAlternativaNaCarta(Integer id, NovaAlternativaDto novaAlternativaDto) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        var alternativa = montarAlternativa(novaAlternativaDto, carta);
        alternativaRepository.save(alternativa);
        return carta;
    }

    public void deletarAlternativaNaCarta(Integer cartaId, Integer alternativaId) {
        var alternativa = alternativaRepository.findByIdAndCarta_Id(alternativaId, cartaId)
                                               .orElseThrow(() -> new AcaoNaoEncontradaException(alternativaId));
        alternativaRepository.delete(alternativa);
    }

    public Carta adicionarAcaoNaAlternativa(Integer cartaId, Integer alternativaId, NovaAcaoDto novaAcaoDto) {
        var alternativa = alternativaRepository.findByIdAndCarta_Id(alternativaId, cartaId)
                                               .orElseThrow(() -> new AcaoNaoEncontradaException(alternativaId));
        List<Acao> acoes = alternativa.getAcoes();
        acoes.add(montarAcao(novaAcaoDto, alternativa));
        alternativaRepository.save(alternativa);
        return alternativa.getCarta();
    }

    public void deletarAcaoNaAlternativa(Integer cartaId, Integer alternativaId, Integer acaoId) {
        var acao = acaoRepository.findByIdAndAlternativa_Id(acaoId, alternativaId)
                                 .orElseThrow(() -> new AcaoNaoEncontradaException(acaoId));

        acaoRepository.delete(acao);
    }

    public Barra buscarBarra(Integer barraId) {
        return barras.stream()
                     .filter(obj -> obj.getId().equals(barraId))
                     .findFirst()
                     .orElseThrow(() -> new BarraNaoEncontradaException(barraId));
    }

    private Carta montarCarta(NovaCartaDto novaCartaDto) {
        validarNovaCarta(novaCartaDto);
        var carta = new Carta(obj -> {
            obj.setDescricao(novaCartaDto.getDescricao());
            obj.setAtor(novaCartaDto.getAtor());
        });
        
        carta.setAlternativas(CollectionUtils.emptyIfNull(novaCartaDto.getAlternativas())
                                             .stream()
                                             .map(novaAlternativa -> montarAlternativa(novaAlternativa, carta))
                                             .collect(Collectors.toList()));

        return carta;
    }

    private Alternativa montarAlternativa(NovaAlternativaDto novaAlternativaDto, Carta carta) {
        validarNovaAlternativa(novaAlternativaDto, carta);
        var alternativa = new Alternativa(obj -> { 
            obj.setCarta(carta);
            obj.setDescricao(novaAlternativaDto.getDescricao());
        });

        alternativa.setAcoes(CollectionUtils.emptyIfNull(novaAlternativaDto.getAcoes())
                                            .stream()
                                            .map(novaAcao -> montarAcao(novaAcao, alternativa))
                                            .collect(Collectors.toList()));

        return alternativa;
    }

    private Acao montarAcao(NovaAcaoDto novaAcaoDto, Alternativa alternativa) {
        Barra barra = buscarBarra(novaAcaoDto.getBarraId());
        return new Acao(obj -> {
            obj.setAlternativa(alternativa);
            obj.setBarra(barra);
            obj.setTipo(novaAcaoDto.getTipo());
            obj.setValor(novaAcaoDto.getValor());
        });
    }

    private void validarNovaCarta(NovaCartaDto novaCartaDto) {
        var carta = cartaRepository.findByDescricao(novaCartaDto.getDescricao());
        if (carta.isPresent()) {
            throw new CartaComEstaDescricaoJaExisteException(novaCartaDto.getDescricao());
        }
    }

    private void validarNovaAlternativa(NovaAlternativaDto novaAlternativaDto, Carta carta) {
        var alternativa = alternativaRepository.findByDescricaoAndCarta_Id(novaAlternativaDto.getDescricao(), carta.getId());
        if (alternativa.isPresent()) {
            throw new AlternativaComEstaDescricaoNaCartaJaExisteException(novaAlternativaDto.getDescricao(), carta.getId());
        }
    }

    @PostConstruct
    private void popularListaDeBarras() {
        barras = barraRepository.findAll();
    }

}
