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
public class NovaJornadaAlternativaDto {
    @NotNull(message = "O campo 'alternativaId' não pode ser nulo")
    private Integer alternativaId;

    @NotNull(message = "O campo 'proximaCartaId' não pode ser nulo")
    private Integer proximaCartaId;
}