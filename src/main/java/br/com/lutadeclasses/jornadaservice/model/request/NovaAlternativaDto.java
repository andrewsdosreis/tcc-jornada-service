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
public class NovaAlternativaDto {
    @NotBlank(message = "O campo 'descricao' não pode ser vazio")
    private String descricao;
    
    private List<NovaAcaoDto> acoes;
}