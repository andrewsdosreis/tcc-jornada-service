package br.com.lutadeclasses.jornadaservice.model.request;

import javax.validation.constraints.NotBlank;

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
public class NovaJornadaDto {
    @NotBlank(message = "O campo 'titulo' não pode ser vazio")    
    private String titulo;
}