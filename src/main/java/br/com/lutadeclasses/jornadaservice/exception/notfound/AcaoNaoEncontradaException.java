package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class AcaoNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public AcaoNaoEncontradaException(Integer acaoId) {
        super(String.format("Acao [id '%s'] nao foi encontrada", acaoId));
    }

    public AcaoNaoEncontradaException(Integer acaoId, Integer alternativaId) {
        super(String.format("Acao [id '%s'] na Alternativa [id '%s'] nao foi encontrada", acaoId, alternativaId));
    }
    
}
