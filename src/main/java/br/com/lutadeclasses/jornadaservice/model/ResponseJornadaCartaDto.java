package br.com.lutadeclasses.jornadaservice.model;

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
public class ResponseJornadaCartaDto {

    private Boolean pontoInicial;
    private ResponseCartaDto carta;

}
