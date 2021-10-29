package br.com.lutadeclasses.jornadaservice.model.request;

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
public class NovaAcaoDto {
    private Integer barraId;
    private String tipo;
    private Integer valor;
}