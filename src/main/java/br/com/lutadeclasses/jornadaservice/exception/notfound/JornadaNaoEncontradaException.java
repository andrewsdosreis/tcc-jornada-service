package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class JornadaNaoEncontradaException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public JornadaNaoEncontradaException(Integer id) {
        super(String.format("Jornada com o Id '%s' nao foi encontrado", id));
    }
    
}
