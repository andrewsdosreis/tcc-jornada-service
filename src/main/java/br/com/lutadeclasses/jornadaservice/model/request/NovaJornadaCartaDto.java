package br.com.lutadeclasses.jornadaservice.model.request;

import javax.validation.constraints.NotNull;

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
public class NovaJornadaCartaDto {
    @NotNull(message = "O campo 'cartaId' não pode ser nulo")
    private Integer cartaId;
}