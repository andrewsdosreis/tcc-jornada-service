package br.com.lutadeclasses.jornadaservice.exception;

public class AlternativaNaoEncontradaException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public AlternativaNaoEncontradaException(Integer id) {
        super(String.format("Alternativa com o Id '%s' nao foi encontrado", id));
    }
    
}
