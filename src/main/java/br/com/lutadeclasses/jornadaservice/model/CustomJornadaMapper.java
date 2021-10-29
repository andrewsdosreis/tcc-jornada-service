package br.com.lutadeclasses.jornadaservice.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;

@Component
public class CustomJornadaMapper {
    
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
                              .carta(jornadaCarta.getCarta().getDescricao())
                              .ator(jornadaCarta.getCarta().getAtor())
                              .build();
    }

}
