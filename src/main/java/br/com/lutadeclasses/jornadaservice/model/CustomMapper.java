package br.com.lutadeclasses.jornadaservice.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;
import br.com.lutadeclasses.jornadaservice.model.response.AlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.response.CartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;

@Component
public class CustomMapper {

    private ObjectMapper mapper;

    public CustomMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    
    public CartaDto converterCartaComAlternativa(Carta carta) {
        List<AlternativaDto> alternativas = carta.getAlternativas()
                                                 .stream()
                                                 .map(alternativa -> mapper.convertValue(alternativa, AlternativaDto.class))
                                                 .collect(Collectors.toList());

        return CartaDto.builder()
                       .id(carta.getId())
                       .descricao(carta.getDescricao())
                       .alternativas(alternativas)
                       .build();
    }

    public JornadaDto converterJornadaComCartas(Jornada jornada) {
        List<JornadaCartaDto> jornadaCartas = jornada.getJornadasCartas()
                                                     .stream()
                                                     .map(this::converterJornadaCarta)
                                                     .collect(Collectors.toList());
        return JornadaDto.builder()
                                 .id(jornada.getId())
                                 .titulo(jornada.getTitulo())
                                 .cartas(jornadaCartas)
                                 .build();
    }

    private JornadaCartaDto converterJornadaCarta(JornadaCarta jornadaCarta) {
        return JornadaCartaDto.builder()
                              .posicao(jornadaCarta.getPosicao())
                              .carta(converterCartaComAlternativa(jornadaCarta.getCarta()))
                              .build();
    }
    
}
