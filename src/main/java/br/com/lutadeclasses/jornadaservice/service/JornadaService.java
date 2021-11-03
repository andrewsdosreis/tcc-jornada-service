package br.com.lutadeclasses.jornadaservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import br.com.lutadeclasses.jornadaservice.converter.JornadaConverter;
import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Barra;
import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCartaDerrota;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaAlternativaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.notfound.JornadaNaoEncontradaException;
import br.com.lutadeclasses.jornadaservice.exception.validation.JornadaCartaJaExisteException;
import br.com.lutadeclasses.jornadaservice.model.enumeration.PosicaoCartaEnum;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.request.NovaJornadaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;
import br.com.lutadeclasses.jornadaservice.repository.JornadaAlternativaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaCartaDerrotaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaCartaRepository;
import br.com.lutadeclasses.jornadaservice.repository.JornadaRepository;

@Service
public class JornadaService {

    private JornadaRepository jornadaRepository;
    private JornadaCartaRepository jornadaCartaRepository;
    private JornadaAlternativaRepository jornadaAlternativaRepository;
    private JornadaCartaDerrotaRepository jornadaCartaDerrotaRepository;

    public JornadaService(JornadaRepository jornadaRepository, JornadaCartaRepository jornadaCartaRepository,
            JornadaAlternativaRepository jornadaAlternativaRepository,
            JornadaCartaDerrotaRepository jornadaCartaDerrotaRepository) {
        this.jornadaRepository = jornadaRepository;
        this.jornadaCartaRepository = jornadaCartaRepository;
        this.jornadaAlternativaRepository = jornadaAlternativaRepository;
        this.jornadaCartaDerrotaRepository = jornadaCartaDerrotaRepository;
    }

    public List<JornadaDto> listarJornadas() {
        return jornadaRepository.findAll()
                                .stream()
                                .map(JornadaConverter::converterJornadaSemCartas)
                                .collect(Collectors.toList());
    }

    public Jornada buscarJornadaPorId(Integer id) {
        return jornadaRepository.findById(id).orElseThrow(() -> new JornadaNaoEncontradaException(id));
    }
    
    public Jornada criarJornada(NovaJornadaDto novaJornada) {
        return jornadaRepository.save(new Jornada(obj -> obj.setTitulo(novaJornada.getTitulo())));
    }
    
    public Jornada adicionarCartaNaJornada(Integer jornadaId, Carta carta) {
        validarSeCartaJaExisteNaJornada(jornadaId, carta.getId());

        var jornada = jornadaRepository.findById(jornadaId).orElseThrow(() -> new JornadaNaoEncontradaException(jornadaId));
        var jornadaCarta = montarJornadaCarta(jornada, carta);
        jornadaCarta.setJornadasAlternativas(CollectionUtils.emptyIfNull(jornadaCarta.getCarta().getAlternativas())
                                                            .stream()
                                                            .map(alternativa -> montarJornadaAlternativa(jornadaCarta, alternativa))
                                                            .collect(Collectors.toList()));
        jornadaCartaRepository.save(jornadaCarta);
        return jornada;
    }

    public JornadaCarta definirProximaCarta(Integer jornadaId, Integer cartaId, NovaJornadaAlternativaDto novaJornadaAlternativaDto) {
        var jornadaCarta = jornadaCartaRepository.findByJornada_IdAndCarta_Id(jornadaId, cartaId)
                                                 .orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaId, cartaId));

        var proximaJornadaCarta = jornadaCartaRepository.findByJornada_IdAndCarta_Id(jornadaId, novaJornadaAlternativaDto.getProximaCartaId())
                                                        .orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaId, novaJornadaAlternativaDto.getProximaCartaId()));

        var jornadaAlternativa = jornadaCarta.getJornadasAlternativas()
                                             .stream()
                                             .filter(alternativa -> alternativa.getAlternativa().getId().equals(novaJornadaAlternativaDto.getAlternativaId()))
                                             .findFirst()
                                             .orElseThrow(() -> new JornadaAlternativaNaoEncontradaException(jornadaId, cartaId, novaJornadaAlternativaDto.getAlternativaId()));

        if (proximaJornadaCarta.getPosicao().equals(PosicaoCartaEnum.INICIO.toString())) {
            proximaJornadaCarta.setPosicao(PosicaoCartaEnum.MEIO.toString());    
            jornadaCartaRepository.save(proximaJornadaCarta);
        }
        jornadaAlternativa.setProximaJornadaCarta(proximaJornadaCarta);
        jornadaAlternativaRepository.save(jornadaAlternativa);

        return jornadaCarta;
    }

    public void deletarCartaDaJornada(Integer jornadaId, Integer cartaId) {
        var jornadaCarta = jornadaCartaRepository.findByJornada_IdAndCarta_Id(jornadaId, cartaId)
                                                 .orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaId, cartaId));
        jornadaCartaRepository.delete(jornadaCarta);
    }

    public JornadaCarta adicionarJornadaCartaDerrota(Integer jornadaId, Carta carta, Barra barra) {
        var jornadaCarta = jornadaCartaRepository.findByJornada_IdAndCarta_Id(jornadaId, carta.getId())
                                                 .orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaId, carta.getId()));
        var jornadaCartaDerrota = new JornadaCartaDerrota(obj -> {
            obj.setJornadaCarta(jornadaCarta);
            obj.setBarra(barra);
        });
        jornadaCarta.setPosicao(PosicaoCartaEnum.DERROTA.toString());
        jornadaCartaRepository.save(jornadaCarta);
        jornadaCartaDerrotaRepository.save(jornadaCartaDerrota);
        return jornadaCarta;
    }

    private JornadaCarta montarJornadaCarta(Jornada jornada, Carta carta) {
        return new JornadaCarta(obj -> {
            obj.setJornada(jornada);
            obj.setCarta(carta);
            obj.setPosicao(definirPosicaoDaCarta(carta.getAlternativas()));
        });
    }

    private JornadaAlternativa montarJornadaAlternativa(JornadaCarta jornadaCarta, Alternativa alternativa) {
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