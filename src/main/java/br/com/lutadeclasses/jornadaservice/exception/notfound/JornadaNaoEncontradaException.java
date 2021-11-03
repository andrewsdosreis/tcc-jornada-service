package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class JornadaNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public JornadaNaoEncontradaException(Integer jornadaId) {
        super(String.format("Jornada [id '%s'] nao foi encontrada", jornadaId));
    }
    
}
