package br.com.lutadeclasses.jornadaservice.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ResponseCartaDto {
    
    private Integer id;
    private String descricao;
    private List<ResponseAlternativaDto> alternativas;

}
