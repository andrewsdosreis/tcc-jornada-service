package br.com.lutadeclasses.jornadaservice.converter;

import java.util.List;
import java.util.stream.Collectors;

import br.com.lutadeclasses.jornadaservice.entity.Acao;
import br.com.lutadeclasses.jornadaservice.entity.Alternativa;
import br.com.lutadeclasses.jornadaservice.entity.Carta;
import br.com.lutadeclasses.jornadaservice.model.response.AcaoDto;
import br.com.lutadeclasses.jornadaservice.model.response.AlternativaDto;
import br.com.lutadeclasses.jornadaservice.model.response.CartaDto;

public interface CartaConverter {
    
    public static CartaDto converterCartaComAlternativas(Carta carta) {
        List<AlternativaDto> alternativas = carta.getAlternativas()
                                                 .stream()
                                                 .map(CartaConverter::converterAlternativaComAcoes)
                                                 .collect(Collectors.toList());

        return CartaDto.builder()
                       .id(carta.getId())
                       .descricao(carta.getDescricao())
                       .alternativas(alternativas)
                       .build();
    }

    private static AlternativaDto converterAlternativaComAcoes(Alternativa alternativa) {
        List<AcaoDto> acoes = alternativa.getAcoes()
                                         .stream()
                                         .map(CartaConverter::converterAcao)
                                         .collect(Collectors.toList());
        return AlternativaDto.builder()
                             .id(alternativa.getId())
                             .descricao(alternativa.getDescricao())
                             .acoes(acoes)
                             .build();
    }

    private static AcaoDto converterAcao(Acao acao) {
        return AcaoDto.builder()
                      .id(acao.getId())
                      .tipo(acao.getTipo())
                      .valor(acao.getValor())
                      .barra(acao.getBarra().getDescricao())
                      .build();
    }

}
