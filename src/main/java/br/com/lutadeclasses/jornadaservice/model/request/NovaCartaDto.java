package br.com.lutadeclasses.jornadaservice.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NovaCartaDto {
    private String descricao;
    private String ator;
    private List<NovaAlternativaDto> alternativas;
}