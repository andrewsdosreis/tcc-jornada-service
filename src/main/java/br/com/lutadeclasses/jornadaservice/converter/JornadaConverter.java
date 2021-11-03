package br.com.lutadeclasses.jornadaservice.converter;

import java.util.Objects;
import java.util.stream.Collectors;

import br.com.lutadeclasses.jornadaservice.entity.Jornada;
import br.com.lutadeclasses.jornadaservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.jornadaservice.entity.JornadaCarta;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaAlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaCartaDto;
import br.com.lutadeclasses.jornadaservice.model.response.JornadaDto;

public interface JornadaConverter {
    
    public static JornadaDto converterJornadaSemCartas(Jornada jornada) {
        return converterJornada(jornada);
    }

    public static JornadaDto converterJornadaComCartas(Jornada jornada) {
        var jornadaDto = converterJornada(jornada);
        jornadaDto.setCartas(jornada.getJornadasCartas()
                                    .stream()
                                    .map(JornadaConverter::converterJornadaCarta)
                                    .collect(Collectors.toList()));
        return jornadaDto;
    }

    public static JornadaCartaDto converterJornadaCartaComAlternativas(JornadaCarta jornadaCarta) {
        return converterJornadaCarta(jornadaCarta);
    }

    private static JornadaDto converterJornada(Jornada jornada) {
        return JornadaDto.builder()
                         .id(jornada.getId())
                         .titulo(jornada.getTitulo())
                         .build();
    }

    private static JornadaCartaDto converterJornadaCarta(JornadaCarta jornadaCarta) {
        return JornadaCartaDto.builder()
                              .cartaId(jornadaCarta.getCarta().getId())
                              .ator(jornadaCarta.getCarta().getAtor())
                              .descricao(jornadaCarta.getCarta().getDescricao())
                              .posicao(jornadaCarta.getPosicao())
                              .alternativas(jornadaCarta.getJornadasAlternativas()
                                                        .stream()
                                                        .map(JornadaConverter::converterJornadaAlternativa)
                                                        .collect(Collectors.toList()))
                              .build();
    }

    private static JornadaAlternativaDto converterJornadaAlternativa(JornadaAlternativa jornadaAlternativa) {
        Integer proximaCartaId = null;
        String proximaCartaDescricao = null;

        if (Objects.nonNull(jornadaAlternativa.getProximaJornadaCarta())) {
            proximaCartaId = jornadaAlternativa.getProximaJornadaCarta().getCarta().getId();
            proximaCartaDescricao = jornadaAlternativa.getProximaJornadaCarta().getCarta().getDescricao();
        }

        return JornadaAlternativaDto.builder()
                                    .alternativaId(jornadaAlternativa.getAlternativa().getId())
                                    .descricao(jornadaAlternativa.getAlternativa().getDescricao())
                                    .proximaCartaId(proximaCartaId)
                                    .proximaCartaDescricao(proximaCartaDescricao)
                                    .build();
    }

}
