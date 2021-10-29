package br.com.lutadeclasses.jornadaservice.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import br.com.lutadeclasses.jornadaservice.entity.Acao;
import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Barra;
import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.exception.notfound.AcaoNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.BarraNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.CartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.model.CustomCartaMapper;
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
    private CustomCartaMapper customCartaMapper;

    public CartaService(CartaRepository cartaRepository, AlternativaRepository alternativaRepository, AcaoRepository acaoRepository, BarraRepository barraRepository, CustomCartaMapper customCartaMapper) {
        this.cartaRepository = cartaRepository;
        this.alternativaRepository = alternativaRepository;
        this.acaoRepository = acaoRepository;
        this.barraRepository = barraRepository;
        this.customCartaMapper = customCartaMapper;
    }

    public List<CartaDto> listarCartas() {
        return cartaRepository.findAll()
                              .stream()
                              .map(carta -> customCartaMapper.converterCartaComAlternativas(carta))
                              .collect(Collectors.toList());
    }

    public CartaDto buscarCartaPorId(Integer id) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        return customCartaMapper.converterCartaComAlternativas(carta);
    }

    public CartaDto criarCarta(NovaCartaDto novaCartaDto) {
        var carta = montarCarta(novaCartaDto);
        return customCartaMapper.converterCartaComAlternativas(cartaRepository.save(carta));
    }

    public void deletarCarta(Integer id) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        cartaRepository.delete(carta);
    }

    public CartaDto adicionarAlternativaNaCarta(Integer id, NovaAlternativaDto novaAlternativaDto) {
        var carta = cartaRepository.findById(id).orElseThrow(() -> new CartaNaoEncontradaException(id));
        var alternativa = montarAlternativa(novaAlternativaDto, carta);
        alternativaRepository.save(alternativa);
        return customCartaMapper.converterCartaComAlternativas(carta);
    }

    public void deletarAlternativaNaCarta(Integer cartaId, Integer alternativaId) {
        var alternativa = alternativaRepository.findByIdAndCarta_Id(alternativaId, cartaId)
                                               .orElseThrow(() -> new AcaoNaoEncontradaException(alternativaId));
        alternativaRepository.delete(alternativa);
    }

    public CartaDto adicionarAcaoNaAlternativa(Integer cartaId, Integer alternativaId, NovaAcaoDto novaAcaoDto) {
        var alternativa = alternativaRepository.findByIdAndCarta_Id(alternativaId, cartaId)
                                               .orElseThrow(() -> new AcaoNaoEncontradaException(alternativaId));
        List<Acao> acoes = alternativa.getAcoes();
        acoes.add(montarAcao(novaAcaoDto, alternativa));
        alternativaRepository.save(alternativa);
        return customCartaMapper.converterCartaComAlternativas(alternativa.getCarta());
    }

    public void deletarAcaoNaAlternativa(Integer cartaId, Integer alternativaId, Integer acaoId) {
        var acao = acaoRepository.findByIdAndAlternativa_Id(acaoId, alternativaId)
                                 .orElseThrow(() -> new AcaoNaoEncontradaException(acaoId));

        acaoRepository.delete(acao);
    }

    private Carta montarCarta(NovaCartaDto novaCartaDto) {
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
        Barra barra = barras.stream()
                            .filter(obj -> obj.getId().equals(novaAcaoDto.getBarraId()))
                            .findFirst()
                            .orElseThrow(() -> new BarraNaoEncontradaException(novaAcaoDto.getBarraId()));

        return new Acao(obj -> {
            obj.setAlternativa(alternativa);
            obj.setBarra(barra);
            obj.setTipo(novaAcaoDto.getTipo());
            obj.setValor(novaAcaoDto.getValor());
        });
    }

    @PostConstruct
    private void popularListaDeBarras() {
        barras = barraRepository.findAll();
    }

}
