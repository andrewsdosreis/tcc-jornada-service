package br.com.lutadeclasses.jornadaservice.model.request;

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
@Builder
@EqualsAndHashCode
public class NovaJornadaCartaDto {
    private Integer cartaId;
    private Boolean pontoInicial;
}