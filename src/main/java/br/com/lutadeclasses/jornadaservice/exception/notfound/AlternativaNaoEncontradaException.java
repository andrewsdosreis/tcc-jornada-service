package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class AlternativaNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public AlternativaNaoEncontradaException(Integer alternativaId) {
        super(String.format("Alternativa [id '%s'] nao foi encontrada", alternativaId));
    }

    public AlternativaNaoEncontradaException(Integer alternativaId, Integer cartaId) {
        super(String.format("Alternativa [id '%s'] na Carta [id '%s'] nao foi encontrada", alternativaId, cartaId));
    }
}
