package br.com.lutadeclasses.jornadaservice.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;

@Component
public class CustomMapper {

    private ObjectMapper mapper;

    public CustomMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    
    public ResponseCartaDto converterCartaComAlternativa(Carta carta) {
        List<ResponseAlternativaDto> alternativas = carta.getAlternativas()
                                                         .stream()
                                                         .map(alternativa -> mapper.convertValue(alternativa, ResponseAlternativaDto.class))
                                                         .collect(Collectors.toList());

        return ResponseCartaDto.builder()
                               .id(carta.getId())
                               .descricao(carta.getDescricao())
                               .alternativas(alternativas)
                               .build();
    }

    public ResponseJornadaDto converterJornadaComCartas(Jornada jornada) {
        List<ResponseJornadaCartaDto> jornadaCartas = jornada.getJornadaCartas()
                                                             .stream()
                                                             .map(this::converterJornadaCarta)
                                                             .collect(Collectors.toList());
        return ResponseJornadaDto.builder()
                                 .id(jornada.getId())
                                 .titulo(jornada.getTitulo())
                                 .cartas(jornadaCartas)
                                 .build();
    }

    private ResponseJornadaCartaDto converterJornadaCarta(JornadaCarta jornadaCarta) {
        return ResponseJornadaCartaDto.builder()
                                      .pontoInicial(jornadaCarta.getPontoInicial())
                                      .carta(converterCartaComAlternativa(jornadaCarta.getCarta()))
                                      .build();
    }
    
}
