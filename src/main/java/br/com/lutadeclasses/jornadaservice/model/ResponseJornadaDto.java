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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ResponseJornadaDto {

    private Integer id;
    private String titulo;
    private List<ResponseJornadaCartaDto> cartas;

}
