package br.com.lutadeclasses.jornadaservice.exception.notfound;

public class CartaNaoEncontradaException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public CartaNaoEncontradaException(Integer cartaId) {
        super(String.format("Carta [id '%s'] nao foi encontrada", cartaId));
    }

}
