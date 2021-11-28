package br.com.lutadeclasses.jornadaservice.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class NovaAcaoDto {
    @NotBlank(message = "O campo 'barra' não pode ser vazio")
    private String barra;
    
    @NotBlank(message = "O campo 'tipo' não pode ser vazio")
    private String tipo;
    
    @NotNull(message = "O campo 'valor' não pode ser nulo")
    private Integer valor;
}