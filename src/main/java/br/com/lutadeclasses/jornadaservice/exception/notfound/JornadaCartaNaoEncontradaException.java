package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class JornadaCartaNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public JornadaCartaNaoEncontradaException(Integer jornadaCartaId) {
        super(String.format("JornadaCarta [id '%s'] nao foi encontrada", jornadaCartaId));
    }

    public JornadaCartaNaoEncontradaException(Integer jornadaId, Integer cartaId) {
        super(String.format("JornadaCarta com Jornada [id '%s'] e Carta [id '%s'] nao foi encontrada", jornadaId, cartaId));
    }
    
}
