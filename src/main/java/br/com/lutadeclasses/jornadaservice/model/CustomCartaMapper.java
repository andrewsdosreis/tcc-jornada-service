package br.com.lutadeclasses.jornadaservice.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.lutadeclasses.jornadaservice.entity.Acao;
import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.model.response.AcaoDto;
import br.com.lutadeclasses.jornadaservice.model.response.AlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.response.CartaDto;

@Component
public class CustomCartaMapper {
    
    public CartaDto converterCartaComAlternativas(Carta carta) {
        List<AlternativaDto> alternativas = carta.getAlternativas()
                                                 .stream()
                                                 .map(this::converterAlternativaComAcoes)
                                                 .collect(Collectors.toList());

        return CartaDto.builder()
                       .id(carta.getId())
                       .descricao(carta.getDescricao())
                       .alternativas(alternativas)
                       .build();
    }

    private AlternativaDto converterAlternativaComAcoes(Alternativa alternativa) {
        List<AcaoDto> acoes = alternativa.getAcoes()
                                         .stream()
                                         .map(this::converterAcao)
                                         .collect(Collectors.toList());
        return AlternativaDto.builder()
                             .id(alternativa.getId())
                             .descricao(alternativa.getDescricao())
                             .acoes(acoes)
                             .build();
    }

    private AcaoDto converterAcao(Acao acao) {
        return AcaoDto.builder()
                      .id(acao.getId())
                      .tipo(acao.getTipo())
                      .valor(acao.getValor())
                      .barra(acao.getBarra().getDescricao())
                      .build();
    }

}
