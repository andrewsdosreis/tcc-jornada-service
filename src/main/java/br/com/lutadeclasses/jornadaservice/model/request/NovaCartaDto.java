package br.com.lutadeclasses.jornadaservice.model.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class NovaCartaDto {
    @NotBlank(message = "O campo 'descricao' não pode ser vazio")
    private String descricao;
    
    @NotBlank(message = "O campo 'ator' não pode ser vazio")
    private String ator;
    
    private List<NovaAlternativaDto> alternativas;
}