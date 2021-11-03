package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class JornadaAlternativaNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public JornadaAlternativaNaoEncontradaException(Integer jornadaAlternativaId) {
        super(String.format("JornadaAlternativa [id '%s'] nao foi encontrada", jornadaAlternativaId));
    }

    public JornadaAlternativaNaoEncontradaException(Integer jornadaId, Integer cartaId, Integer alternativaId) {
        super(String.format("JornadaAlternativa com Jornada [id '%s'], Carta [id '%s'] e Alternativa [id '%s'] nao foi encontrada", jornadaId, cartaId, alternativaId));
    }
    
}
