package br.com.lutadeclasses.jornadaservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;
import br.com.lutadeclasses.jornadaservice.exception.notfound.AlternativaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.CartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaAlternativaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.validation.JornadaCartaJaExisteException;
import br.com.lutadeclasses.jornadaservice.mapper.JornadaMapper;
import br.com.lutadeclasses.jornadaservice.model.enumeration.PosicaoCartaEnum;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;
import br.com.lutadeclasses.jornadaservice.repository.AlternativaRepository;
import br.com.lutadeclasses.jornadaservice.repository.CartaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaAlternativaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaCartaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaRepository;

@Service
public class JornadaService {

    private JornadaRepository jornadaRepository;
    private JornadaCartaRepository jornadaCartaRepository;
    private JornadaAlternativaRepository jornadaAlternativaRepository;
    private CartaRepository cartaRepository;
    private AlternativaRepository alternativaRepository;
    private JornadaMapper jornadaMapper;

    public JornadaService(JornadaRepository jornadaRepository, JornadaCartaRepository jornadaCartaRepository, JornadaAlternativaRepository jornadaAlternativaRepository,
            CartaRepository cartaRepository, AlternativaRepository alternativaRepository, JornadaMapper customMapper) {
        this.jornadaRepository = jornadaRepository;
        this.jornadaCartaRepository = jornadaCartaRepository;
        this.jornadaAlternativaRepository = jornadaAlternativaRepository;
        this.cartaRepository = cartaRepository;
        this.alternativaRepository = alternativaRepository;
        this.jornadaMapper = customMapper;
    }

    public List<JornadaDto> listarJornadas() {
        return jornadaRepository.findAll()
                                .stream()
                                .map(jornada -> jornadaMapper.converterJornadaSemCartas(jornada))
                                .collect(Collectors.toList());
    }

    public JornadaDto buscarJornadaPorId(Integer id) {
        var jornada = jornadaRepository.findById(id).orElseThrow(() -> new JornadaNaoEncontradaException(id));
        return jornadaMapper.converterJornadaComCartas(jornada);
    }
    
    public JornadaDto criarJornada(NovaJornadaDto novaJornada) {
        var jornada = jornadaRepository.save(new Jornada(obj -> obj.setTitulo(novaJornada.getTitulo())));
        return jornadaMapper.converterJornadaComCartas(jornada);
    }
    
    public JornadaDto adicionarCartaNaJornada(Integer jornadaId, NovaJornadaCartaDto novaJornadaCarta) {
        validarSeCartaJaExisteNaJornada(jornadaId, novaJornadaCarta.getCartaId());

        var jornada = jornadaRepository.findById(jornadaId).orElseThrow(() -> new JornadaNaoEncontradaException(jornadaId));
        var jornadaCarta = montarJornadaCarta(jornada, novaJornadaCarta);
        jornadaCarta.setJornadasAlternativas(CollectionUtils.emptyIfNull(jornadaCarta.getCarta().getAlternativas())
                                                            .stream()
                                                            .map(alternativa -> montarJornadaAlternativa(jornadaCarta, alternativa.getId()))
                                                            .collect(Collectors.toList()));
        jornadaCartaRepository.save(jornadaCarta);
        return jornadaMapper.converterJornadaComCartas(jornada);
    }

    public JornadaCartaDto definirProximaCarta(Integer jornadaId, Integer cartaId, NovaJornadaAlternativaDto novaJornadaAlternativaDto) {
        var jornadaCarta = jornadaCartaRepository.findByJornada_IdAndCarta_Id(jornadaId, cartaId)
                                                 .orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaId, cartaId));

        var proximaJornadaCarta = jornadaCartaRepository.findByJornada_IdAndCarta_Id(jornadaId, novaJornadaAlternativaDto.getProximaCartaId())
                                                        .orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaId, novaJornadaAlternativaDto.getProximaCartaId()));

        var jornadaAlternativa = jornadaCarta.getJornadasAlternativas()
                                             .stream()
                                             .filter(alternativa -> alternativa.getAlternativa().getId().equals(novaJornadaAlternativaDto.getAlternativaId()))
                                             .findFirst()
                                             .orElseThrow(() -> new JornadaAlternativaNaoEncontradaException(jornadaId, cartaId, novaJornadaAlternativaDto.getAlternativaId()));

        proximaJornadaCarta.setPosicao(PosicaoCartaEnum.MEIO.toString());
        jornadaCartaRepository.save(proximaJornadaCarta);

        jornadaAlternativa.setProximaJornadaCarta(proximaJornadaCarta);
        jornadaAlternativaRepository.save(jornadaAlternativa);

        return jornadaMapper.converterJornadaCartaComAlternativas(jornadaCarta);
    }

    public void deletarCartaDaJornada(Integer jornadaId, Integer cartaId) {
        var jornadaCarta = jornadaCartaRepository.findByJornada_IdAndCarta_Id(jornadaId, cartaId)
                                                 .orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaId, cartaId));
        jornadaCartaRepository.delete(jornadaCarta);
    }

    private JornadaCarta montarJornadaCarta(Jornada jornada, NovaJornadaCartaDto novaJornadaCarta) {
        var carta = cartaRepository.findById(novaJornadaCarta.getCartaId()).orElseThrow(() -> new CartaNaoEncontradaException(novaJornadaCarta.getCartaId()));
        return new JornadaCarta(obj -> {
            obj.setJornada(jornada);
            obj.setCarta(carta);
            obj.setPosicao(definirPosicaoDaCarta(carta.getAlternativas()));
        });
    }

    private JornadaAlternativa montarJornadaAlternativa(JornadaCarta jornadaCarta, Integer alternativaId) {
        var alternativa = alternativaRepository.findById(alternativaId).orElseThrow(() -> new AlternativaNaoEncontradaException(alternativaId));
        return new JornadaAlternativa(obj -> {
            obj.setJornadaCarta(jornadaCarta);
            obj.setAlternativa(alternativa);
        });
    }

    private String definirPosicaoDaCarta(List<Alternativa> alternativas) {
        return alternativas.isEmpty() ? PosicaoCartaEnum.VITORIA.toString() : PosicaoCartaEnum.INICIO.toString();
    }

    private void validarSeCartaJaExisteNaJornada(Integer jornadaId, Integer cartaId) {
        var jornadaCarta = jornadaCartaRepository.findByJornada_IdAndCarta_Id(jornadaId, cartaId);
        if (jornadaCarta.isPresent()) {
            throw new JornadaCartaJaExisteException(jornadaId, cartaId);
        }        
    }

}